package com.mahalatk.common.component.loading

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mahalatk.theme.AppColor

/**
 * Creates a shimmer brush that animates a gradient sweep left→right.
 */
@Composable
fun shimmerBrush(): Brush {
    val transition = rememberInfiniteTransition()
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
    )

    val base = if (AppColor.isDark) Color(0xFF2A2A2A) else Color(0xFFE8E8E8)
    val highlight = if (AppColor.isDark) Color(0xFF3A3A3A) else Color(0xFFF5F5F5)

    return Brush.linearGradient(
        colors = listOf(base, highlight, base),
        start = Offset(translateAnim - 500f, 0f),
        end = Offset(translateAnim, 0f),
    )
}

/**
 * A rounded rectangle placeholder with shimmer animation.
 */
@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier,
    width: Dp? = null,
    height: Dp = 16.dp,
    shape: Shape = RoundedCornerShape(8.dp),
) {
    val brush = shimmerBrush()
    val mod = if (width != null) {
        modifier.width(width).height(height)
    } else {
        modifier.fillMaxWidth().height(height)
    }
    Spacer(
        modifier = mod
            .clip(shape)
            .background(brush),
    )
}

/**
 * A circular placeholder with shimmer animation (for avatars).
 */
@Composable
fun ShimmerCircle(
    size: Dp = 48.dp,
    modifier: Modifier = Modifier,
) {
    val brush = shimmerBrush()
    Spacer(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(brush),
    )
}
