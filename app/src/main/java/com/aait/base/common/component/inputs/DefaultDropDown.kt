package com.aait.base.common.component.inputs

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.aait.base.common.component.text.DefaultText
import com.aait.base.common.component.utilis.noRippleClickable
import com.aait.base.ui.theme.BaseTheme
import com.aait.domain.entity.general.DataItem
import com.mahalatak.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultDropDown(
    modifier: Modifier = Modifier,
    title: String,
    placeholderText: String = "",
    selectedItem: DataItem? = null,
    isEnabled: Boolean = true,
    isRequired: Boolean = false,
    items: List<DataItem> = emptyList(),
    errorText: String? = null,
    leadingIcon: (@Composable (() -> Unit))? = null,
    onItemSelected: (DataItem) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    var currentError by remember { mutableStateOf(errorText) }
    LaunchedEffect(errorText) {
        currentError = errorText
    }
    Column(modifier = modifier.fillMaxWidth()) {
        if (title.isNotEmpty()) Row {
            DefaultText(
                modifier = Modifier.padding(top = 5.dp, bottom = 5.dp, start = 5.dp),
                text = title,
                textColor = MaterialTheme.colorScheme.onSurface,
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 14.sp
                )
            )
            if (isRequired)
                DefaultText(
                    modifier = Modifier.padding(top = 5.dp),
                    text = "*",
                    textColor = MaterialTheme.colorScheme.error,
                    textStyle = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 14.sp
                    )
                )
        }
        ExposedDropdownMenuBox(
            expanded = expanded && isEnabled,
            onExpandedChange = { if (isEnabled) expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                modifier = modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.SecondaryEditable, true),
                readOnly = true,
                shape = MaterialTheme.shapes.large,
                value = selectedItem?.name ?: "",
                onValueChange = {},
                placeholder = {
                    Text(
                        text = placeholderText,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                },
                leadingIcon = leadingIcon,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                textStyle = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                enabled = isEnabled,
                isError = (currentError != null),
                supportingText = if (currentError != null) {
                    {
                        DefaultText(
                            text = currentError.toString(),
                            textStyle = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 12.sp, color = MaterialTheme.colorScheme.error
                            ),
                            textColor = MaterialTheme.colorScheme.error
                        )
                    }
                } else {
                    null
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    errorContainerColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                    errorBorderColor = MaterialTheme.colorScheme.error
                )
            )
            ExposedDropdownMenu(
                expanded = expanded && isEnabled,
                onDismissRequest = { expanded = false }
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            DefaultText(
                                text = item.name ?: "",
                                textColor = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        onClick = {
                            expanded = false
                            currentError = null
                            onItemSelected(item)
                        }
                    )
                }
            }
        }
    }
}
/**
 * Marital status dropdown with gender-aware options.
 *
 * @param customOptions Custom list of options to override defaults
 */
@Composable
fun MaritalStatusDropDown(
    modifier: Modifier = Modifier,
    title: String,
    placeholderText: String = "",
    selectedItem: DataItem? = null,
    isEnabled: Boolean = true,
    isRequired: Boolean = false,
    errorText: String? = null,
    leadingIcon: (@Composable (() -> Unit))? = null,
    gender: DataItem? = null,
    customOptions: List<DataItem>? = null,
    onItemSelected: (DataItem) -> Unit = {}
) {
    // Determine if gender is female
    val isFemale = gender?.key?.lowercase() == "female"
    val maritalStatusOptions = customOptions ?: listOf(
        DataItem(
            1,
            key = "single",
            name = stringResource(
                id = if (isFemale) R.string.single_female else R.string.single_male
            )
        ),
        DataItem(
            2,
            key = "married",
            name = stringResource(
                id = if (isFemale) R.string.married_female else R.string.married_male
            )
        ),
    )
    DefaultDropDown(
        modifier = modifier,
        title = title,
        placeholderText = placeholderText,
        selectedItem = selectedItem,
        isEnabled = isEnabled,
        isRequired = isRequired,
        items = maritalStatusOptions,
        errorText = errorText,
        leadingIcon = leadingIcon,
        onItemSelected = onItemSelected,
    )
}
/**
 * Gender dropdown with configurable options.
 *
 * @param customOptions Custom list of gender options to override defaults
 */
