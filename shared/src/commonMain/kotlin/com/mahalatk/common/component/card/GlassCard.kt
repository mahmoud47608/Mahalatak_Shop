package com.mahalatk.common.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mahalatk.theme.AppColor

/**
 * A reusable glass-morphism styled card.
 *
 * @param accentColor  The tint used for gradient background & border.
 *                     Pass [Color.Unspecified] to use a neutral surface style.
 * @param cornerRadius Corner radius of the card shape.
 * @param contentPadding Inner padding of the card content.
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    accentColor: Color = Color.Unspecified,
    cornerRadius: Dp = 20.dp,
    contentPadding: Dp = 16.dp,
    content: @Composable BoxScope.() -> Unit,
) {
    val shape = RoundedCornerShape(cornerRadius)
    val tint = if (accentColor == Color.Unspecified) AppColor.Primary else accentColor

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        tint.copy(alpha = 0.05f),
                        tint.copy(alpha = 0.10f),
                    ),
                ),
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        tint.copy(alpha = 0.25f),
                        tint.copy(alpha = 0.06f),
                    ),
                ),
                shape = shape,
            )
            .padding(contentPadding),
        content = content,
    )
}
