package com.aait.base.common.component.inputs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aait.base.common.component.button.RadioOption
import com.aait.base.common.component.text.DefaultText
import com.aait.base.ui.theme.ColorLightTokens
import com.mahalatak.R


@Composable
fun YesNoRadioQuestion(
    modifier: Modifier = Modifier,
    question: String,
    selectedValue: Boolean,
    onValueChange: (Boolean) -> Unit,
    isRequired: Boolean = false
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Question label
        Row(verticalAlignment = Alignment.CenterVertically) {
            DefaultText(
                text = question,
                textStyle = MaterialTheme.typography.labelLarge.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                ),
                textColor = ColorLightTokens.OnBackground
            )
            if (isRequired) {
                DefaultText(
                    text = " *",
                    textStyle = MaterialTheme.typography.labelLarge,
                    textColor = MaterialTheme.colorScheme.error
                )
            }
        }
        // Radio buttons row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioOption(
                selected = selectedValue,
                label = stringResource(R.string.yes),
                onSelect = { onValueChange(true) }
            )
            RadioOption(
                selected = !selectedValue,
                label = stringResource(R.string.no),
                onSelect = { onValueChange(false) }
            )
        }
    }
}
