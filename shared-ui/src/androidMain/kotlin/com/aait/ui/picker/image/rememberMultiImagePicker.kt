package com.aait.ui.picker.image

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.aait.ui.component.sheet.ImageSourceBottomSheet
import com.aait.ui.util.files.CompressUtil.compressImageToWebPInCache
import com.aait.ui.util.files.CompressUtil.compressToString
import com.aait.ui.util.files.hasCameraPermission
import com.aait.ui.util.files.rememberCameraPermissionRequest
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun rememberMultiImagePicker(
    onImagesSelected: (List<String>) -> Unit
): () -> Unit {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var showBottomSheet by remember { mutableStateOf(false) }
    var cameraImageFile by remember { mutableStateOf<File?>(null) }
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia()
    ) { uris ->
        if (uris.isNotEmpty()) {
            scope.launch {
                val images = uris.mapNotNull { it.compressToString(context) }
                if (images.isNotEmpty()) {
                    onImagesSelected(images)
                }
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        val file = cameraImageFile
        cameraImageFile = null
        cameraImageUri = null

        if (success && file != null && file.exists()) {
            scope.launch {
                val compressed = try {
                    compressImageToWebPInCache(context, file.absolutePath)
                } catch (e: Exception) {
                    null
                }
                compressed?.let { onImagesSelected(listOf(it)) }
            }
        }
    }

    val requestCameraPermission = rememberCameraPermissionRequest(
        onPermissionGranted = {
            try {
                val photoFile = File.createTempFile(
                    "camera_photo_${System.currentTimeMillis()}", ".jpg", context.cacheDir
                )
                val uri = FileProvider.getUriForFile(
                    context, "${context.packageName}.fileprovider", photoFile
                )
                cameraImageFile = photoFile
                cameraImageUri = uri
                cameraLauncher.launch(uri)
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

    val onGallerySelected = {
        galleryLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    if (showBottomSheet) {
        ImageSourceBottomSheet(
            onCameraSelect = onCameraSelected,
            onGallerySelect = onGallerySelected,
            onDismiss = { showBottomSheet = false }
        )
    }

    return remember { { showBottomSheet = true } }
}
