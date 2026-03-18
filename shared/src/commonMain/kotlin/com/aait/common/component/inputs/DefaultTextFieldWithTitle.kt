package com.aait.common.component.inputs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aait.common.component.text.DefaultText


@Composable
fun DefaultTextFieldWithTitle(
    modifier: Modifier = Modifier,
    title: String? = null,
    placeholderText: String = "",
    maxLines: Int = 1,
    maxLength: Int? = null,
    minLines: Int = 1,
    imeAction: ImeAction = ImeAction.Next,
    leadingIcon: (@Composable (() -> Unit))? = null,
    trailingIcon: (@Composable (() -> Unit))? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isError: Boolean = false,
    isRequired: Boolean = false,
    errorText: String? = null,
    value: String = "",
    isEnabled: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    onClick: () -> Unit = {},
    onValueChanged: (String) -> Unit = {}
) {
    val textFieldModifier = Modifier
        .fillMaxWidth()
        .then(modifier)
    Column {
        if (title.isNullOrEmpty().not()) Row {
            DefaultText(
                modifier = Modifier.padding(start = 5.dp, bottom = 5.dp),
                text = title,
                textColor = MaterialTheme.colorScheme.onSurface,
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 14.sp
                )
            )
            if (isRequired)
                DefaultText(
                    text = "*",
                    textColor = MaterialTheme.colorScheme.error,
                    textStyle = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 14.sp
                    )
                )
        }
        DefaultTextField(
            modifier = textFieldModifier,
            errorText = errorText,
            value = value,
            isEnabled = isEnabled,
            keyboardType = keyboardType,
            onValueChanged = onValueChanged,
            placeholderText = placeholderText,
            imeAction = imeAction,
            maxLength = maxLength,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            maxLines = maxLines,
            minLines = minLines,
            onClick = onClick,
        )
    }
}

@Composable
fun DefaultPasswordFieldWithTitle(
    modifier: Modifier = Modifier,
    title: String? = null,
    placeholderText: String = "",
    maxLines: Int = 1,
    minLines: Int = 1,
    imeAction: ImeAction = ImeAction.Next,
    leadingIcon: (@Composable (() -> Unit))? = null,
    isError: Boolean = false,
    isRequired: Boolean = false,
    errorText: String? = null,
    value: String = "",
    isEnabled: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Password,
    onClick: () -> Unit = {},
    onValueChanged: (String) -> Unit = {}
) {
    val isPasswordVisible = remember { mutableStateOf(false) }
    if (value.isEmpty()) VisualTransformation.None else PasswordVisualTransformation()
    DefaultTextFieldWithTitle(
        modifier = modifier,
        title = title,
        placeholderText = placeholderText,
        maxLines = maxLines,
        minLines = minLines,
        imeAction = imeAction,
        leadingIcon = leadingIcon,
        trailingIcon = {
            IconButton(onClick = { isPasswordVisible.value = (isPasswordVisible.value.not()) }) {
                Icon(
                    imageVector = if (isPasswordVisible.value) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = if (isPasswordVisible.value) "Hide password" else "Show password"
                )
            }
        },
        visualTransformation = if (isPasswordVisible.value)
            VisualTransformation.None
        else
            PasswordVisualTransformation(),
        isError = isError,
        isRequired = isRequired,
        errorText = errorText,
        value = value,
        isEnabled = isEnabled,
        keyboardType = keyboardType,
        onClick = onClick,
        onValueChanged = onValueChanged
    )
}
