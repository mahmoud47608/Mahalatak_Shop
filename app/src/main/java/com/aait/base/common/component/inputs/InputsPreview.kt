package com.aait.base.common.component.inputs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aait.base.ui.theme.BaseTheme
import com.aait.domain.entity.general.DataItem
import com.mahalatak.R


@Preview(showBackground = true)
@Composable
fun DefaultTextFieldsPreview() {
    BaseTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PaddingValues(10.dp)),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DefaultTextFieldWithTitle(
                title = "Title",
                value = "",
                onValueChanged = {},
                placeholderText = "Placeholder",
                maxLines = 1,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                leadingIcon = null,
                trailingIcon = null,
                isError = true,
                errorText = "Error",
            )
            DefaultTextFieldPhoneWithCode(
                title = "Title",
                phoneValue = "",
                imeAction = ImeAction.Next,
            )
            DefaultDropDown(
                title = stringResource(R.string.city_required),
                selectedItem = DataItem(),
                items = listOf(),
                placeholderText = stringResource(R.string.please_select_city),
                onItemSelected = { }
            )
            PinCodeTextField(
                pinValue = "1",
                onPinChanged = { }
            )
        }
    }
}