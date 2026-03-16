package com.aait.ui.picker.media

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
import com.aait.ui.util.files.CompressUtil.compressImageToWebPInCache
import com.aait.ui.util.files.CompressUtil.compressToString
import com.aait.ui.util.files.hasCameraPermission
import com.aait.ui.util.files.rememberCameraPermissionRequest
import com.aait.domain.entity.general.MediaItemModel
import com.mahalatak.shared.ui.R
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun rememberMediaPicker(
    onMediaSelected: (String) -> Unit,
    allowVideo: Boolean = true,
    currentMediaPath: String? = null
): () -> Unit {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var showBottomSheet by remember { mutableStateOf(false) }
    var showPreview by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var cameraFile by remember { mutableStateOf<File?>(null) }
    var cameraUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            scope.launch {
                isLoading = true
                try {
                    val compressed = uri.compressToString(context)
                    onMediaSelected(compressed ?: "")
                } finally {
                    isLoading = false
                }
            }
        }
    }

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
                    val compressed = compressImageToWebPInCache(context, filePath)
                    if (!compressed.isNullOrEmpty()) onMediaSelected(compressed)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    isLoading = false
                }
            }
        }
    }

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
                    onMediaSelected(compressed ?: filePath)
                } catch (e: Exception) {
                    e.printStackTrace()
                    onMediaSelected(filePath)
                } finally {
                    isLoading = false
                }
            }
        }
    }

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

    val onGallerySelected = {
        val mediaType = if (allowVideo) {
            ActivityResultContracts.PickVisualMedia.ImageAndVideo
        } else {
            ActivityResultContracts.PickVisualMedia.ImageOnly
        }
        galleryLauncher.launch(PickVisualMediaRequest(mediaType))
    }

    if (showBottomSheet) {
        MediaPickerBottomSheet(
            mode = if (allowVideo) MediaPickerMode.IMAGE_AND_VIDEO else MediaPickerMode.IMAGE_ONLY,
            onCameraPhotoSelect = onCameraPhotoSelected,
            onCameraVideoSelect = if (allowVideo) onCameraVideoSelected else null,
            onGallerySelect = onGallerySelected,
            onDismiss = { showBottomSheet = false },
            currentFilePath = currentMediaPath,
            onPreviewClick = if (currentMediaPath != null) {
                { showPreview = true }
            } else null
        )
    }

    if (showPreview && currentMediaPath != null) {
        MediaPreviewDialog(
            mediaItems = listOf(MediaItemModel.fromUrl(currentMediaPath)),
            initialPage = 0,
            onDismiss = { showPreview = false }
        )
    }

    if (isLoading) {
        LoadingDialog(message = stringResource(R.string.compressing_media))
    }

    return remember { { showBottomSheet = true } }
}
