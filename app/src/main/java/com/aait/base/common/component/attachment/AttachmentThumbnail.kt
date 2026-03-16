package com.aait.base.common.component.attachment

import androidx.compose.foundation.background
import com.aait.base.common.component.utilis.noRippleClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.aait.base.common.component.image.DefaultImage
import com.aait.base.ui.theme.ColorLightTokens
import com.aait.base.ui.theme.CornerDimensions

/**
 * Individual attachment thumbnail component
 * Displays either an image or a PDF icon with optional delete overlay
 *
 * @param modifier Modifier for the container
 * @param url The attachment URL
 * @param size The thumbnail size (width and height)
 * @param attachmentType Type of attachment (IMAGE or PDF)
 * @param editMode Whether to show the delete icon
 * @param onClick Callback when thumbnail is clicked (for preview)
 * @param onDelete Callback when delete icon is clicked (only shown in edit mode)
 */
@Composable
internal fun AttachmentThumbnail(
    modifier: Modifier = Modifier,
    url: String,
    size: Dp,
    attachmentType: AttachmentType,
    editMode: Boolean,
    onClick: () -> Unit,
    onDelete: (() -> Unit)?
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(CornerDimensions.medium))
            .background(MaterialTheme.colorScheme.surface)
            .noRippleClickable { onClick() }
    ) {
        // Content: Image, PDF icon, or Video thumbnail
        when (attachmentType) {
            AttachmentType.IMAGE -> {
                DefaultImage(
                    url = url,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    shape = RoundedCornerShape(CornerDimensions.medium),
                    showLoadingState = true,
                    showErrorState = true,
                    retryEnabled = false // No retry button in thumbnails
                )
            }

            AttachmentType.PDF, AttachmentType.FILE -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF9F9F9)), // Light gray background
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PictureAsPdf,
                        contentDescription = "PDF file",
                        modifier = Modifier.size(48.dp),
                        tint = ColorLightTokens.Error // Red PDF icon
                    )
                }
            }

            AttachmentType.VIDEO -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF1A1A1A)) // Dark background for video
                ) {
                    // Play icon overlay
                    Icon(
                        imageVector = Icons.Default.PlayCircle,
                        contentDescription = "Video file",
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.Center),
                        tint = Color.White
                    )
                }
            }
        }

        // Delete icon overlay (only in edit mode)
        if (editMode && onDelete != null) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove attachment",
                modifier = Modifier
                    .align(Alignment.TopStart) // Auto-mirrors to top-right in LTR, top-left in RTL
                    .padding(4.dp)
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(ColorLightTokens.Error)
                    .noRippleClickable { onDelete() }
                    .padding(2.dp),
                tint = Color.White
            )
        }
    }
}
