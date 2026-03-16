package com.aait.base.common.component.stepper

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aait.base.common.component.text.DefaultText
import com.aait.base.ui.theme.PaddingDimensions

/**
 * A horizontal stepper component.
 *
 * @param steps List of titles for each step
 * @param currentStep Current step index (0-based)
 */
@Composable
fun DefaultStepper(
    modifier: Modifier = Modifier,
    steps: List<String>,
    currentStep: Int
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val outlineColor = MaterialTheme.colorScheme.outline
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
    val onSurfaceVariantColor = MaterialTheme.colorScheme.onSurfaceVariant

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center
    ) {
        steps.forEachIndexed { index, title ->
            val isCompleted = index < currentStep
            val isCurrent = index == currentStep
            val isLast = index == steps.size - 1

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Step Indicator and Line
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left Line (if not first)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(2.dp)
                            .background(if (index > 0 && index <= currentStep) primaryColor else if (index > 0) outlineColor else Color.Transparent)
                    )

                    // Dot
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted || isCurrent) primaryColor else outlineColor),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            // You could add a checkmark icon here
                        }
                        DefaultText(
                            text = (index + 1).toString(),
                            textStyle = MaterialTheme.typography.bodySmall,
                            textColor = if (isCompleted || isCurrent) onPrimaryColor else onSurfaceVariantColor
                        )
                    }

                    // Right Line (if not last)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(2.dp)
                            .background(if (index < currentStep) primaryColor else if (!isLast) outlineColor else Color.Transparent)
                    )
                }

                Spacer(modifier = Modifier.height(PaddingDimensions.low))

                // Title
                DefaultText(
                    text = title,
                    textStyle = MaterialTheme.typography.bodySmall,
                    textColor = if (isCurrent) primaryColor else onSurfaceVariantColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = PaddingDimensions.low)
                )
            }
        }
    }
}
