package com.mahalatk.common.component.inputs

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.utilis.noRippleClickable
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.MahalatkTheme


@Composable
fun DefaultTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChanged: (String) -> Unit,
    placeholderText: String = "",
    errorText: String? = null,
    maxLength: Int? = null,
    maxLines: Int = 1,
    minLines: Int = 1,
    fieldHeight:Int=56,
    isEnabled: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Default,
    leadingIcon: (@Composable (() -> Unit))? = null,
    trailingIcon: (@Composable (() -> Unit))? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onClick: (() -> Unit)? = null
) {
    val shape = com.mahalatk.theme.AppShapes.Large

    // Stable click handler — avoids new lambda allocation every recomposition
    val currentOnClick = rememberUpdatedState(onClick)

    var isFocused by remember { mutableStateOf(false) }
    val borderColor by animateColorAsState(
        targetValue = when {
            !errorText.isNullOrEmpty() -> MahalatkTheme.error
            isFocused -> MahalatkTheme.primary
            else -> MahalatkTheme.border
        },
        animationSpec = tween(200),
    )

    Column(modifier = modifier.fillMaxWidth()) {

        OutlinedTextField(
            value = value,
            onValueChange = { newText ->
                if (maxLength == null || newText.length <= maxLength) {
                    onValueChanged(newText)
                }
            },
            textStyle = MahalatkTheme.bodyMedium.copy(
                color = MahalatkTheme.black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(fieldHeight.dp)
                .clip(shape)
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = shape,
                )
                .onFocusChanged { isFocused = it.isFocused }
                .noRippleClickable(onClick = { currentOnClick.value?.invoke() }),
            isError = !errorText.isNullOrEmpty(),
            placeholder = {
                if (placeholderText.isNotEmpty()) {
                    Text(
                        text = placeholderText,
                        style = MahalatkTheme.bodyMedium,
                        color = MahalatkTheme.hint
                    )
                }
            },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            visualTransformation = visualTransformation,
            singleLine = minLines == 1 && maxLines == 1,
            minLines = minLines,
            maxLines = maxLines,
            enabled = isEnabled,
            shape = shape,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent,
                errorBorderColor = Color.Transparent,
                focusedContainerColor = AppColor.Surface,
                unfocusedContainerColor = AppColor.Surface,
                disabledContainerColor = AppColor.Surface,
                errorContainerColor = AppColor.Surface,
                disabledTextColor = MahalatkTheme.black,
                disabledPlaceholderColor = MahalatkTheme.hint
            )
        )

        if (!errorText.isNullOrEmpty()) {
            Text(
                text = errorText,
                style = MaterialTheme.typography.bodySmall,
                color = MahalatkTheme.error,
                modifier = Modifier.fillMaxWidth().padding(start = 12.dp, top = 4.dp)
            )
        }
    }
}
