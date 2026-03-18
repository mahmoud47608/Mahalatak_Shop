package com.aait.common.component.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aait.ui.theme.PaddingDimensions
import mahalatak.shared.generated.resources.*
import org.jetbrains.compose.resources.stringResource

/**
 * Media picker mode to determine which options to show
 */
enum class MediaPickerMode {
    IMAGE_ONLY,      // Camera photo + Gallery (images)
    VIDEO_ONLY,      // Camera video + Gallery (videos)
    IMAGE_AND_VIDEO  // Camera photo + Camera video + Gallery (all media)
}

/**
 * Unified bottom sheet for selecting media source
 * Supports image-only, video-only, or both image and video selection
 *
 * @param mode The picker mode (IMAGE_ONLY, VIDEO_ONLY, or IMAGE_AND_VIDEO)
 * @param onCameraPhotoSelect Callback for camera photo selection
 * @param onCameraVideoSelect Callback for camera video selection (optional, used for VIDEO_ONLY and IMAGE_AND_VIDEO modes)
 * @param onGallerySelect Callback for gallery selection
 * @param onDismiss Callback when sheet is dismissed
 * @param currentFilePath Current file path - if provided, shows preview option
 * @param onPreviewClick Callback for preview action - only shown if currentFilePath is not null
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaPickerBottomSheet(
    mode: MediaPickerMode,
    onCameraPhotoSelect: (() -> Unit)? = null,
    onCameraVideoSelect: (() -> Unit)? = null,
    onGallerySelect: () -> Unit,
    onDismiss: () -> Unit,
    currentFilePath: String? = null,
    onPreviewClick: (() -> Unit)? = null
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Define colors for different options
    val cameraPhotoColor = Color(0xFF4CAF50)  // Green
    val cameraVideoColor = Color(0xFFF44336)  // Red
    val galleryColor = Color(0xFF2196F3)      // Blue
    val previewColor = Color(0xFF9C27B0)      // Purple

    // Get title based on mode
    val title = when (mode) {
        MediaPickerMode.IMAGE_ONLY -> stringResource(Res.string.select_image_source)
        MediaPickerMode.VIDEO_ONLY -> stringResource(Res.string.select_video_source)
        MediaPickerMode.IMAGE_AND_VIDEO -> stringResource(Res.string.select_media_source)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            // Header with title and close button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = PaddingDimensions.high,
                        vertical = PaddingDimensions.medium
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = PaddingDimensions.high),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Spacer(modifier = Modifier.height(PaddingDimensions.medium))

            // Preview Option (only if current file exists)
            if (currentFilePath != null && onPreviewClick != null) {
                MediaPickerOption(
                    icon = Icons.Default.PlayCircle,
                    iconBackgroundColor = previewColor,
                    title = stringResource(Res.string.preview_current),
                    subtitle = "View current selection",
                    onClick = {
                        onPreviewClick()
                        onDismiss()
                    }
                )
            }

            // Camera Photo Option (for IMAGE_ONLY and IMAGE_AND_VIDEO modes)
            if (mode == MediaPickerMode.IMAGE_ONLY || mode == MediaPickerMode.IMAGE_AND_VIDEO) {
                onCameraPhotoSelect?.let { callback ->
                    MediaPickerOption(
                        icon = Icons.Default.CameraAlt,
                        iconBackgroundColor = cameraPhotoColor,
                        title = stringResource(Res.string.take_photo),
                        subtitle = "Capture a new photo",
                        onClick = {
                            callback()
                            onDismiss()
                        }
                    )
                }
            }

            // Camera Video Option (for VIDEO_ONLY and IMAGE_AND_VIDEO modes)
            if (mode == MediaPickerMode.VIDEO_ONLY || mode == MediaPickerMode.IMAGE_AND_VIDEO) {
                onCameraVideoSelect?.let { callback ->
                    MediaPickerOption(
                        icon = Icons.Default.Videocam,
                        iconBackgroundColor = cameraVideoColor,
                        title = stringResource(Res.string.record_video),
                        subtitle = "Record a new video",
                        onClick = {
                            callback()
                            onDismiss()
                        }
                    )
                }
            }

            // Gallery Option
            MediaPickerOption(
                icon = Icons.Outlined.PhotoLibrary,
                iconBackgroundColor = galleryColor,
                title = stringResource(Res.string.choose_from_gallery),
                subtitle = when (mode) {
                    MediaPickerMode.IMAGE_ONLY -> "Select from your photos"
                    MediaPickerMode.VIDEO_ONLY -> "Select from your videos"
                    MediaPickerMode.IMAGE_AND_VIDEO -> "Select from your library"
                },
                onClick = {
                    onGallerySelect()
                    onDismiss()
                }
            )
        }
    }
}

@Composable
private fun MediaPickerOption(
    icon: ImageVector,
    iconBackgroundColor: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = PaddingDimensions.high, vertical = PaddingDimensions.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon with colored background
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(iconBackgroundColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(24.dp),
                tint = iconBackgroundColor
            )
        }

        Spacer(modifier = Modifier.width(PaddingDimensions.medium))

        // Text content
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Backward compatibility functions

/**
 * Image source bottom sheet - selects between camera photo and gallery
 */
@Composable
fun ImageSourceBottomSheet(
    onCameraSelect: () -> Unit,
    onGallerySelect: () -> Unit,
    onDismiss: () -> Unit
) {
    MediaPickerBottomSheet(
        mode = MediaPickerMode.IMAGE_ONLY,
        onCameraPhotoSelect = onCameraSelect,
        onGallerySelect = onGallerySelect,
        onDismiss = onDismiss
    )
}

/**
 * Video source bottom sheet - selects between camera video and gallery
 */
@Composable
fun VideoSourceBottomSheet(
    onCameraSelect: () -> Unit,
    onGallerySelect: () -> Unit,
    onDismiss: () -> Unit,
    currentFilePath: String? = null,
    onPreviewClick: (() -> Unit)? = null
) {
    MediaPickerBottomSheet(
        mode = MediaPickerMode.VIDEO_ONLY,
        onCameraVideoSelect = onCameraSelect,
        onGallerySelect = onGallerySelect,
        onDismiss = onDismiss,
        currentFilePath = currentFilePath,
        onPreviewClick = onPreviewClick
    )
}

/**
 * Media source bottom sheet - selects between camera photo, camera video, and gallery
 */
@Composable
fun MediaSourceBottomSheet(
    onCameraPhotoSelect: () -> Unit,
    onCameraVideoSelect: (() -> Unit)? = null,
    onGallerySelect: () -> Unit,
    onDismiss: () -> Unit,
    currentFilePath: String? = null,
    onPreviewClick: (() -> Unit)? = null
) {
    MediaPickerBottomSheet(
        mode = if (onCameraVideoSelect != null) MediaPickerMode.IMAGE_AND_VIDEO else MediaPickerMode.IMAGE_ONLY,
        onCameraPhotoSelect = onCameraPhotoSelect,
        onCameraVideoSelect = onCameraVideoSelect,
        onGallerySelect = onGallerySelect,
        onDismiss = onDismiss,
        currentFilePath = currentFilePath,
        onPreviewClick = onPreviewClick
    )
}
