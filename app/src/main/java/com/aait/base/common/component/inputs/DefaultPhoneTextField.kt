package com.aait.base.common.component.inputs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aait.base.common.component.text.DefaultText
import com.aait.base.ui.theme.BaseTheme
import com.aait.base.ui.theme.PaddingDimensions
import com.aait.base.ui.theme.colorPrimary
import com.aait.base.ui.theme.colorText
import com.mahalatak.R


/**
 * Phone number text field with country code prefix.
 *
 * Displays a text field for phone number input with a configurable country code
 * and flag icon. Supports validation and error display.
 *
 * @param modifier Optional modifier for this component
 * @param phoneValue Current phone number value
 * @param isEnabled Whether the field is enabled for input
 * @param isRequired Whether to show a required asterisk
 * @param imeAction IME action for the keyboard
 * @param title Optional title label above the field
 * @param phoneErrorText Error message to display below the field
 * @param onPhoneChange Callback invoked when phone number changes
 * @param countryCode Country code prefix (e.g., "+966", "+1", "+44")
 * @param countryFlagIcon Drawable resource for country flag (null to hide)
 * @param phoneIcon Drawable resource for phone icon
 */
@Composable
fun DefaultTextFieldPhoneWithCode(
    modifier: Modifier = Modifier,
    phoneValue: String = "",
    isEnabled: Boolean = true,
    isRequired: Boolean = false,
    imeAction: ImeAction = ImeAction.Done,
    title: String? = stringResource(id = R.string.phone),
    phoneErrorText: String? = null,
    onPhoneChange: (String) -> Unit = {},
    countryCode: String = "+966",
    countryFlagIcon: Int? = R.drawable.app_icon,
    phoneIcon: Int = R.drawable.app_icon
) {
    var currentError by remember { mutableStateOf(phoneErrorText) }
    LaunchedEffect(phoneErrorText) {
        currentError = phoneErrorText
    }
    Column {
        if (title.isNullOrEmpty().not()) Row {
            DefaultText(
                modifier = Modifier.padding(start = 5.dp,top = 5.dp,bottom = 5.dp),
                text = title,
                textColor = colorText(),
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 14.sp
                )
            )
            if (isRequired)
                DefaultText(
                    modifier = Modifier.padding(top = 5.dp,bottom = 5.dp),
                    text = "*",
                    textColor = Color.Red,
                    textStyle = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 14.sp
                    )
                )
        }
        DefaultTextField(
            value = phoneValue,
            onValueChanged = onPhoneChange,
            isEnabled = isEnabled,
            errorText = currentError,
            placeholderText = stringResource(id = R.string.please_enter_mobile_number),
            keyboardType = KeyboardType.Phone,
            imeAction = imeAction,
            maxLength = 15,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = phoneIcon),
                    contentDescription = null,
                    tint = Color.Unspecified,
                )
            },
            trailingIcon = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    DefaultText(
                        text = countryCode,
                        textColor = colorPrimary(),
                        textStyle = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 14.sp
                        )
                    )
                    countryFlagIcon?.let { flagIcon ->
                        Icon(
                            modifier = Modifier.padding(
                                horizontal = PaddingDimensions.medium,
                            ),
                            painter = painterResource(id = flagIcon),
                            contentDescription = null,
                            tint = Color.Unspecified,
                        )
                    }
                }
            }
        )
    }
}
@Preview(showBackground = true, locale = "ar")
@Composable
fun DefaultPhoneTextFieldPreview() {
    BaseTheme {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
        DefaultTextFieldPhoneWithCode(
            isRequired = true
        )
        }
    }
}