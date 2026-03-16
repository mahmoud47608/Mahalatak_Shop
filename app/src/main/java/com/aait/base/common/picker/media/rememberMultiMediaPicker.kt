package com.aait.base.ui.component.picker.media

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
import androidx.compose.ui.res.stringResource
import androidx.core.content.FileProvider
import com.aait.base.R
import com.aait.base.common.component.dialog.LoadingDialog
import com.aait.base.common.component.preview_media.MediaPreviewDialog
import com.aait.base.common.component.sheet.MediaPickerBottomSheet
import com.aait.base.common.component.sheet.MediaPickerMode
import com.aait.base.util.files.CompressUtil
import com.aait.base.util.files.CompressUtil.compressImageToWebPInCache
import com.aait.base.util.files.CompressUtil.compressToString
import com.aait.base.util.files.hasCameraPermission
import com.aait.base.util.files.rememberCameraPermissionRequest
import com.aait.domain.entity.general.MediaItemModel
import kotlinx.coroutines.launch
import java.io.File

/**
 * Multi-media picker that allows user to choose multiple images/videos from camera or gallery
 *
 * @param onMediaSelected Callback with list of media file paths
 * @param allowVideo Whether to allow video capture/selection (default: true)
 * @param currentMediaPaths Currently selected media paths (optional) - shows preview option if provided
 * @return Lambda to invoke the media picker (shows bottom sheet)
 */
@Composable
fun rememberMultiMediaPicker(
    onMediaSelected: (List<String>) -> Unit,
    allowVideo: Boolean = true,
    currentMediaPaths: List<String>? = null
): () -> Unit {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Bottom sheet visibility
    var showBottomSheet by remember { mutableStateOf(false) }

    // Preview state for current media
    var showPreview by remember { mutableStateOf(false) }

    // Loading state
    var isLoading by remember { mutableStateOf(false) }

    // Camera photo/video file and URI
    var cameraFile by remember { mutableStateOf<File?>(null) }
    var cameraUri by remember { mutableStateOf<Uri?>(null) }

    // Gallery launcher for multiple images and videos using PickMultipleVisualMedia
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia()
    ) { uris ->
        if (uris.isNotEmpty()) {
            scope.launch {
                isLoading = true
                try {
                    val compressedPaths = mutableListOf<String>()
                    uris.forEach { uri ->
                        val compressed = uri.compressToString(context)
                        if (!compressed.isNullOrEmpty()) {
                            compressedPaths.add(compressed)
                        }
                    }
                    if (compressedPaths.isNotEmpty()) {
                        onMediaSelected(compressedPaths)
                    }
                } finally {
                    isLoading = false
                }
            }
        }
    }

    // Camera photo launcher
    val cameraPhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        val fileToCompress = cameraFile
        val filePath = fileToCompress?.absolutePath

        cameraFile = null
        cameraUri = null

        if (success && filePath != null && fileToCompress?.exists() == true) {
            scope.launch {
                isLoading = true
                try {
                    val compressed = compressImageToWebPInCache(
                        context,
                        filePath
                    )
                    if (!compressed.isNullOrEmpty()) {
                        onMediaSelected(listOf(compressed))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    isLoading = false
                }
            }
        }
    }

    // Camera video launcher
    val cameraVideoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CaptureVideo()
    ) { success ->
        val fileToSave = cameraFile
        val filePath = fileToSave?.absolutePath

        cameraFile = null
        cameraUri = null

        if (success && filePath != null && fileToSave?.exists() == true) {
            scope.launch {
                isLoading = true
                try {
                    val compressed = CompressUtil.compressVideoToCache(context, filePath)
                    onMediaSelected(listOf(compressed ?: filePath))
                } catch (e: Exception) {
                    e.printStackTrace()
                    onMediaSelected(listOf(filePath))
                } finally {
                    isLoading = false
                }
            }
        }
    }

    // Camera permission handler for photo
    val requestCameraPhotoPermission = rememberCameraPermissionRequest(
        onPermissionGranted = {
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

                cameraFile = photoFile
                cameraUri = photoUri

                cameraPhotoLauncher.launch(photoUri)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        },
        onPermissionDenied = {}
    )

    // Camera permission handler for video
    val requestCameraVideoPermission = rememberCameraPermissionRequest(
        onPermissionGranted = {
            try {
                val videoFile = File.createTempFile(
                    "camera_video_${System.currentTimeMillis()}",
                    ".mp4",
                    context.cacheDir
                )

                val videoUri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    videoFile
                )

                cameraFile = videoFile
                cameraUri = videoUri

                cameraVideoLauncher.launch(videoUri)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        },
        onPermissionDenied = {}
    )

    // Handle camera photo selection
    val onCameraPhotoSelected = {
        if (context.hasCameraPermission()) {
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

                cameraFile = photoFile
                cameraUri = photoUri

                cameraPhotoLauncher.launch(photoUri)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            requestCameraPhotoPermission()
        }
    }

    // Handle camera video selection
    val onCameraVideoSelected = {
        if (context.hasCameraPermission()) {
            try {
                val videoFile = File.createTempFile(
                    "camera_video_${System.currentTimeMillis()}",
                    ".mp4",
                    context.cacheDir
                )

                val videoUri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    videoFile
                )

                cameraFile = videoFile
                cameraUri = videoUri

                cameraVideoLauncher.launch(videoUri)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            requestCameraVideoPermission()
        }
    }

    // Handle gallery selection (images and videos)
    val onGallerySelected = {
        val mediaType = if (allowVideo) {
            ActivityResultContracts.PickVisualMedia.ImageAndVideo
        } else {
            ActivityResultContracts.PickVisualMedia.ImageOnly
        }
        galleryLauncher.launch(PickVisualMediaRequest(mediaType))
    }

    // Show bottom sheet
    if (showBottomSheet) {
        MediaPickerBottomSheet(
            mode = if (allowVideo) MediaPickerMode.IMAGE_AND_VIDEO else MediaPickerMode.IMAGE_ONLY,
            onCameraPhotoSelect = onCameraPhotoSelected,
            onCameraVideoSelect = if (allowVideo) onCameraVideoSelected else null,
            onGallerySelect = onGallerySelected,
            onDismiss = { showBottomSheet = false },
            currentFilePath = currentMediaPaths?.firstOrNull(),
            onPreviewClick = if (!currentMediaPaths.isNullOrEmpty()) {
                { showPreview = true }
            } else null
        )
    }

    // Show preview dialog for current media
    if (showPreview && !currentMediaPaths.isNullOrEmpty()) {
        MediaPreviewDialog(
            mediaItems = MediaItemModel.fromUrls(currentMediaPaths),
            initialPage = 0,
            onDismiss = { showPreview = false }
        )
    }

    // Show loading dialog
    if (isLoading) {
        LoadingDialog(message = stringResource(R.string.compressing_media))
    }

    // Return lambda that opens bottom sheet
    return remember {
        { showBottomSheet = true }
    }
}
