package com.mahalatk.common.component.header

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.MahalatkTheme

/**
 * Reusable screen header with glass-morphism styling.
 * Used across Products, Orders, Chat, and detail screens.
 */
@Composable
fun ScreenHeader(
    title: String,
    modifier: Modifier = Modifier,
    height: Dp = 90.dp,
    onBackClick: (() -> Unit)? = null,
) {
    val glassColors = AppColor.HeaderGradient

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .drawBehind {
                drawRect(Brush.verticalGradient(glassColors))
                // Glass orbs for depth
                drawCircle(
                    Color.White.copy(alpha = 0.07f),
                    80.dp.toPx(),
                    Offset(-20.dp.toPx(), -10.dp.toPx()),
                )
                drawCircle(
                    Color.White.copy(alpha = 0.05f),
                    55.dp.toPx(),
                    Offset(size.width + 10.dp.toPx(), size.height * 0.4f),
                )
                drawCircle(
                    Color.White.copy(alpha = 0.03f),
                    35.dp.toPx(),
                    Offset(size.width * 0.5f, -15.dp.toPx()),
                )
                drawCircle(
                    Color.White.copy(alpha = 0.04f),
                    45.dp.toPx(),
                    Offset(size.width * 0.3f, size.height * 0.8f),
                )
                drawCircle(
                    Color.White.copy(alpha = 0.06f),
                    25.dp.toPx(),
                    Offset(size.width * 0.75f, size.height * 0.2f),
                )
                drawCircle(
                    Color.White.copy(alpha = 0.03f),
                    60.dp.toPx(),
                    Offset(size.width * 0.15f, size.height * 0.6f),
                )
                // Subtle accent line at bottom
                drawLine(
                    color = Color.White.copy(alpha = 0.12f),
                    start = Offset(size.width * 0.1f, size.height - 1.dp.toPx()),
                    end = Offset(size.width * 0.9f, size.height - 1.dp.toPx()),
                    strokeWidth = 0.5.dp.toPx(),
                )
            },
        contentAlignment = Alignment.BottomCenter,
    ) {
        Text(
            text = title,
            style = MahalatkTheme.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 14.dp),
        )

        if (onBackClick != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 10.dp)
                    .size(34.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.25f),
                        shape = CircleShape,
                    )
                    .then(Modifier.padding(0.dp)),
                contentAlignment = Alignment.Center,
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(34.dp),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp),
                    )
                }
            }
        }
    }
}
