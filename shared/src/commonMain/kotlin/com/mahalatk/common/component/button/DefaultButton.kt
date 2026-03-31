package com.mahalatk.common.component.button

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahalatk.theme.AppPadding
import com.mahalatk.theme.AppShapes
import com.mahalatk.theme.MahalatkTheme

enum class ButtonStyle {
    PRIMARY,
    ERROR,
    SECONDARY,
    OUTLINED
}

@Composable
fun DefaultButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: ButtonStyle = ButtonStyle.PRIMARY,
    shape: Shape = AppShapes.Medium,
    contentPadding: PaddingValues = AppPadding.ButtonContent,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh,
        ),
    )

    val containerColor = when (style) {
        ButtonStyle.PRIMARY -> MahalatkTheme.primary
        ButtonStyle.ERROR -> MahalatkTheme.error
        ButtonStyle.SECONDARY -> MahalatkTheme.black
        ButtonStyle.OUTLINED -> MaterialTheme.colorScheme.surface
    }

    val contentColor = when (style) {
        ButtonStyle.PRIMARY -> MahalatkTheme.white
        ButtonStyle.ERROR -> MaterialTheme.colorScheme.onError
        ButtonStyle.SECONDARY -> MahalatkTheme.white
        ButtonStyle.OUTLINED -> MahalatkTheme.primary
    }

    if (style == ButtonStyle.OUTLINED) {
        OutlinedButton(
            onClick = onClick,
            interactionSource = interactionSource,
            modifier = modifier
                .graphicsLayer { scaleX = scale; scaleY = scale }
                .fillMaxWidth(),
            shape = shape,
            enabled = enabled,
            border = BorderStroke(
                width = 1.dp,
                color = if (enabled) MahalatkTheme.primary else MahalatkTheme.border
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = containerColor,
                contentColor = contentColor,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(contentPadding),
                style = MahalatkTheme.labelMedium.copy(fontSize = 14.sp),
                color = if (enabled) contentColor else MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.38f
                )
            )
        }
    } else {
        Button(
            onClick = onClick,
            interactionSource = interactionSource,
            modifier = modifier
                .graphicsLayer { scaleX = scale; scaleY = scale }
                .fillMaxWidth(),
            shape = shape,
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = contentColor,
                disabledContainerColor = MahalatkTheme.hint,
                disabledContentColor = MahalatkTheme.white.copy(alpha = 0.7f)
            )
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(contentPadding),
                style = MahalatkTheme.labelMedium.copy(fontSize = 14.sp),
            )
        }
    }
}
