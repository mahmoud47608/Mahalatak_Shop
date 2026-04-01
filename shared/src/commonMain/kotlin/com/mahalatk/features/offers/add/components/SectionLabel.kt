package com.mahalatk.features.offers.add.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.MahalatkTheme

@Composable
fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MahalatkTheme.titleMedium,
        color = AppColor.TextPrimary,
        fontWeight = FontWeight.Bold,
    )
}
