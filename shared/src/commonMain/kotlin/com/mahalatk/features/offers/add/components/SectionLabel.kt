package com.mahalatk.features.offers.add.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.MahalatkTheme

@Composable
fun SectionLabel(
    text: String,
    subtitle: String? = null,
) {
    Column {
        Text(
            text = text,
            style = MahalatkTheme.titleMedium,
            color = AppColor.TextPrimary,
            fontWeight = FontWeight.Bold,
        )
        if (subtitle != null) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MahalatkTheme.bodySmall,
                color = AppColor.TextHint,
            )
        }
    }
}
