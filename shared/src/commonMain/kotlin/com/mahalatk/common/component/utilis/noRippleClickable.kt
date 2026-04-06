package com.mahalatk.common.component.utilis

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import kotlin.time.ComparableTimeMark
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeSource

@Composable
fun Modifier.noRippleClickable(
    enabled: Boolean = true,
    debounceMillis: Long = 500L,
    onClick: () -> Unit
): Modifier {
    val lastClickMark = remember { mutableStateOf<ComparableTimeMark?>(null) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh,
        ),
        label = "pressScale",
    )

    return this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            indication = null,
            interactionSource = interactionSource,
            enabled = enabled
        ) {
            val now = TimeSource.Monotonic.markNow()
            val last = lastClickMark.value
            if (last == null || now - last >= debounceMillis.milliseconds) {
                lastClickMark.value = now
                onClick()
            }
        }
}
