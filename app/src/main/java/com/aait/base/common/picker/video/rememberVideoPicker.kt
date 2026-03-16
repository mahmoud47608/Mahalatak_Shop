package com.aait.alkarashi.common.componant.image

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
import com.aait.base.util.files.CompressUtil.compressToString
import com.aait.base.util.files.hasCameraPermission
import com.aait.base.util.files.rememberCameraPermissionRequest
import com.aait.domain.entity.general.MediaItemModel
import kotlinx.coroutines.launch
import java.io.File


/**
 * Video picker that allows user to choose between Camera and Gallery
 *
 * @param onVideoSelected Callback with video file path
 * @param currentVideoPath Currently selected video path (optional) - shows preview option if provided
 * @return Lambda to invoke the video picker (shows bottom sheet)
 */
@Composable
fun rememberVideoPicker(
    onVideoSelected: (String) -> Unit,
    currentVideoPath: String? = null
): () -> Unit {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Bottom sheet visibility
    var showBottomSheet by remember { mutableStateOf(false) }

    // Preview state for current video
    var showPreview by remember { mutableStateOf(false) }

    // Loading state
    var isLoading by remember { mutableStateOf(false) }

    // Camera video file and URI
    var cameraVideoFile by remember { mutableStateOf<File?>(null) }
    var cameraVideoUri by remember { mutableStateOf<Uri?>(null) }

    // Gallery launcher for video using PickVisualMedia
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            scope.launch {
                isLoading = true
                try {
                    val compressed = uri.compressToString(context)
                    onVideoSelected(compressed ?: "")
                } finally {
                    isLoading = false
                }
            }
        }
    }

    // Camera video launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CaptureVideo()
    ) { success ->
        val fileToSave = cameraVideoFile
        val filePath = fileToSave?.absolutePath

        // Clean up state
        cameraVideoFile = null
        cameraVideoUri = null

        // Process the captured video
        if (success && filePath != null && fileToSave?.exists() == true) {
            scope.launch {
                isLoading = true
                try {
                    val compressed = CompressUtil.compressVideoToCache(context, filePath)
                    onVideoSelected(compressed ?: filePath)
                } catch (e: Exception) {
                    e.printStackTrace()
                    onVideoSelected(filePath)
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

                cameraVideoFile = videoFile
                cameraVideoUri = videoUri

                cameraLauncher.launch(videoUri)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        },
        onPermissionDenied = {
            // Permission denied, do nothing
        }
    )

    // Handle camera selection
    val onCameraSelected = {
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

                cameraVideoFile = videoFile
                cameraVideoUri = videoUri

                cameraLauncher.launch(videoUri)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            requestCameraPermission()
        }
    }

    // Handle gallery selection
    val onGallerySelected = {
        galleryLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
        )
    }

    // Show bottom sheet
    if (showBottomSheet) {
        MediaPickerBottomSheet(
            mode = MediaPickerMode.VIDEO_ONLY,
            onCameraVideoSelect = onCameraSelected,
            onGallerySelect = onGallerySelected,
            onDismiss = { showBottomSheet = false },
            currentFilePath = currentVideoPath,
            onPreviewClick = if (currentVideoPath != null) {
                { showPreview = true }
            } else null
        )
    }

    // Show preview dialog for current video
    if (showPreview && currentVideoPath != null) {
        MediaPreviewDialog(
            mediaItems = listOf(MediaItemModel.fromUrl(currentVideoPath)),
            initialPage = 0,
            onDismiss = { showPreview = false }
        )
    }

    // Show loading dialog
    if (isLoading) {
        LoadingDialog(message = stringResource(R.string.compressing_video))
    }

    // Return lambda that opens bottom sheet
    return remember {
        { showBottomSheet = true }
    }
}
