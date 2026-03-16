package com.aait.base.common.picker.image

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
import com.aait.base.R
import com.aait.base.common.component.dialog.LoadingDialog
import com.aait.base.common.component.sheet.MediaPickerBottomSheet
import com.aait.base.common.component.sheet.MediaPickerMode
import com.aait.base.util.files.CompressUtil.compressImageToWebPInCache
import com.aait.base.util.files.CompressUtil.compressToString
import com.aait.base.util.files.hasCameraPermission
import com.aait.base.util.files.rememberCameraPermissionRequest
import kotlinx.coroutines.launch
import java.io.File

/**
 * Enhanced image picker that allows user to choose between Camera and Gallery
 *
 * @param onImageSelected Callback with compressed image path (WebP in cache)
 * @return Lambda to invoke the image picker (shows bottom sheet)
 */
@Composable
fun rememberImagePicker(
    onImageSelected: (String) -> Unit
): () -> Unit {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Bottom sheet visibility
    var showBottomSheet by remember { mutableStateOf(false) }

    // Loading state
    var isLoading by remember { mutableStateOf(false) }

    // Camera photo file and URI
    var cameraImageFile by remember { mutableStateOf<File?>(null) }
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }

    // Gallery launcher
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

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        // Capture the file reference before cleaning up state
        val fileToCompress = cameraImageFile
        val filePath = fileToCompress?.absolutePath

        // Clean up state immediately
        cameraImageFile = null
        cameraImageUri = null

        // Process the captured photo
        if (success && filePath != null && fileToCompress?.exists() == true) {
            scope.launch {
                isLoading = true
                try {
                    val compressed = compressImageToWebPInCache(
                        context,
                        filePath
                    )
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

    // Camera permission handler
    val requestCameraPermission = rememberCameraPermissionRequest(
        onPermissionGranted = {
            // Permission granted, launch camera
            try {
                val photoFile = File.createTempFile(
                    "camera_photo_${System.currentTimeMillis()}",
                    ".jpg",
                    context.cacheDir
                )

                val photoUri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    photoFile
                )

                cameraImageFile = photoFile
                cameraImageUri = photoUri

                cameraLauncher.launch(photoUri)
            } catch (e: Exception) {
                e.printStackTrace()
                // Failed to create temp file, silently fail
            }
        },
        onPermissionDenied = {
            // Permission denied, do nothing (silent fail)
        }
    )

    // Handle camera selection
    val onCameraSelected = {
        if (context.hasCameraPermission()) {
            // Already have permission, launch camera directly
            try {
                val photoFile = File.createTempFile(
                    "camera_photo_${System.currentTimeMillis()}",
                    ".jpg",
                    context.cacheDir
                )

                val photoUri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    photoFile
                )

                cameraImageFile = photoFile
                cameraImageUri = photoUri

                cameraLauncher.launch(photoUri)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            // Request permission first
            requestCameraPermission()
        }
    }

    // Handle gallery selection
    val onGallerySelected = {
        galleryLauncher.launch("image/*")
    }

    // Show bottom sheet
    if (showBottomSheet) {
        MediaPickerBottomSheet(
            mode = MediaPickerMode.IMAGE_ONLY,
            onCameraPhotoSelect = onCameraSelected,
            onGallerySelect = onGallerySelected,
            onDismiss = { showBottomSheet = false }
        )
    }

    // Show loading dialog
    if (isLoading) {
        LoadingDialog(message = stringResource(R.string.compressing_image))
    }

    // Return lambda that opens bottom sheet
    return remember {
        { showBottomSheet = true }
    }
}
