package com.aait.base.common.component.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * Modifier extension that draws an underline beneath the content.
 *
 * @param color Color of the underline
 * @return Modified Modifier with underline drawn
 */
@Composable
fun Modifier.textUnderline(
    color: Color,
): Modifier {
    return this.drawWithContent {
        drawContent()

        drawLine(
            color = color,
            start = Offset(0f, (size.height - (size.height / 10))),
            end = Offset(size.width, size.height - (size.height / 10)),
            strokeWidth = 1.dp.toPx()
        )
    }
}

/**
 * Default text component following Material Design 3 patterns.
 *
 * A standardized text component that applies theme colors and typography.
 * Use this component for consistent text rendering throughout the application.
 *
 * @param text The text to display
 * @param modifier Optional modifier for this component
 * @param textColor Color of the text (defaults to theme onBackground)
 * @param textStyle Typography style to apply (defaults to bodyMedium)
 * @param textAlign Text alignment (start, center, end, etc.)
 * @param maxLines Maximum number of lines before truncation
 * @param overflow Text overflow behavior when maxLines is exceeded
 *
 * Example:
 * ```kotlin
 * DefaultText(
 *     text = "Hello World",
 *     textStyle = MaterialTheme.typography.headlineMedium,
 *     textColor = MaterialTheme.colorScheme.primary
 * )
 * ```
 */
@Composable
fun DefaultText(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    Text(
        text = text,
        modifier = modifier,
        color = textColor,
        style = textStyle,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}
