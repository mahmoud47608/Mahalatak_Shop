package com.mahalatk.common.component.utilis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mahalatk.theme.AppColor

/**
 * Reusable gradient divider matching the glassmorphism design language.
 * Fades from transparent → color → transparent for a soft glass separator effect.
 */
@Composable
fun GradientDivider(
    modifier: Modifier = Modifier,
    color: Color = AppColor.Primary,
    height: Dp = 0.5.dp,
    horizontalPadding: Dp = 12.dp,
) {
    Box(
        modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding)
            .height(height)
            .background(
                brush = Brush.horizontalGradient(
                    listOf(
                        color.copy(alpha = 0.0f),
                        color.copy(alpha = if (AppColor.isDark) 0.08f else 0.12f),
                        color.copy(alpha = 0.0f),
                    )
                )
            )
    )
}
