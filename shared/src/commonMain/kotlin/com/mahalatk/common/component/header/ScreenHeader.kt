package com.mahalatk.common.component.header

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.MahalatkTheme

/**
 * Reusable screen header with primary gradient background.
 * Used across Products, Orders, Chat screens.
 */
@Composable
fun ScreenHeader(
    title: String,
    modifier: Modifier = Modifier,
    height: Dp = 90.dp,
) {
    val gradient = remember {
        Brush.verticalGradient(
            colors = listOf(AppColor.Primary, AppColor.Primary.copy(alpha = 0.85f)),
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(gradient),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Text(
            text = title,
            style = MahalatkTheme.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 14.dp),
        )
    }
}
