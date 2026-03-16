package com.aait.base.common.component.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aait.base.common.component.text.DefaultText
import com.aait.base.ui.theme.BaseTheme


@Composable
fun PinCodeTextField(
    modifier: Modifier = Modifier,
    pinLength: Int = 4,
    pinValue: String,
    onPinChanged: (String) -> Unit,
    onPinEntered: (String) -> Unit = {},
    isError: Boolean = false,
    errorText: String? = null,
    focusRequester: FocusRequester = FocusRequester(),
    enabled: Boolean = true
) {
    val focusManager = LocalFocusManager.current
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        BasicTextField(
            value = pinValue,
            onValueChange = { value ->
                if (value.length <= pinLength && value.all { it.isDigit() }) {
                    onPinChanged(value)
                    if (value.length == pinLength) {
                        onPinEntered(value)
                        focusManager.clearFocus()
                    }
                }
            },
            enabled = enabled,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done
            ),
            cursorBrush = SolidColor(Color.Transparent), // Hide cursor since we show custom indicators
            textStyle = TextStyle(
                color = Color.Transparent, // Hide the actual text
                fontSize = 0.sp
            ),
            decorationBox = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // PIN Boxes
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(pinLength) { index ->
                            PinBox(
                                digit = pinValue.getOrNull(index)?.toString() ?: "",
                                isFocused = pinValue.length == index && enabled,
                                isFilled = index < pinValue.length,
                                isError = isError,
                                enabled = enabled
                            )
                        }
                    }
                    // Error Text
                    if (isError && !errorText.isNullOrBlank()) {
                        DefaultText(
                            text = errorText,
                            textStyle = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.error
                            ),
                            textColor = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            },
            modifier = modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
        )
    }
}
@Composable
private fun PinBox(
    digit: String,
    isFocused: Boolean,
    isFilled: Boolean,
    isError: Boolean,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when {
        !enabled -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        isError -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)
        else -> Color.White
    }
    val borderColor = when {
        isError -> MaterialTheme.colorScheme.error
        isFocused -> MaterialTheme.colorScheme.primary
        isFilled -> MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    }
    val borderWidth = when {
        isFocused -> 1.dp
        isFilled || isError -> 1.dp
        else -> .7.dp
    }
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
            .border(
                width = borderWidth,
                color = borderColor,
                shape = RoundedCornerShape(24.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isFilled) {
            Text(
                text = digit,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            )
        } else if (isFocused) {
            // Blinking cursor effect
            var showCursor by remember { mutableStateOf(true) }
            LaunchedEffect(Unit) {
                while (true) {
                    kotlinx.coroutines.delay(500)
                    showCursor = !showCursor
                }
            }
            if (showCursor) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(20.dp)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(1.dp)
                        )
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PinCodeTextFieldPreview() {
    BaseTheme {
        PinCodeTextField(
            pinValue = "",
            onPinChanged = { }
        )
    }
}