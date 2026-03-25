package com.mahalatk.common.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.utilis.noRippleClickable
import com.mahalatk.common.util.getCurrentLanguageCode
import com.mahalatk.common.util.rememberLanguageChanger
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.ic_language
import org.jetbrains.compose.resources.painterResource

@Composable
fun LanguageButton(
    modifier: Modifier = Modifier,
    onLanguageChanged: () -> Unit = {},
) {
    val currentLang = getCurrentLanguageCode()
    val changeLanguage = rememberLanguageChanger()

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MahalatkTheme.white)
            .noRippleClickable {
                val newLang = if (currentLang == "ar") "en" else "ar"
                changeLanguage(newLang)
                onLanguageChanged()
            }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_language),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Color.Unspecified
        )
        Text(
            text = if (currentLang == "ar") "English" else "العربية",
            style = MahalatkTheme.bodyMedium
        )
    }
}
