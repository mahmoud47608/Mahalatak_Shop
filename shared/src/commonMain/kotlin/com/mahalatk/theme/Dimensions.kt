package com.mahalatk.theme

import androidx.compose.ui.unit.dp

/**
 * Spacing dimensions from Figma VIVO design system
 * Based on gap values: 0, 8, 12, 16, 24, 32dp
 */
object SpacingDimensions {
    /** gap/0 - 0dp */
    val sp0 = 0.dp

    /** Space/sp - 4dp */
    val sp = 4.dp

    /** Space/sp 1 - 8dp (common gap) */
    val sp1 = 8.dp

    /** Space/sp 2 - 12dp (common gap) */
    val sp2 = 12.dp

    /** px/4 - 16dp (common gap) */
    val sp4 = 16.dp

    /** px/6 - 24dp (from Figma) */
    val sp6 = 24.dp

    /** Gap - 32dp (common gap) */
    val sp8 = 32.dp
}

/**
 * Padding dimensions from Figma VIVO design system
 * Based on: 8dp (small elements), 12dp (inputs), 16dp (sections), 24dp (horizontal button padding)
 */
object PaddingDimensions {
    /** px/6 - 24dp (button horizontal padding from Figma) */
    val extraHigh = 24.dp

    /** 16dp - Section padding */
    val high = 16.dp

    /** 12dp - Input padding from Figma */
    val medium = 12.dp

    /** 8dp - Small element padding from Figma */
    val small = 8.dp

    /** 4dp */
    val low = 4.dp

    /** 2dp */
    val veryLow = 2.dp
}

/**
 * Corner radius dimensions from Figma VIVO design system
 * --radius-i-sm: 8dp (buttons, inputs)
 * Container radius: 12dp (cards, containers)
 */
object CornerDimensions {

    /** Interactive border radius/--radius-i-xs - 4dp */
    val xs = 4.dp

    /** Interactive border radius/--radius-i-sm - 8dp (buttons, inputs from Figma) */
    val sm = 8.dp

    /** Container radius - 12dp (cards, containers from Figma) */
    val md = 12.dp

    /** Large corner radius - 16dp */
    val lg = 16.dp

    /** Extra large corner radius - 20dp */
    val xl = 20.dp

    /** Full/Circular corner radius - 100dp */
    val full = 100.dp

    // Legacy naming aliases
    /** Container/Card radius - 12dp */
    val high = md

    /** Button/Input radius - 8dp (--radius-i-sm from Figma) */
    val medium = sm

    /** Small radius - 4dp */
    val low = xs

    /** Very small radius - 2dp */
    val veryLow = 2.dp
}

/**
 * Icon size dimensions
 */
object IconDimensions {
    val small = 16.dp
    val medium = 20.dp
    val large = 24.dp
    val extraLarge = 32.dp
}

/**
 * Button dimensions
 */
object ButtonDimensions {
    val heightSmall = 36.dp
    val heightMedium = 44.dp
    val heightLarge = 52.dp
    val minWidth = 88.dp
}

/**
 * Input field dimensions
 */
object InputDimensions {
    val height = 48.dp
    val heightSmall = 40.dp
}
