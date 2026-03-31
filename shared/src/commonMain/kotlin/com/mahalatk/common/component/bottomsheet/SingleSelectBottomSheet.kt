package com.mahalatk.common.component.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.utilis.noRippleClickable
import com.mahalatk.theme.MahalatkTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SingleSelectBottomSheet(
    showBottomSheet: Boolean,
    title: String,
    items: List<T>,
    selectedItem: T?,
    itemLabel: @Composable (T) -> String,
    onItemSelected: (T) -> Unit,
    onDismiss: () -> Unit,
    isItemSelected: (T, T?) -> Boolean = { item, selected -> item == selected },
    leadingIcon: (@Composable (T, Boolean) -> Unit)? = null,
) {
    if (!showBottomSheet) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MahalatkTheme.white,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MahalatkTheme.titleMedium,
                color = MahalatkTheme.black,
                textAlign = TextAlign.Center
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(
                    items,
                    key = { it.hashCode() },
                    contentType = { "single_select_item" }) { item ->
                    val isSelected = isItemSelected(item, selectedItem)
                    val backgroundColor =
                        if (isSelected) MahalatkTheme.primary.copy(alpha = 0.08f)
                        else MahalatkTheme.white
                    val borderColor =
                        if (isSelected) MahalatkTheme.primary else MahalatkTheme.border

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(backgroundColor, RoundedCornerShape(12.dp))
                            .border(0.7.dp, borderColor, RoundedCornerShape(12.dp))
                            .noRippleClickable {
                                onItemSelected(item)
                                onDismiss()
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (leadingIcon != null) {
                            leadingIcon(item, isSelected)
                        }
                        Text(
                            text = itemLabel(item),
                            style = MahalatkTheme.bodyMedium,
                            color = if (isSelected) MahalatkTheme.primary else MahalatkTheme.black,
                        )
                    }
                }
            }
        }
    }
}
