package com.aait.common.component.utilis

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun DashedBox(
    modifier: Modifier = Modifier,
    onClicked: () -> Unit,
    hasError: Boolean = false,
    content: @Composable () -> Unit
) {
    val borderColor =
        if (hasError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline

    Box(
        modifier = modifier
            .size(64.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Transparent)
            .drawBehind {
                val borderWidth = 3.dp.toPx()
                val cornerRadius = 8.dp.toPx()

                drawRoundRect(
                    color = borderColor,
                    style = Stroke(
                        width = borderWidth,
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(15f, 10f),
                            0f
                        )
                    ),
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                )
            }
            .clickable {
                onClicked()
            },
        contentAlignment = Alignment.Center
    ) {
        content.invoke()
    }
}