@Composable
fun GenderDropDown(
    modifier: Modifier = Modifier,
    title: String,
    placeholderText: String = "",
    selectedItem: DataItem? = null,
    isEnabled: Boolean = true,
    isRequired: Boolean = false,
    errorText: String? = null,
    leadingIcon: (@Composable (() -> Unit))? = null,
    customOptions: List<DataItem>? = null,
    onItemSelected: (DataItem) -> Unit = {}
) {
    val genderOptions = customOptions ?: listOf(
        DataItem(1, key = "male", name = stringResource(id = R.string.male)),
        DataItem(2, key = "female", name = stringResource(id = R.string.female)),
    )
    DefaultDropDown(
        modifier = modifier,
        title = title,
        placeholderText = placeholderText,
        selectedItem = selectedItem,
        isEnabled = isEnabled,
        isRequired = isRequired,
        items = genderOptions,
        errorText = errorText,
        leadingIcon = leadingIcon,
        onItemSelected = onItemSelected,
    )
}
/**
 * Relationship dropdown with gender-based filtering.
 *
 * @param gender Gender for filtering relationship options ("male", "female")
 * @param customOptions Custom list of relationship options to override defaults
 */
@Composable
fun RelationshipDropDown(
    modifier: Modifier = Modifier,
    title: String,
    gender: String,
    placeholderText: String = "",
    selectedItem: DataItem? = null,
    isEnabled: Boolean = true,
    isRequired: Boolean = false,
    errorText: String? = null,
    leadingIcon: (@Composable (() -> Unit))? = null,
    customOptions: List<DataItem>? = null,
    onItemSelected: (DataItem) -> Unit = {}
) {
    @Composable
    fun relationshipOptions(): List<DataItem> {
        val list = customOptions ?: listOf(
            DataItem(1, key = "husband", name = stringResource(id = R.string.relationship_husband)),
            DataItem(2, key = "wife", name = stringResource(id = R.string.relationship_wife)),
            DataItem(
                3,
                key = "grandchild",
                name = stringResource(id = R.string.relationship_grandchild)
            )
        )
        return if (gender == "male") list.filter { it.key != "wife" }
        else if (gender == "female") list.filter { it.key != "husband" }
        else list
    }
    DefaultDropDown(
        modifier = modifier,
        title = title,
        placeholderText = placeholderText,
        selectedItem = selectedItem,
        isEnabled = isEnabled,
        isRequired = isRequired,
        items = relationshipOptions(),
        errorText = errorText,
        leadingIcon = leadingIcon,
        onItemSelected = onItemSelected,
    )
}
@Preview(showBackground = true)
@Composable
fun DefaultDropDownPreview() {
    BaseTheme {
        DefaultDropDown(
            title = "Title",
            placeholderText = "Placeholder",
            isRequired = true,
            items = listOf(
                DataItem(1, "Item 1", null, "key1"),
                DataItem(2, "Item 2", null, "key2")
            ),
            errorText = "This field is required"
        )
    }
}
data class DropDownItem(
    val id: String,
    val label: String,
    val value: Any? = null
)
@Composable
fun MultiSelectDropDown(
    modifier: Modifier = Modifier,
    title: String = "",
    items: List<DataItem>,
    selectedItems: List<DataItem>,
    onSelectionChange: (List<DataItem>) -> Unit,
    placeholder: String = "",
    enabled: Boolean = true,
    showChips: Boolean = true,
    maxChipsToShow: Int? = null,
    errorText: String? = null
) {
    var expanded by remember { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "dropdown_rotation"
    )
    var currentError by remember { mutableStateOf(errorText) }
    LaunchedEffect(errorText) {
        currentError = errorText
    }
    Column(modifier = modifier) {
        DefaultText(
            modifier = Modifier.padding(5.dp),
            text = title,
            textColor = MaterialTheme.colorScheme.onSurface,
            textStyle = MaterialTheme.typography.titleLarge.copy(
                fontSize = 14.sp
            )
        )
        OutlinedTextField(
            value = if (showChips || selectedItems.isEmpty()) {
                if (selectedItems.isEmpty()) placeholder else ""
            } else {
                "${selectedItems.size} items selected"
            },
            onValueChange = {},
            readOnly = true,
            enabled = enabled,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Dropdown Arrow",
                    modifier = Modifier.rotate(rotationAngle)
                )
            },
            colors = OutlinedTextFieldDefaults.colors().copy(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                errorContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
                disabledIndicatorColor = MaterialTheme.colorScheme.outline,
                errorIndicatorColor = MaterialTheme.colorScheme.error
            ),
            isError = (currentError != null), supportingText = if (currentError != null) {
                {
                    DefaultText(
                        text = currentError.toString(),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 12.sp, color = MaterialTheme.colorScheme.error
                        ),
                        textColor = MaterialTheme.colorScheme.error
                    )
                }
            } else {
                null
            },
            modifier = Modifier
                .fillMaxWidth()
                .noRippleClickable(enabled = enabled) { expanded = !expanded }
        )
        // Show selected items as chips
        if (showChips && selectedItems.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val itemsToShow =
                    if (maxChipsToShow != null) selectedItems.take(maxChipsToShow) else selectedItems
                itemsToShow.forEach { item ->
                    SelectedChip(
                        label = item.name ?: "",
                        onRemove = {
                            onSelectionChange(selectedItems.filter { it.id != item.id })
                        }
                    )
                }
                if (selectedItems.size > (maxChipsToShow ?: 0)) {
                    AssistChip(
                        onClick = { },
                        label = { Text("+${selectedItems.size - (maxChipsToShow ?: 0)} more") },
                        enabled = false
                    )
                }
            }
        }
        // Dropdown Menu
        if (expanded) {
            Popup(
                alignment = Alignment.TopStart,
                properties = PopupProperties(
                    focusable = true,
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                ),
                onDismissRequest = { expanded = false }
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                        .heightIn(max = 300.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column {
                        // Select All / Clear All buttons
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextButton(
                                onClick = {
                                    onSelectionChange(items)
                                }
                            ) {
                                Text("Select All")
                            }
                            TextButton(
                                onClick = {
                                    onSelectionChange(emptyList())
                                }
                            ) {
                                Text("Clear All")
                            }
                        }
                        HorizontalDivider()
                        // Items list
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(items) { item ->
                                val isSelected = selectedItems.any { it.id == item.id }
                                DropDownMenuItem(
                                    item = item,
                                    isSelected = isSelected,
                                    onToggle = {
                                        val newSelection = if (isSelected) {
                                            selectedItems.filter { it.id != item.id }
                                        } else {
                                            selectedItems + item
                                        }
                                        onSelectionChange(newSelection)
                                    }
                                )
                            }
                        }
                        // Done button
                        HorizontalDivider()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = { expanded = false }
                            ) {
                                Text("Done")
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
private fun DropDownMenuItem(
    item: DataItem,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable { onToggle() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = { onToggle() }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = item.name ?: "",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
    }
}
@Composable
private fun SelectedChip(
    label: String,
    onRemove: () -> Unit
) {
    AssistChip(
        onClick = { },
        label = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = label,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Remove",
                    modifier = Modifier
                        .size(16.dp)
                        .noRippleClickable { onRemove() }
                )
            }
        }
    )
}
// Usage Example
@Preview(showBackground = true)
@Composable
fun MultiSelectDropDownExample() {
    val availableItems = remember {
        listOf(
            DataItem(1, "Kotlin"),
            DataItem(2, "Java"),
            DataItem(3, "Swift"),
            DataItem(4, "Dart"),
            DataItem(5, "TypeScript"),
            DataItem(6, "Python"),
            DataItem(7, "Go"),
            DataItem(8, "Rust")
        )
    }
    var selectedItems by remember { mutableStateOf<List<DataItem>>(emptyList()) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MultiSelectDropDown(
            title = "Programming Languages",
            items = availableItems,
            selectedItems = selectedItems,
            onSelectionChange = { selectedItems = it },
            placeholder = "Select languages...",
            showChips = true,
            maxChipsToShow = 3
        )
        if (selectedItems.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Selected Items:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    selectedItems.forEach { item ->
                        Text("• ${item.name}")
                    }
                }
            }
        }
    }
}
