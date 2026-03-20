package com.mahalatk.common.component.bottomsheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.button.ButtonStyle
import com.mahalatk.common.component.button.DefaultButton
import com.mahalatk.ui.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.ic_flag_egypt
import mahalatk.shared.generated.resources.ic_flag_usa
import mahalatk.shared.generated.resources.language_arabic
import mahalatk.shared.generated.resources.language_english
import mahalatk.shared.generated.resources.language_selector_confirm
import mahalatk.shared.generated.resources.language_selector_subtitle
import mahalatk.shared.generated.resources.language_selector_title
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

enum class AppLanguage(val code: String) {
    ARABIC("ar"),
    ENGLISH("en")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelectorBottomSheet(
    showBottomSheet: Boolean,
    currentLanguage: AppLanguage,
    onDismiss: () -> Unit,
    onLanguageSelected: (AppLanguage) -> Unit = {}
) {
    if (!showBottomSheet) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var selectedLanguage by remember { mutableStateOf(currentLanguage) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MahalatkTheme.white,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(Res.string.language_selector_title),
                style = MahalatkTheme.titleMedium,
                color = MahalatkTheme.black,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(Res.string.language_selector_subtitle),
                style = MahalatkTheme.bodyMedium,
                color = MahalatkTheme.hint,
                textAlign = TextAlign.Center
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LanguageOptionCard(
                    language = AppLanguage.ENGLISH,
                    selectedLanguage = selectedLanguage,
                    flagRes = Res.drawable.ic_flag_usa,
                    label = stringResource(Res.string.language_english),
                    modifier = Modifier.weight(1f),
                    onSelect = { selectedLanguage = it }
                )
                LanguageOptionCard(
                    language = AppLanguage.ARABIC,
                    selectedLanguage = selectedLanguage,
                    flagRes = Res.drawable.ic_flag_egypt,
                    label = stringResource(Res.string.language_arabic),
                    modifier = Modifier.weight(1f),
                    onSelect = { selectedLanguage = it }
                )
            }
            DefaultButton(
                text = stringResource(Res.string.language_selector_confirm),
                style = ButtonStyle.PRIMARY,
                modifier = Modifier.height(56.dp),
                shape = RoundedCornerShape(16.dp),
                onClick = {
                    onLanguageSelected(selectedLanguage)
                    onDismiss()
                }
            )
        }
    }
}

@Composable
private fun LanguageOptionCard(
    language: AppLanguage,
    selectedLanguage: AppLanguage,
    flagRes: DrawableResource,
    label: String,
    modifier: Modifier = Modifier,
    onSelect: (AppLanguage) -> Unit
) {
    val isSelected = language == selectedLanguage
    val backgroundColor =
        if (isSelected) MahalatkTheme.primary.copy(alpha = 0.08f) else MahalatkTheme.white
    val borderColor = if (isSelected) MahalatkTheme.primary else MahalatkTheme.border
    val cornerRadius = if (isSelected) 16.dp else 12.dp

    Box(
        modifier = modifier
            .height(120.dp)
            .background(backgroundColor, RoundedCornerShape(cornerRadius))
            .border(
                width = 1.5.dp,
                color = borderColor,
                shape = RoundedCornerShape(cornerRadius)
            )
            .clickable { onSelect(language) },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(flagRes),
                contentDescription = label,
                modifier = Modifier.size(55.dp)
            )
            Text(
                text = label,
                style = MahalatkTheme.bodyMedium,
                color = MahalatkTheme.black,
                textAlign = TextAlign.Center
            )
        }
    }
}
