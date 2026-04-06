package com.mahalatk.common.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mahalatk.theme.AppColor

/**
 * Reusable glassmorphism-styled icon container with gradient background and border.
 * Standardizes the icon box pattern used across MenuItemRow, MoreScreen, ToggleCard, etc.
 */
@Composable
fun GlassIconBox(
    modifier: Modifier = Modifier,
    size: Dp = 36.dp,
    accentColor: Color = AppColor.Primary,
    cornerRadius: Dp = 12.dp,
    content: @Composable BoxScope.() -> Unit,
) {
    val shape = RoundedCornerShape(cornerRadius)
    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(
                brush = Brush.linearGradient(
                    listOf(
                        accentColor.copy(alpha = 0.06f),
                        accentColor.copy(alpha = 0.14f),
                    )
                )
            )
            .border(0.5.dp, accentColor.copy(alpha = 0.12f), shape),
        contentAlignment = Alignment.Center,
        content = content,
    )
}
