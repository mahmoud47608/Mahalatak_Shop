package com.mahalatk.common.component.inputs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import com.mahalatk.common.component.text.DefaultText
import com.mahalatk.common.component.utilis.noRippleClickable
import com.mahalatk.theme.MahalatkTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTextField(
    modifier: Modifier = Modifier,
    errorText: String? = null,
    value: String,
    maxLines: Int = 1,
    minLines: Int = 1,
    maxLength: Int? = null,
    placeholderText: String,
    isEnabled: Boolean = true,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    leadingIcon: (@Composable (() -> Unit))? = null,
    trailingIcon: (@Composable (() -> Unit))? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onClick: (() -> Unit)? = null,
    onValueChanged: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable(onClick = { onClick?.invoke() }),
        shape = MaterialTheme.shapes.large,
        value = value,
        enabled = isEnabled,
        onValueChange = { newText ->
            if (maxLength == null || newText.length <= maxLength) {
                onValueChanged(newText)
            }
        },
        textStyle = MahalatkTheme.bodyMedium,
        maxLines = maxLines,
        minLines = minLines,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        visualTransformation = visualTransformation,
        placeholder = {
            Text(
                text = placeholderText,
                style = MahalatkTheme.bodyMedium.copy(
                    color = MahalatkTheme.hint
                )
            )
        },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MahalatkTheme.black,
            unfocusedTextColor = MahalatkTheme.black,
            disabledTextColor = MahalatkTheme.black.copy(alpha = 0.6f),
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            errorContainerColor = MaterialTheme.colorScheme.surface,
            focusedBorderColor = MahalatkTheme.primary,
            unfocusedBorderColor = MahalatkTheme.border,
            disabledBorderColor = MahalatkTheme.border.copy(alpha = 0.5f),
            disabledPlaceholderColor = MahalatkTheme.hint.copy(alpha = 0.5f),
            errorBorderColor = MahalatkTheme.error
        ),
        isError = errorText != null,
        supportingText =
            if (errorText != null) {
                {
                    DefaultText(
                        text = errorText,
                        textStyle = MahalatkTheme.bodySmall,
                        textColor = MahalatkTheme.error
                    )
                }
            } else {
                null
            }
    )
}
