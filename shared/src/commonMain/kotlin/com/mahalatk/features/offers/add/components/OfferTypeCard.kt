package com.mahalatk.features.offers.add.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.animation.AnimatedListItem
import com.mahalatk.common.component.utilis.noRippleClickable
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.CornerDimensions
import com.mahalatk.theme.MahalatkTheme

@Composable
fun OfferTypeCard(
    index: Int,
    icon: ImageVector,
    iconColor: Color,
    title: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val containerBg by animateColorAsState(
        targetValue = if (isSelected) AppColor.PrimaryContainer else AppColor.Surface,
        animationSpec = tween(250),
    )

    val iconBgAlpha by animateFloatAsState(
        targetValue = if (isSelected) 0.18f else 0.1f,
        animationSpec = tween(250),
    )

    val titleColor by animateColorAsState(
        targetValue = if (isSelected) AppColor.OnPrimaryContainer else AppColor.TextPrimary,
        animationSpec = tween(250),
    )

    AnimatedListItem(index = index) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .noRippleClickable { onClick() },
            shape = RoundedCornerShape(CornerDimensions.lg),
            colors = CardDefaults.cardColors(containerColor = containerBg),
            elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 0.dp else 1.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(CornerDimensions.md))
                        .background(iconColor.copy(alpha = iconBgAlpha)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(22.dp),
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                // Text
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MahalatkTheme.titleSmall,
                        color = titleColor,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = description,
                        style = MahalatkTheme.bodySmall,
                        color = AppColor.TextHint,
                    )
                }

                // Check badge
                AnimatedVisibility(
                    visible = isSelected,
                    enter = scaleIn(spring(dampingRatio = Spring.DampingRatioMediumBouncy)) + fadeIn(),
                    exit = scaleOut() + fadeOut(),
                ) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(AppColor.Primary),
                        contentAlignment = Alignment.Center,
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
        }
    }
}
