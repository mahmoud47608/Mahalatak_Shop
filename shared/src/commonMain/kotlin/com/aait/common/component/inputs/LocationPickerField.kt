package com.aait.common.component.inputs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


/**
 * A text field that opens a map screen for location selection.
 * Displays the selected location address.
 *
 * @param modifier Modifier for the component
 * @param value Current location address, or null if not set
 * @param title Label displayed above the text field
 * @param placeholderText Hint text shown when value is null
 * @param errorText Error message to display, or null if no error
 * @param isEnabled Whether the field is enabled
 * @param isRequired Whether to show the required asterisk
 * @param onClick Callback when the field is clicked to select location
 */
@Composable
fun LocationPickerField(
    modifier: Modifier = Modifier,
    value: String?,
    title: String? = null,
    placeholderText: String,
    errorText: String? = null,
    isEnabled: Boolean = true,
    isRequired: Boolean = false,
    onClick: () -> Unit
) {
    val fieldModifier = Modifier
        .fillMaxWidth()
        .then(modifier)
    SelectorField(
        modifier = fieldModifier,
        value = value ?: "",
        title = title,
        placeholderText = placeholderText,
        errorText = errorText,
        isEnabled = isEnabled,
        isRequired = isRequired,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Select location",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        onClick = onClick
    )
}
