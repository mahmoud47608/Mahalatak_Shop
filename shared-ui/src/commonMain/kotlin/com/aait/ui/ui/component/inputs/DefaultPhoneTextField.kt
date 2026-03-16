package com.aait.ui.component.inputs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aait.ui.component.text.DefaultText
import com.aait.ui.theme.PaddingDimensions
import com.aait.ui.theme.colorPrimary
import com.aait.ui.theme.colorText
import org.jetbrains.compose.resources.stringResource
import com.mahalatak.shared.Res
import com.mahalatak.shared.*


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
 * @param countryFlagIcon ImageVector for country flag (null to hide)
 * @param phoneIcon ImageVector for phone icon
 */
@Composable
fun DefaultTextFieldPhoneWithCode(
    modifier: Modifier = Modifier,
    phoneValue: String = "",
    isEnabled: Boolean = true,
    isRequired: Boolean = false,
    imeAction: ImeAction = ImeAction.Done,
    title: String? = stringResource(Res.string.phone),
    phoneErrorText: String? = null,
    onPhoneChange: (String) -> Unit = {},
    countryCode: String = "+966",
    countryFlagIcon: ImageVector? = null,
    phoneIcon: ImageVector? = Icons.Filled.Phone
) {
    var currentError by remember { mutableStateOf(phoneErrorText) }
    LaunchedEffect(phoneErrorText) {
        currentError = phoneErrorText
    }
    Column {
        if (title.isNullOrEmpty().not()) Row {
            DefaultText(
                modifier = Modifier.padding(start = 5.dp, top = 5.dp, bottom = 5.dp),
                text = title,
                textColor = colorText(),
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 14.sp
                )
            )
            if (isRequired)
                DefaultText(
                    modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
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
            placeholderText = stringResource(Res.string.please_enter_mobile_number),
            keyboardType = KeyboardType.Phone,
            imeAction = imeAction,
            maxLength = 15,
            leadingIcon = {
                phoneIcon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = Color.Unspecified,
                    )
                }
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
                            imageVector = flagIcon,
                            contentDescription = null,
                            tint = Color.Unspecified,
                        )
                    }
                }
            }
        )
    }
}
