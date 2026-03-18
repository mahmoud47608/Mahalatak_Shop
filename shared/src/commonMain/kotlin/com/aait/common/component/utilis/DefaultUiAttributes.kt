package com.aait.common.component.utilis

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.aait.ui.theme.PaddingDimensions

/**
 * Default horizontal padding aligned with PaddingDimensions.high (16.dp)
 * Use PaddingDimensions directly instead of this function
 */
@Deprecated(
    "Use PaddingDimensions.high instead",
    replaceWith = ReplaceWith("PaddingDimensions.high", "com.aait.base.ui.theme.PaddingDimensions")
)
@Composable
fun defaultHorizontalPadding() = PaddingDimensions.high

/**
 * Default vertical padding aligned with PaddingDimensions.medium (8.dp)
 * Use PaddingDimensions directly instead of this function
 */
@Deprecated(
    "Use PaddingDimensions.medium instead",
    replaceWith = ReplaceWith(
        "PaddingDimensions.medium",
        "com.aait.base.ui.theme.PaddingDimensions"
    )
)
@Composable
fun defaultVerticalPadding() = PaddingDimensions.medium

/**
 * Default body small text size (12sp)
 * Use MaterialTheme.typography.bodySmall instead
 */
@Deprecated(
    "Use MaterialTheme.typography.bodySmall.fontSize instead",
    replaceWith = ReplaceWith("12.sp", "androidx.compose.ui.unit.sp")
)
@Composable
fun defaultTextSize() = 12.sp

/**
 * Default label large text size (14sp)
 * Use MaterialTheme.typography.labelLarge instead
 */
@Deprecated(
    "Use MaterialTheme.typography.labelLarge.fontSize instead",
    replaceWith = ReplaceWith("14.sp", "androidx.compose.ui.unit.sp")
)
@Composable
fun defaultTitleTextSize() = 14.sp

/**
 * Default text field font size (14sp - bodyMedium)
 * Use MaterialTheme.typography.bodyMedium instead
 */
@Deprecated(
    "Use MaterialTheme.typography.bodyMedium.fontSize instead",
    replaceWith = ReplaceWith("14.sp", "androidx.compose.ui.unit.sp")
)
@Composable
fun defaultTextFieldFontSize() = 14.sp

/**
 * Default text field error font size (12sp - bodySmall)
 * Use MaterialTheme.typography.bodySmall instead
 */
@Deprecated(
    "Use MaterialTheme.typography.bodySmall.fontSize instead",
    replaceWith = ReplaceWith("12.sp", "androidx.compose.ui.unit.sp")
)
@Composable
fun defaultTextFieldErrorFontSize() = 12.sp
