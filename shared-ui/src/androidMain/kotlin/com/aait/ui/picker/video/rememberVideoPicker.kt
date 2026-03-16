package com.aait.ui.picker.video

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
import com.aait.ui.component.dialog.LoadingDialog
import com.aait.ui.component.preview_media.MediaPreviewDialog
import com.aait.ui.component.sheet.MediaPickerBottomSheet
import com.aait.ui.component.sheet.MediaPickerMode
import com.aait.ui.util.files.CompressUtil
import com.aait.ui.util.files.CompressUtil.compressToString
import com.aait.ui.util.files.hasCameraPermission
import com.aait.ui.util.files.rememberCameraPermissionRequest
import com.aait.domain.entity.general.MediaItemModel
import com.mahalatak.shared.ui.R
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun rememberVideoPicker(
    onVideoSelected: (String) -> Unit,
    currentVideoPath: String? = null
): () -> Unit {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var showBottomSheet by remember { mutableStateOf(false) }
    var showPreview by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var cameraVideoFile by remember { mutableStateOf<File?>(null) }
    var cameraVideoUri by remember { mutableStateOf<Uri?>(null) }

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

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CaptureVideo()
    ) { success ->
        val fileToSave = cameraVideoFile
        val filePath = fileToSave?.absolutePath
        cameraVideoFile = null
        cameraVideoUri = null

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

    val requestCameraPermission = rememberCameraPermissionRequest(
        onPermissionGranted = {
            try {
                val videoFile = File.createTempFile(
                    "camera_video_${System.currentTimeMillis()}", ".mp4", context.cacheDir
                )
                val videoUri = FileProvider.getUriForFile(
                    context, "${context.packageName}.fileprovider", videoFile
                )
                cameraVideoFile = videoFile
                cameraVideoUri = videoUri
                cameraLauncher.launch(videoUri)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        },
        onPermissionDenied = {}
    )

    val onCameraSelected = {
        if (context.hasCameraPermission()) {
            try {
                val videoFile = File.createTempFile(
                    "camera_video_${System.currentTimeMillis()}", ".mp4", context.cacheDir
                )
                val videoUri = FileProvider.getUriForFile(
                    context, "${context.packageName}.fileprovider", videoFile
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

    val onGallerySelected = {
        galleryLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
        )
    }

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

    if (showPreview && currentVideoPath != null) {
        MediaPreviewDialog(
            mediaItems = listOf(MediaItemModel.fromUrl(currentVideoPath)),
            initialPage = 0,
            onDismiss = { showPreview = false }
        )
    }

    if (isLoading) {
        LoadingDialog(message = stringResource(R.string.compressing_video))
    }

    return remember { { showBottomSheet = true } }
}
