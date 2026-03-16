package com.aait.ui.picker.image

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.FileProvider
import com.aait.ui.component.dialog.LoadingDialog
import com.aait.ui.component.sheet.MediaPickerBottomSheet
import com.aait.ui.component.sheet.MediaPickerMode
import com.aait.ui.util.files.CompressUtil.compressImageToWebPInCache
import com.aait.ui.util.files.CompressUtil.compressToString
import com.aait.ui.util.files.hasCameraPermission
import com.aait.ui.util.files.rememberCameraPermissionRequest
import com.mahalatak.shared.ui.R
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun rememberImagePicker(
    onImageSelected: (String) -> Unit
): () -> Unit {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var showBottomSheet by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var cameraImageFile by remember { mutableStateOf<File?>(null) }
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            scope.launch {
                isLoading = true
                try {
                    val compressed = uri.compressToString(context)
                    onImageSelected(compressed ?: "")
                } finally {
                    isLoading = false
                }
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        val fileToCompress = cameraImageFile
        val filePath = fileToCompress?.absolutePath

        cameraImageFile = null
        cameraImageUri = null

        if (success && filePath != null && fileToCompress?.exists() == true) {
            scope.launch {
                isLoading = true
                try {
                    val compressed = compressImageToWebPInCache(context, filePath)
                    if (!compressed.isNullOrEmpty()) {
                        onImageSelected(compressed)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    isLoading = false
                }
            }
        }
    }

    val requestCameraPermission = rememberCameraPermissionRequest(
        onPermissionGranted = {
            try {
                val photoFile = File.createTempFile(
                    "camera_photo_${System.currentTimeMillis()}", ".jpg", context.cacheDir
                )
                val photoUri = FileProvider.getUriForFile(
                    context, "${context.packageName}.fileprovider", photoFile
                )
                cameraImageFile = photoFile
                cameraImageUri = photoUri
                cameraLauncher.launch(photoUri)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        },
        onPermissionDenied = {}
    )

    val onCameraSelected = {
        if (context.hasCameraPermission()) {
            try {
                val photoFile = File.createTempFile(
                    "camera_photo_${System.currentTimeMillis()}", ".jpg", context.cacheDir
                )
                val photoUri = FileProvider.getUriForFile(
                    context, "${context.packageName}.fileprovider", photoFile
                )
                cameraImageFile = photoFile
                cameraImageUri = photoUri
                cameraLauncher.launch(photoUri)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            requestCameraPermission()
        }
    }

    val onGallerySelected = { galleryLauncher.launch("image/*") }

    if (showBottomSheet) {
        MediaPickerBottomSheet(
            mode = MediaPickerMode.IMAGE_ONLY,
            onCameraPhotoSelect = onCameraSelected,
            onGallerySelect = onGallerySelected,
            onDismiss = { showBottomSheet = false }
        )
    }

    if (isLoading) {
        LoadingDialog(message = stringResource(R.string.compressing_image))
    }

    return remember { { showBottomSheet = true } }
}
