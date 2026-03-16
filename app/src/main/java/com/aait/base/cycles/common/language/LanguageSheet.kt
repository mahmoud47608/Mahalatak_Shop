package com.aait.base.cycles.common.language

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.aait.base.ui.theme.PaddingDimensions
import com.aait.base.ui.theme.SpacingDimensions
import com.mahalatak.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSheet(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onLanguageSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val appLocaleManager = remember { AppLocaleManager(context) }

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = null,
    ) {
        LanguageSheetContent(appLocaleManager.getCurrentLocale()) { selectedLanguageCode ->
            if (selectedLanguageCode != appLocaleManager.getCurrentLocale())
                onLanguageSelected(selectedLanguageCode)
        }
    }
}

@Composable
private fun LanguageSheetContent(
    selectedLanguageCode: String,
    onLanguageSelected: (String) -> Unit
) {
    val languages = listOf(
        "ar" to stringResource(R.string.arabic),
        "en" to stringResource(R.string.english)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = PaddingDimensions.extraHigh)
            .background(MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.change_language),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(SpacingDimensions.sp2))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = PaddingDimensions.extraHigh)
        ) {
            items(languages.size) {
                LanguageItem(
                    label = languages[it].second,
                    isSelected = selectedLanguageCode == languages[it].first,
                    onSelect = { onLanguageSelected(languages[it].first) }
                )
            }
        }
    }
}

@Composable
private fun LanguageItem(
    label: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .padding(horizontal = PaddingDimensions.high, vertical = PaddingDimensions.medium)
    ) {
        RadioButton(
            selected = isSelected,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
                unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            onClick = onSelect
        )

        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = SpacingDimensions.sp1)
        )
    }
}
