package com.mahalatk.common.component.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

/**
 * Wraps content with a staggered fade-in + slide-up animation.
 * Each item animates based on its [index] with a slight delay.
 */
@Composable
fun AnimatedListItem(
    index: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val animatable = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animatable.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 400,
                delayMillis = (index * 60).coerceAtMost(300),
                easing = FastOutSlowInEasing,
            ),
        )
    }

    Box(
        modifier = modifier.graphicsLayer {
            alpha = animatable.value
            translationY = (1f - animatable.value) * 40f
        },
    ) {
        content()
    }
}
