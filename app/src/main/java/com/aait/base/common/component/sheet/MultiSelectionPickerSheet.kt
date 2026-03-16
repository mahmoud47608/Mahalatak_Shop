package com.aait.base.common.component.sheet

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.aait.base.common.component.button.DefaultButton
import com.aait.base.common.component.inputs.DefaultTextField
import com.aait.base.ui.theme.ExtendedTheme
import com.aait.base.ui.theme.PaddingDimensions
import com.aait.base.ui.theme.SpacingDimensions
import com.aait.domain.entity.general.DataItem
import com.mahalatak.R
import kotlinx.coroutines.launch

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiSelectionPickerSheet(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.select_items),
    list: List<DataItem>,
    selectedItems: List<DataItem>,
    onItemsSelected: (List<DataItem>) -> Unit,
    onDismiss: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }

    val filteredList = remember(searchQuery, list) {
        if (searchQuery.isEmpty()) list
        else list.filter { it.name?.contains(searchQuery, ignoreCase = true) == true }
    }

    val selectedIds = remember {
        mutableStateListOf<Int>().apply {
            addAll(selectedItems.map { it.id ?: -1 })
        }
    }

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = null,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = LocalConfiguration.current.screenHeightDp.dp * 0.75f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = ExtendedTheme.colors.textPrimary,
                modifier = Modifier
                    .padding(all = PaddingDimensions.medium)
                    .padding(top = PaddingDimensions.low, bottom = PaddingDimensions.medium)
                    .align(Alignment.CenterHorizontally)
            )

            Row(
                modifier = Modifier
                    .padding(horizontal = PaddingDimensions.medium)
                    .padding(top = PaddingDimensions.low),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DefaultTextField(
                    value = searchQuery,
                    onValueChanged = { searchQuery = it },
                    placeholderText = stringResource(R.string.search),
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    modifier = Modifier
                        .padding(horizontal = PaddingDimensions.medium)
                        .padding(top = PaddingDimensions.low, bottom = PaddingDimensions.medium)
                )

            }


            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(
                    horizontal = PaddingDimensions.medium,
                    vertical = 8.dp
                ),
                verticalArrangement = Arrangement.spacedBy(PaddingDimensions.small)
            ) {
                if (filteredList.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                            )
                            Spacer(modifier = Modifier.height(PaddingDimensions.medium))
                            Text(
                                text = stringResource(R.string.no_items_found_matching, searchQuery),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                } else {
                    items(filteredList.size) { index ->
                        val item = filteredList[index]
                        val isSelected = selectedIds.contains(item.id)

                        SelectorItem(
                            item = item,
                            isSelected = isSelected,
                            onItemSelected = {
                                if (isSelected) {
                                    selectedIds.remove(item.id)
                                } else {
                                    item.id?.let {
                                        selectedIds.add(it)
                                    }
                                }
                            }
                        )
                    }
                }
            }

            // Sticky Footer Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = PaddingDimensions.medium)
                    .padding(bottom = PaddingDimensions.medium)
            ) {
                DefaultButton(
                    text = stringResource(R.string.confirm),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        val updatedList = list.filter { selectedIds.contains(it.id) }
                        onItemsSelected(updatedList)
                        coroutineScope.launch {
                            onDismiss()
                        }
                    }
                )
                Spacer(modifier = Modifier.height(SpacingDimensions.sp4)) // Extra padding for system bars
            }
        }
    }
}
