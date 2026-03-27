package com.mahalatk.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

/**
 * Pre-allocated shape instances to avoid recreating on every recomposition.
 * Use these instead of inline RoundedCornerShape() calls.
 */
object AppShapes {
    val Small = RoundedCornerShape(8.dp)
    val Medium = RoundedCornerShape(12.dp)
    val Large = RoundedCornerShape(16.dp)
    val ExtraLarge = RoundedCornerShape(20.dp)
    val Card = RoundedCornerShape(CornerDimensions.lg)
    val BottomSheet = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    val Header = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
}

object AppPadding {
    val ButtonContent = PaddingValues(vertical = 8.dp)
}
