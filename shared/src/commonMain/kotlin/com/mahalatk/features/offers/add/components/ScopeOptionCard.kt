package com.mahalatk.features.offers.add.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.animation.AnimatedListItem
import com.mahalatk.common.component.card.GlassCard
import com.mahalatk.common.component.utilis.noRippleClickable
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.CornerDimensions
import com.mahalatk.theme.MahalatkTheme

@Composable
fun ScopeOptionCard(
    index: Int,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {

    val titleColor by animateColorAsState(
        targetValue = if (isSelected) AppColor.OnPrimaryContainer else AppColor.TextPrimary,
        animationSpec = tween(250),
    )

    AnimatedListItem(index = index) {
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .noRippleClickable { onClick() },
            cornerRadius = CornerDimensions.md,
            contentPadding = 0.dp,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioDot(isSelected = isSelected)

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = title,
                    style = MahalatkTheme.titleSmall,
                    color = titleColor,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                )
            }
        }
    }
}

@Composable
private fun RadioDot(isSelected: Boolean) {
    val outerColor by animateColorAsState(
        targetValue = if (isSelected) AppColor.Primary else AppColor.Border,
        animationSpec = tween(200),
    )

    val innerScale by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
    )

    Box(
        modifier = Modifier
            .size(20.dp)
            .clip(CircleShape)
            .background(if (isSelected) AppColor.Primary else Color.Transparent)
            .border(width = 2.dp, color = outerColor, shape = CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .graphicsLayer { scaleX = innerScale; scaleY = innerScale }
                .clip(CircleShape)
                .background(Color.White),
        )
    }
}
