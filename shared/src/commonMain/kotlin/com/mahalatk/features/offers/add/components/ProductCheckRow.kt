package com.mahalatk.features.offers.add.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.utilis.noRippleClickable
import com.mahalatk.features.offers.add.ProductItem
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.CornerDimensions
import com.mahalatk.theme.MahalatkTheme

@Composable
fun ProductCheckRow(
    product: ProductItem,
    isChecked: Boolean,
    onToggle: () -> Unit,
) {
    val rowBg by animateColorAsState(
        targetValue = if (isChecked) AppColor.PrimaryContainer else Color.Transparent,
        animationSpec = tween(200),
    )

    val avatarBg by animateColorAsState(
        targetValue = if (isChecked) AppColor.Primary else AppColor.Primary.copy(alpha = 0.08f),
        animationSpec = tween(250),
    )

    val avatarTextColor by animateColorAsState(
        targetValue = if (isChecked) Color.White else AppColor.Primary,
        animationSpec = tween(250),
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(CornerDimensions.md))
            .background(rowBg)
            .noRippleClickable { onToggle() }
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(CornerDimensions.sm))
                .background(avatarBg),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = product.name.take(1),
                style = MahalatkTheme.labelMedium,
                color = avatarTextColor,
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Name + category
        Text(
            text = product.name,
            style = MahalatkTheme.bodyMedium,
            color = AppColor.TextPrimary,
            fontWeight = if (isChecked) FontWeight.SemiBold else FontWeight.Normal,
            modifier = Modifier.weight(1f),
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Custom check circle
        CheckCircleIndicator(isChecked = isChecked)
    }
}

@Composable
private fun CheckCircleIndicator(isChecked: Boolean) {
    val bgColor by animateColorAsState(
        targetValue = if (isChecked) AppColor.Primary else AppColor.SurfaceContainerHigh,
        animationSpec = tween(200),
    )

    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(bgColor),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedVisibility(
            visible = isChecked,
            enter = scaleIn(spring(dampingRatio = Spring.DampingRatioMediumBouncy)) + fadeIn(),
            exit = scaleOut() + fadeOut(),
        ) {
            Icon(
                imageVector = Icons.Rounded.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp),
            )
        }
    }
}
