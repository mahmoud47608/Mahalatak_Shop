package com.aait.base.common.component.inputs

import com.aait.base.common.component.utilis.noRippleClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aait.base.R
import com.aait.base.common.component.text.DefaultText
import com.aait.base.ui.theme.BaseTheme
import com.aait.base.ui.theme.colorPrimary
import com.aait.base.ui.theme.colorText
import com.aait.base.ui.theme.colorTextHint


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
    var currentError by remember { mutableStateOf(errorText) }

    LaunchedEffect(errorText) {
        currentError = errorText
    }

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable(onClick = { onClick?.invoke() }),
        shape = MaterialTheme.shapes.large,
        value = value,
        enabled = isEnabled,
        onValueChange = { newText ->
            if (maxLength == null || newText.length <= maxLength) {
                currentError = null
                onValueChanged(newText)
            }
        },
        textStyle = MaterialTheme.typography.bodyMedium,  // 14sp, Normal weight
        maxLines = maxLines,
        minLines = minLines ,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        visualTransformation = visualTransformation,
        placeholder = {
            Text(
                text = placeholderText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = colorTextHint()  // #6D6D6D
                )
            )
        },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = colorText(),
            unfocusedTextColor = colorText(),
            disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            errorContainerColor = MaterialTheme.colorScheme.surface,
            focusedBorderColor = colorPrimary(),
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            errorBorderColor = MaterialTheme.colorScheme.error
        ),
        isError = currentError != null,
        supportingText =
            if (currentError != null) {
                {
                    DefaultText(
                        text = currentError.toString(),
                        textStyle = MaterialTheme.typography.bodySmall,  // 12sp error text
                        textColor = MaterialTheme.colorScheme.error
                    )
                }
            } else {
                null
            }

    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, locale = "ar")
@Composable
fun DefaultTextFieldPreview() {
    var text by remember { mutableStateOf("") }
    BaseTheme {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            DefaultTextField(
                value = text,
                placeholderText = stringResource(id = R.string.please_enter_code),
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
                onValueChanged = { text = it }
            )
        }
    }


}

