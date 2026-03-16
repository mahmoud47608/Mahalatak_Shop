package com.aait.base.ui.component.picker.video

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

@Composable
fun rememberMultiVideoPicker(
    onVideosSelected: (List<String>) -> Unit,
    currentVideoPaths: List<String> = emptyList()
): () -> Unit {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var showBottomSheet by remember { mutableStateOf(false) }
    var showPreview by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    var cameraVideoFile by remember { mutableStateOf<File?>(null) }
    var cameraVideoUri by remember { mutableStateOf<Uri?>(null) }

    /* -------------------- Gallery (MULTI) -------------------- */

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia()
    ) { uris ->
        if (uris.isNotEmpty()) {
            scope.launch {
                isLoading = true
                try {
                    val videos = uris.mapNotNull { uri ->
                        uri.compressToString(context)
                    }
                    if (videos.isNotEmpty()) {
                        onVideosSelected(videos)
                    }
                } finally {
                    isLoading = false
                }
            }
        }
    }

    /* -------------------- Camera (SINGLE) -------------------- */

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CaptureVideo()
    ) { success ->
        val file = cameraVideoFile
        cameraVideoFile = null
        cameraVideoUri = null

        if (success && file != null && file.exists()) {
            scope.launch {
                isLoading = true
                try {
                    val result = try {
                        CompressUtil.compressVideoToCache(context, file.absolutePath)
                    } catch (e: Exception) {
                        file.absolutePath
                    }
                    onVideosSelected(listOf(result ?: file.absolutePath))
                } finally {
                    isLoading = false
                }
            }
        }
    }

    /* -------------------- Camera Permission -------------------- */

    val requestCameraPermission = rememberCameraPermissionRequest(
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

                cameraVideoFile = videoFile
                cameraVideoUri = videoUri

                cameraLauncher.launch(videoUri)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        },
        onPermissionDenied = {}
    )

    /* -------------------- Actions -------------------- */

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

    val onGallerySelected = {
        galleryLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
        )
    }

    /* -------------------- Bottom Sheet -------------------- */

    if (showBottomSheet) {
        MediaPickerBottomSheet(
            mode = MediaPickerMode.VIDEO_ONLY,
            onCameraVideoSelect = onCameraSelected,
            onGallerySelect = onGallerySelected,
            onDismiss = { showBottomSheet = false },
            currentFilePath = currentVideoPaths.firstOrNull(),
            onPreviewClick = if (currentVideoPaths.isNotEmpty()) {
                { showPreview = true }
            } else null
        )
    }

    /* -------------------- Preview -------------------- */

    if (showPreview && currentVideoPaths.isNotEmpty()) {
        MediaPreviewDialog(
            mediaItems = currentVideoPaths.map { MediaItemModel.fromUrl(it) },
            initialPage = 0,
            onDismiss = { showPreview = false }
        )
    }

    /* -------------------- Loading -------------------- */

    if (isLoading) {
        LoadingDialog(message = stringResource(R.string.compressing_video))
    }

    /* -------------------- Open Picker -------------------- */

    return remember {
        { showBottomSheet = true }
    }
}
