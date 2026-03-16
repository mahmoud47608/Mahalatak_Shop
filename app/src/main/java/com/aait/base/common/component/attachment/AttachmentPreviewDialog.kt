package com.aait.base.common.component.attachment

import androidx.compose.foundation.background
import com.aait.base.common.component.utilis.noRippleClickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage

/**
 * Attachment preview dialog dispatcher
 * Shows different preview dialogs based on attachment type
 *
 * @param url The attachment URL to preview
 * @param attachmentType Type of attachment (IMAGE, PDF, or VIDEO)
 * @param onDismiss Callback when dialog is dismissed

@Composable
fun AttachmentPreviewDialog(
    url: String,
    attachmentType: AttachmentType,
    onDismiss: () -> Unit
) {
    when (attachmentType) {
        AttachmentType.IMAGE -> ImagePreviewDialog(url, onDismiss)
//        AttachmentType.PDF -> PdfPreviewDialog(url, onDismiss)
//        AttachmentType.VIDEO -> VideoPreviewDialog(url, onDismiss)
    }
}
*/
/**
 * Image preview dialog with zoom and pan gestures
 *
 * @param url The image URL to preview
 * @param onDismiss Callback when dialog is dismissed
 */
@Composable
private fun ImagePreviewDialog(
    url: String,
    onDismiss: () -> Unit
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false // Full screen
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.9f))
        ) {
            // Zoomable and pannable image
            AsyncImage(
                model = url,
                contentDescription = "Attachment preview",
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offsetX,
                        translationY = offsetY
                    )
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            // Apply zoom with constraints (1x to 5x)
                            scale = (scale * zoom).coerceIn(1f, 5f)

                            // Allow panning only when zoomed in
                            if (scale > 1f) {
                                offsetX += pan.x
                                offsetY += pan.y
                            } else {
                                // Reset offset when zoomed out
                                offsetX = 0f
                                offsetY = 0f
                            }
                        }
                    },
                contentScale = ContentScale.Fit
            )

            // Close button (top-right)
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close preview",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f))
                    .noRippleClickable { onDismiss() }
                    .padding(6.dp),
                tint = Color.White
            )
        }
    }
}


