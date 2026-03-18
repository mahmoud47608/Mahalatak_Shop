package com.aait.common.component.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aait.common.component.text.DefaultText

@Composable
fun RadioOption(
    selected: Boolean,
    label: String,
    modifier: Modifier = Modifier,
    onSelect: () -> Unit
) {
    Row(
        modifier = modifier
            .selectable(
                selected = selected,
                onClick = onSelect
            ),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
                unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        DefaultText(
            text = label,
            textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
            textColor = MaterialTheme.colorScheme.onSurface
        )
    }
}


