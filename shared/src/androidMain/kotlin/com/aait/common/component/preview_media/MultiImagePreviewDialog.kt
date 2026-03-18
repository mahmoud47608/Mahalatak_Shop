package com.aait.common.component.preview_media

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.aait.common.component.image.ZoomableImageView
import com.aait.domain.entity.general.MediaItemModel
import com.aait.domain.entity.general.MediaType
import com.aait.ui.theme.colorWhite

/**
 * Focused image preview dialog for a single image.
 */
@Composable
fun SingleImagePreviewDialog(
    imageUrl: String,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            ZoomableImageView(
                imageUrl = imageUrl,
                modifier = Modifier.fillMaxSize(),
                onToggleControls = { /* Single view might not need separate controls toggling */ },
                onShowControls = { },
                onHideControls = { }
            )

            // Close button
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close",
                    tint = colorWhite(),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

/**
 * Image preview dialog with swipe support for multiple images
 * @param images List of image URLs to display
 * @param initialPage Initial page to display (default: 0)
 * @param onDismiss Callback when dialog is dismissed
 */
@Composable
fun MultiImagePreviewDialog(
    images: List<String>,
    initialPage: Int = 0,
    onDismiss: () -> Unit
) {
    MediaPreviewDialog(
        mediaItems = images.map { MediaItemModel(url = it, type = MediaType.IMAGE) },
        initialPage = initialPage,
        onDismiss = onDismiss
    )
}

/**
 * Standalone component that shows a customClickable image thumbnail to preview
 */


// ZoomableImage is now redundant due to ZoomableImageView in MediaPreviewDialog.kt

