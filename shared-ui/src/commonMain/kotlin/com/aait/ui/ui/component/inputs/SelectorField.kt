package com.aait.ui.component.inputs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aait.ui.component.text.DefaultText
import com.aait.ui.theme.PaddingDimensions

/**
 * A text field component designed for triggering selectors (bottom sheets, dialogs).
 * Includes a label, optional required asterisk, and a dropdown arrow icon.
 *
 * @param modifier Modifier for the root layout
 * @param value Current text value to display
 * @param title Optional title/label to display above the field
 * @param placeholderText Hint text when value is empty
 * @param errorText Optional error message to show below the field
 * @param isEnabled Whether the field is interactive
 * @param isRequired Whether to show the required asterisk (*)
 * @param onClick Callback triggered when the field is clicked
 */
@Composable
fun SelectorField(
    modifier: Modifier = Modifier,
    value: String,
    title: String? = null,
    placeholderText: String,
    errorText: String? = null,
    isEnabled: Boolean = true,
    isRequired: Boolean = false,
    trailingIcon: (@Composable (() -> Unit))? = null,
    onClick: () -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (!title.isNullOrEmpty()) {
            Row {
                DefaultText(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .padding(bottom = PaddingDimensions.low),
                    text = title,
                    textColor = MaterialTheme.colorScheme.onSurface,
                    textStyle = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 14.sp
                    )
                )
                if (isRequired) {
                    DefaultText(
                        text = " *",
                        textColor = MaterialTheme.colorScheme.error,
                        textStyle = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 14.sp
                        )
                    )
                }
            }
        }

        DefaultTextField(
            value = value,
            placeholderText = placeholderText,
            errorText = errorText,
            isEnabled = false,
            keyboardType = KeyboardType.Unspecified,
            imeAction = ImeAction.None,
            trailingIcon = trailingIcon ?: {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            onClick = if (isEnabled) onClick else null,
            onValueChanged = { }
        )
    }
}
