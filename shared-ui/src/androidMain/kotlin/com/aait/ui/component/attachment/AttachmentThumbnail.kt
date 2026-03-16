package com.aait.ui.component.attachment

import androidx.compose.foundation.background
import com.aait.ui.component.utilis.noRippleClickable
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
import com.aait.ui.component.image.DefaultImage
import com.aait.ui.theme.ColorLightTokens
import com.aait.ui.theme.CornerDimensions

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
        when (attachmentType) {
            AttachmentType.IMAGE -> {
                DefaultImage(
                    url = url,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    shape = RoundedCornerShape(CornerDimensions.medium),
                    showLoadingState = true,
                    showErrorState = true,
                    retryEnabled = false
                )
            }

            AttachmentType.PDF, AttachmentType.FILE -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF9F9F9)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PictureAsPdf,
                        contentDescription = "PDF file",
                        modifier = Modifier.size(48.dp),
                        tint = ColorLightTokens.Error
                    )
                }
            }

            AttachmentType.VIDEO -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF1A1A1A))
                ) {
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

        if (editMode && onDelete != null) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove attachment",
                modifier = Modifier
                    .align(Alignment.TopStart)
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
