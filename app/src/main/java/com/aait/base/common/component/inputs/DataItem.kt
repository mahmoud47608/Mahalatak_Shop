package com.aait.base.common.component.inputs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import com.aait.base.common.component.utilis.noRippleClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aait.domain.entity.general.DataItem

// Assuming your DataItem class looks something like this


/**
 * Compact Multi-Select with inline expansion
 * This version expands inline without a popup, good for forms with limited space
 */
@Composable
fun CompactMultiSelect(
    modifier: Modifier = Modifier,
    title: String = "",
    items: List<DataItem>,
    selectedItems: List<DataItem>,
    onSelectionChange: (List<DataItem>) -> Unit,
    placeholder: String = "Select items...",
    errorText: String? = null,
    maxHeight: Int = 200
) {
    var expanded by remember { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "dropdown_rotation"
    )

    Column(modifier = modifier) {
        // Title
        if (title.isNotEmpty()) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        // Main customClickable area
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .noRippleClickable { expanded = !expanded },
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(
                width = 1.dp,
                color = if (errorText != null) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.outline
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when {
                        selectedItems.isEmpty() -> placeholder
                        selectedItems.size == 1 -> selectedItems.first().name ?: ""
                        else -> "${selectedItems.size} items selected"
                    },
                    color = if (selectedItems.isEmpty())
                        MaterialTheme.colorScheme.onSurfaceVariant
                    else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.rotate(rotationAngle)
                )
            }
        }

        // Error text
        if (errorText != null) {
            Text(
                text = errorText,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        // Expanded content
        AnimatedVisibility(visible = expanded) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .heightIn(max = maxHeight.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column {
                    // Quick actions
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TextButton(
                            onClick = { onSelectionChange(items) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Rounded.Add,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("All", fontSize = 12.sp)
                        }
                        TextButton(
                            onClick = { onSelectionChange(emptyList()) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("None", fontSize = 12.sp)
                        }
                        TextButton(
                            onClick = { expanded = false },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Default.Done,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Done", fontSize = 12.sp)
                        }
                    }
                    HorizontalDivider()

                    // Items
                    LazyColumn {
                        items(items) { item ->
                            val isSelected = selectedItems.any { it.id == item.id }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .noRippleClickable {
                                        val newSelection = if (isSelected) {
                                            selectedItems.filter { it.id != item.id }
                                        } else {
                                            selectedItems + item
                                        }
                                        onSelectionChange(newSelection)
                                    }
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isSelected,
                                    onCheckedChange = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = item.name ?: "",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Tag-based Multi-Select
 * Shows selected items as tags/chips and allows adding/removing with a modal
 */
@Composable
fun TagMultiSelect(
    modifier: Modifier = Modifier,
    title: String = "",
    items: List<DataItem>,
    selectedItems: List<DataItem>,
    onSelectionChange: (List<DataItem>) -> Unit,
    placeholder: String = "Click to add items",
    errorText: String? = null
) {
    var showDialog by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        // Title
        if (title.isNotEmpty()) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Tag container
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(
                width = 1.dp,
                color = if (errorText != null) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.outline
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 56.dp)
                    .padding(8.dp)
            ) {
                if (selectedItems.isEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .noRippleClickable { showDialog = true }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = placeholder,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        selectedItems.forEach { item ->
                            InputChip(
                                selected = true,
                                onClick = { },
                                label = { Text(item.name ?: "", fontSize = 14.sp) },
                                trailingIcon = {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Remove",
                                        modifier = Modifier
                                            .size(18.dp)
                                            .noRippleClickable {
                                                onSelectionChange(selectedItems.filter { it.id != item.id })
                                            }
                                    )
                                },
                                modifier = Modifier.height(32.dp)
                            )
                        }
                        // Add more button
                        AssistChip(
                            onClick = { showDialog = true },
                            label = {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            modifier = Modifier.height(32.dp)
                        )
                    }
                }
            }
        }

        // Error text
        if (errorText != null) {
            Text(
                text = errorText,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }

    // Selection dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Select Items") },
            text = {
                LazyColumn {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            TextButton(onClick = { onSelectionChange(items) }) {
                                Text("Select All")
                            }
                            TextButton(onClick = { onSelectionChange(emptyList()) }) {
                                Text("Clear All")
                            }
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    }
                    items(items) { item ->
                        val isSelected = selectedItems.any { it.id == item.id }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .noRippleClickable {
                                    val newSelection = if (isSelected) {
                                        selectedItems.filter { it.id != item.id }
                                    } else {
                                        selectedItems + item
                                    }
                                    onSelectionChange(newSelection)
                                }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = item.name ?: "")
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Done")
                }
            }
        )
    }
}

/**
 * Example usage
 */
@Composable
fun MultiSelectExamples() {
    val items = remember {
        listOf(
            DataItem(1, "Option 1"),
            DataItem(2, "Option 2"),
            DataItem(3, "Option 3"),
            DataItem(4, "Option 4"),
            DataItem(5, "Option 5")
        )
    }

    var selectedItemsCompact by remember { mutableStateOf<List<DataItem>>(emptyList()) }
    var selectedItemsTags by remember { mutableStateOf<List<DataItem>>(emptyList()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Compact version
        CompactMultiSelect(
            title = "Compact Multi-Select",
            items = items,
            selectedItems = selectedItemsCompact,
            onSelectionChange = { selectedItemsCompact = it },
            placeholder = "Choose options..."
        )

        // Tag-based version
        TagMultiSelect(
            title = "Tag-based Multi-Select",
            items = items,
            selectedItems = selectedItemsTags,
            onSelectionChange = { selectedItemsTags = it },
            placeholder = "Click to add tags"
        )
    }
}