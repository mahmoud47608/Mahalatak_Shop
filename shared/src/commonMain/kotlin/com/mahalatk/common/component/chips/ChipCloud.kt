package com.mahalatk.common.component.chips

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.utilis.noRippleClickable
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.MahalatkTheme

/**
 * A flow-row of selectable chips.
 * Selected = Primary bg + white text, Unselected = outline + hint text.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T> ChipCloud(
    items: List<T>,
    selectedItems: Set<T>,
    label: @Composable (T) -> String,
    onToggle: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items.forEach { item ->
            val isSelected = item in selectedItems

            val bgColor by animateColorAsState(
                targetValue = if (isSelected) AppColor.Primary else Color.Transparent,
                animationSpec = tween(200),
            )
            val textColor by animateColorAsState(
                targetValue = if (isSelected) Color.White else AppColor.TextHint,
                animationSpec = tween(200),
            )
            val borderColor by animateColorAsState(
                targetValue = if (isSelected) AppColor.Primary else AppColor.Border,
                animationSpec = tween(200),
            )
            val scale by animateFloatAsState(
                targetValue = if (isSelected) 1.05f else 1f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
            )

            Text(
                text = label(item),
                style = MahalatkTheme.bodySmall,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = textColor,
                modifier = Modifier
                    .graphicsLayer { scaleX = scale; scaleY = scale }
                    .clip(RoundedCornerShape(20.dp))
                    .background(bgColor)
                    .border(1.dp, borderColor, RoundedCornerShape(20.dp))
                    .noRippleClickable { onToggle(item) }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }
    }
}
