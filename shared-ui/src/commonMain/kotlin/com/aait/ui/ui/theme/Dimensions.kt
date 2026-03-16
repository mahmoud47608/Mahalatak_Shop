package com.aait.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.dp

/**
 * Spacing dimensions from Figma VIVO design system
 * Based on gap values: 0, 8, 12, 16, 24, 32dp
 */
object SpacingDimensions {
    /** gap/0 - 0dp */
    val sp0
        @Composable
        @Stable
        get() = 0.dp

    /** Space/sp - 4dp */
    val sp
        @Composable
        @Stable
        get() = 4.dp

    /** Space/sp 1 - 8dp (common gap) */
    val sp1
        @Composable
        @Stable
        get() = 8.dp

    /** Space/sp 2 - 12dp (common gap) */
    val sp2
        @Composable
        @Stable
        get() = 12.dp

    /** px/4 - 16dp (common gap) */
    val sp4
        @Composable
        @Stable
        get() = 16.dp

    /** px/6 - 24dp (from Figma) */
    val sp6
        @Composable
        @Stable
        get() = 24.dp

    /** Gap - 32dp (common gap) */
    val sp8
        @Composable
        @Stable
        get() = 32.dp
}

/**
 * Padding dimensions from Figma VIVO design system
 * Based on: 8dp (small elements), 12dp (inputs), 16dp (sections), 24dp (horizontal button padding)
 */
object PaddingDimensions {
    /** px/6 - 24dp (button horizontal padding from Figma) */
    val extraHigh
        @Composable
        @Stable
        get() = 24.dp

    /** 16dp - Section padding */
    val high
        @Composable
        @Stable
        get() = 16.dp

    /** 12dp - Input padding from Figma */
    val medium
        @Composable
        @Stable
        get() = 12.dp

    /** 8dp - Small element padding from Figma */
    val small
        @Composable
        @Stable
        get() = 8.dp

    /** 4dp */
    val low
        @Composable
        @Stable
        get() = 4.dp

    /** 2dp */
    val veryLow
        @Composable
        @Stable
        get() = 2.dp
}

/**
 * Corner radius dimensions from Figma VIVO design system
 * --radius-i-sm: 8dp (buttons, inputs)
 * Container radius: 12dp (cards, containers)
 */
object CornerDimensions {

    /** Interactive border radius/--radius-i-xs - 4dp */
    val xs
        @Composable
        @Stable
        get() = 4.dp

    /** Interactive border radius/--radius-i-sm - 8dp (buttons, inputs from Figma) */
    val sm
        @Composable
        @Stable
        get() = 8.dp

    /** Container radius - 12dp (cards, containers from Figma) */
    val md
        @Composable
        @Stable
        get() = 12.dp

    /** Large corner radius - 16dp */
    val lg
        @Composable
        @Stable
        get() = 16.dp

    /** Extra large corner radius - 20dp */
    val xl
        @Composable
        @Stable
        get() = 20.dp

    /** Full/Circular corner radius - 100dp */
    val full
        @Composable
        @Stable
        get() = 100.dp

    // Legacy naming aliases
    /** Container/Card radius - 12dp */
    val high
        @Composable
        @Stable
        get() = md

    /** Button/Input radius - 8dp (--radius-i-sm from Figma) */
    val medium
        @Composable
        @Stable
        get() = sm

    /** Small radius - 4dp */
    val low
        @Composable
        @Stable
        get() = xs

    /** Very small radius - 2dp */
    val veryLow
        @Composable
        @Stable
        get() = 2.dp
}

/**
 * Icon size dimensions
 */
object IconDimensions {
    val small
        @Composable
        @Stable
        get() = 16.dp

    val medium
        @Composable
        @Stable
        get() = 20.dp

    val large
        @Composable
        @Stable
        get() = 24.dp

    val extraLarge
        @Composable
        @Stable
        get() = 32.dp
}

/**
 * Button dimensions
 */
object ButtonDimensions {
    val heightSmall
        @Composable
        @Stable
        get() = 36.dp

    val heightMedium
        @Composable
        @Stable
        get() = 44.dp

    val heightLarge
        @Composable
        @Stable
        get() = 52.dp

    val minWidth
        @Composable
        @Stable
        get() = 88.dp
}

/**
 * Input field dimensions
 */
object InputDimensions {
    val height
        @Composable
        @Stable
        get() = 48.dp

    val heightSmall
        @Composable
        @Stable
        get() = 40.dp
}
