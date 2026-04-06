package com.mahalatk.common.component.loading

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
 * Creates a glassmorphism brush — frosted tinted base with an animated light sweep.
 */
@Composable
fun shimmerBrush(): Brush {
    val transition = rememberInfiniteTransition()
    val sweepAnim by transition.animateFloat(
        initialValue = -500f,
        targetValue = 1500f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart,
        ),
    )

    val glass = if (AppColor.isDark) AppColor.Primary.copy(alpha = 0.08f)
    else AppColor.Primary.copy(alpha = 0.06f)

    val highlight = if (AppColor.isDark) Color.White.copy(alpha = 0.10f)
    else Color.White.copy(alpha = 0.35f)

    return Brush.linearGradient(
        colors = listOf(glass, highlight, glass),
        start = Offset(sweepAnim, sweepAnim * 0.3f),
        end = Offset(sweepAnim + 400f, sweepAnim * 0.3f + 200f),
    )
}

/**
 * A glassmorphism-styled rounded rectangle placeholder.
 */
@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier,
    width: Dp? = null,
    height: Dp = 16.dp,
    shape: Shape = RoundedCornerShape(8.dp),
) {
    val sweep = shimmerBrush()

    val glassBg = Brush.horizontalGradient(
        listOf(
            AppColor.Primary.copy(alpha = if (AppColor.isDark) 0.05f else 0.04f),
            AppColor.Primary.copy(alpha = if (AppColor.isDark) 0.10f else 0.08f),
        ),
    )

    Brush.linearGradient(
        listOf(
            AppColor.Primary.copy(alpha = 0.20f),
            AppColor.Primary.copy(alpha = 0.05f),
        ),
    )

    val sizeModifier = if (width != null) modifier.width(width).height(height)
    else modifier.fillMaxWidth().height(height)

    Box(
        modifier = sizeModifier
            .clip(shape)
            .background(glassBg)
            .background(sweep),
    )
}

/**
 * A glassmorphism-styled circular placeholder (for avatars).
 */
@Composable
fun ShimmerCircle(
    size: Dp = 48.dp,
    modifier: Modifier = Modifier,
) {
    val sweep = shimmerBrush()

    val glassBg = Brush.horizontalGradient(
        listOf(
            AppColor.Primary.copy(alpha = if (AppColor.isDark) 0.05f else 0.04f),
            AppColor.Primary.copy(alpha = if (AppColor.isDark) 0.10f else 0.08f),
        ),
    )

    Brush.linearGradient(
        listOf(
            AppColor.Primary.copy(alpha = 0.20f),
            AppColor.Primary.copy(alpha = 0.05f),
        ),
    )

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(glassBg)
            .background(sweep),
    )
}
