package com.aait.base.common.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aait.base.common.component.text.DefaultText
import com.aait.base.ui.theme.BaseTheme
import com.aait.base.ui.theme.CornerDimensions
import com.aait.base.ui.theme.ExtendedTheme
import com.aait.base.ui.theme.PaddingDimensions

/**
 * Visual style for buttons
 */
enum class ButtonStyle {
    /** Primary brand button using theme primary color */
    PRIMARY,
    /** Error/destructive action button */
    ERROR,
    /** Success/confirmation button */
    SUCCESS,
    /** Secondary/alternative button */
    SECONDARY,
    /** Outlined button with white/surface background and primary stroke */
    OUTLINED
}

/**
 * Unified button component with configurable styling.
 *
 * This component consolidates all button variants (primary, error, success, secondary)
 * into a single composable function with a style parameter. Use this for all button
 * instances throughout the application for consistent styling.
 *
 * @param text The button label text
 * @param onClick Callback invoked when button is clicked
 * @param modifier Optional modifier for this component
 * @param enabled Whether the button is enabled and customClickable
 * @param style Visual style of the button (PRIMARY, ERROR, SUCCESS, SECONDARY)
 * @param shape Shape of the button (defaults to rounded corners)
 * @param contentPadding Internal padding for button content
 *
 * Example:
 * ```kotlin
 * DefaultButton(
 *     text = "Submit",
 *     onClick = { /* handle click */ },
 *     style = ButtonStyle.PRIMARY
 * )
 * ```
 */
@Composable
fun DefaultButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: ButtonStyle = ButtonStyle.PRIMARY,
    shape: Shape = RoundedCornerShape(CornerDimensions.high),
    contentPadding: PaddingValues = PaddingValues(vertical = PaddingDimensions.low)
) {
    val containerColor = when (style) {
        ButtonStyle.PRIMARY -> MaterialTheme.colorScheme.primary
        ButtonStyle.ERROR -> MaterialTheme.colorScheme.error
        ButtonStyle.SUCCESS -> ExtendedTheme.colors.success
        ButtonStyle.SECONDARY -> Color.Black
        ButtonStyle.OUTLINED -> MaterialTheme.colorScheme.surface
    }

    val contentColor = when (style) {
        ButtonStyle.PRIMARY -> MaterialTheme.colorScheme.onPrimary
        ButtonStyle.ERROR -> MaterialTheme.colorScheme.onError
        ButtonStyle.SUCCESS -> ExtendedTheme.colors.onSuccess
        ButtonStyle.SECONDARY -> Color.White
        ButtonStyle.OUTLINED -> MaterialTheme.colorScheme.primary
    }

    if (style == ButtonStyle.OUTLINED) {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier
                .padding(horizontal = PaddingDimensions.low)
                .fillMaxWidth(),
            shape = shape,
            enabled = enabled,
            border = BorderStroke(
                width = 1.dp,
                color = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = containerColor,
                contentColor = contentColor,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
        ) {
            DefaultText(
                text = text,
                modifier = Modifier.padding(contentPadding),
                textStyle = MaterialTheme.typography.labelMedium.copy(fontSize = 14.sp),
                textColor = if (enabled) contentColor else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
        }
    } else {
        Button(
            onClick = onClick,
            modifier = modifier
                .padding(horizontal = PaddingDimensions.low)
                .fillMaxWidth(),
            shape = shape,
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = contentColor
            )
        ) {
            DefaultText(
                text = text,
                modifier = Modifier.padding(contentPadding),
                textStyle = MaterialTheme.typography.labelMedium.copy(fontSize = 14.sp),
                textColor = contentColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultButtonPreview() {
    BaseTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(PaddingDimensions.high),
            modifier = Modifier.padding(PaddingDimensions.low)
        ) {
            DefaultButton(text = "Primary Button", onClick = {})
            DefaultButton(text = "Error Button", onClick = {}, style = ButtonStyle.ERROR)
            DefaultButton(text = "Success Button", onClick = {}, style = ButtonStyle.SUCCESS)
            DefaultButton(text = "Secondary Button", onClick = {}, style = ButtonStyle.SECONDARY)
            DefaultButton(text = "Outlined Button", onClick = {}, style = ButtonStyle.OUTLINED)
            DefaultButton(text = "Outlined Disabled", onClick = {}, style = ButtonStyle.OUTLINED, enabled = false)
        }
    }
}
