package com.mahalatk.common.component.tabs

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.utilis.noRippleClickable
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.MahalatkTheme

/**
 * Reusable animated tab filter (e.g. Active/Closed, New/Current/Completed/Returns).
 */
@Composable
fun <T> FilterTabs(
    tabs: List<Pair<T, String>>,
    selectedTab: T,
    onTabSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Stable reference so the lambda passed to noRippleClickable doesn't change
    val currentOnTabSelected = rememberUpdatedState(onTabSelected)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = AppColor.Surface, shape = RoundedCornerShape(12.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        tabs.forEach { (tab, label) ->
            val isSelected = tab == selectedTab
            val textColor by animateColorAsState(
                targetValue = if (isSelected) Color.White else AppColor.TextHint,
                label = "tabText",
            )

            val tabShape = RoundedCornerShape(12.dp)
            val stableOnClick = remember(tab) { { currentOnTabSelected.value(tab) } }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(tabShape)
                    .then(
                        if (isSelected) Modifier
                            .background(
                                brush = Brush.verticalGradient(
                                    listOf(
                                        AppColor.Primary,
                                        AppColor.Primary.copy(alpha = 0.85f),
                                    )
                                ),
                                shape = tabShape,
                            )
                            .border(
                                0.5.dp,
                                AppColor.Primary.copy(alpha = 0.3f),
                                tabShape,
                            )
                        else Modifier
                    )
                    .noRippleClickable(onClick = stableOnClick)
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = label,
                    style = MahalatkTheme.labelMedium,
                    color = textColor,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
