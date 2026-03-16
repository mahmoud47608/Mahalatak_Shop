package com.aait.base.common.component.image

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.layout.ContentScale

/**
 * Zoomable image view for media dialogs
 */
@Composable
fun ZoomableImageView(
    imageUrl: String,
    onToggleControls: () -> Unit,
    onShowControls: () -> Unit,
    onHideControls: () -> Unit,
    modifier: Modifier = Modifier
) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    // Auto show/hide controls based on zoom level
    LaunchedEffect(scale) {
        if (scale > 1f) {
            onHideControls()
        } else {
            onShowControls()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RectangleShape) // Ensure content is clipped
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onToggleControls() },
                    onDoubleTap = {
                        if (scale > 1f) {
                            scale = 1f
                            offset = Offset.Zero
                        } else {
                            scale = 2.5f
                        }
                    }
                )
            }
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown()
                    do {
                        val event = awaitPointerEvent()
                        val zoom = event.calculateZoom()
                        val pan = event.calculatePan()

                        val isMultiTouch = event.changes.size > 1
                        val isZoomed = scale > 1f

                        // We only want to handle (consume) events if:
                        // 1. We are already zoomed in (to allow panning)
                        // 2. We are performing a multi-touch gesture (pinching to zoom)
                        if (isZoomed || isMultiTouch) {
                            val newScale = (scale * zoom).coerceIn(1f, 5f)

                            // Calculate bounds based on standard screen assumes
                            // In a real production app, we'd use onGloballyPositioned to get exact bounds,
                            // but for a dialog full screen, screen size is a safe proxy.
                            val maxX = (1000f * (newScale - 1f)).coerceAtLeast(0f)
                            val maxY = (1000f * (newScale - 1f)).coerceAtLeast(0f)

                            val newOffset = if (newScale > 1f) {
                                Offset(
                                    x = (offset.x + pan.x * newScale).coerceIn(-maxX, maxX),
                                    y = (offset.y + pan.y * newScale).coerceIn(-maxY, maxY)
                                )
                            } else {
                                Offset.Zero
                            }

                            scale = newScale
                            offset = newOffset

                            // Consume only if we are zoomed in or performing a multi-touch
                            event.changes.forEach {
                                if (it.positionChanged()) {
                                    it.consume()
                                }
                            }
                        }
                    } while (event.changes.any { it.pressed })
                }
            }
    ) {
        DefaultImage(
            url = imageUrl,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                ),
            contentScale = ContentScale.Fit
        )
    }
}
