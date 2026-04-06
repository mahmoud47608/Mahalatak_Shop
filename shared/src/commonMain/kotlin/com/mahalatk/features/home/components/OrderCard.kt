package com.mahalatk.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.mahalatk.common.component.card.GlassCard
import com.mahalatk.common.component.utilis.noRippleClickable
import com.mahalatk.features.home.OrderItem
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.CornerDimensions
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.today
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun OrderCard(order: OrderItem, onClick: () -> Unit = {}) {
    GlassCard(
        modifier = Modifier.fillMaxWidth().noRippleClickable { onClick() },
        cornerRadius = CornerDimensions.lg,
        contentPadding = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(AppColor.Secondary.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center,
            ) {
                if (order.imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = order.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Text(
                        text = order.customerName.take(1).uppercase(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColor.Primary,
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = order.customerName,
                    style = MahalatkTheme.titleSmall,
                    color = AppColor.TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "#${order.orderNumber}",
                    style = MahalatkTheme.bodySmall,
                    color = AppColor.TextHint,
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = order.time,
                    style = MahalatkTheme.bodySmall,
                    color = AppColor.TextSecondary,
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = stringResource(Res.string.today),
                    style = MahalatkTheme.labelSmall,
                    color = AppColor.Primary,
                )
            }
        }
    }
}
