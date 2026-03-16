package com.aait.base.common.component.inputs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aait.base.ui.theme.BaseTheme


@Preview(showBackground = true)
@Composable
fun EnhancedPinCodeTextFieldPreview() {
    BaseTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Empty state
            PinCodeTextField(
                pinValue = "",
                onPinChanged = { }
            )

            // Partially filled
            PinCodeTextField(
                pinValue = "12",
                onPinChanged = { }
            )

            // Filled
            PinCodeTextField(
                pinValue = "1234",
                onPinChanged = { }
            )

            // Error state
            PinCodeTextField(
                pinValue = "123",
                onPinChanged = { },
                isError = true,
                errorText = "كود التحقق غير صحيح"
            )

            // Disabled state
            PinCodeTextField(
                pinValue = "12",
                onPinChanged = { },
                enabled = false
            )
        }
    }
}