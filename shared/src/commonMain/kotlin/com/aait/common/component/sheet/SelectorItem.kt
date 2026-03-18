package com.aait.common.component.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aait.common.component.utilis.noRippleClickable
import com.aait.domain.entity.general.DataItem
import com.aait.ui.theme.CornerDimensions
import com.aait.ui.theme.ExtendedTheme
import com.aait.ui.theme.IconDimensions
import com.aait.ui.theme.PaddingDimensions

@Composable
fun SelectorItem(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    item: DataItem,
    onItemSelected: () -> Unit
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(CornerDimensions.sm))
            .background(if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.05f) else Color.Transparent)
            .border(
                width = 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(CornerDimensions.sm)
            )
            .noRippleClickable {
                onItemSelected()
            }
            .padding(PaddingDimensions.medium)
    ) {
        Text(
            text = item.name ?: "",
            style = MaterialTheme.typography.bodyMedium,
            color = ExtendedTheme.colors.textPrimary,
            modifier = Modifier.weight(1f)
        )

        Box(
            modifier = Modifier
                .size(IconDimensions.large)
                .clip(CircleShape)
                .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                .border(
                    1.dp,
                    if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(IconDimensions.small)
                )
            }
        }
    }
}
