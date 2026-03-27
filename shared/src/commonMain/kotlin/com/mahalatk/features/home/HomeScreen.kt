package com.mahalatk.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.CornerDimensions
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.available
import mahalatk.shared.generated.resources.cancelled_orders
import mahalatk.shared.generated.resources.completed_orders
import mahalatk.shared.generated.resources.hello
import mahalatk.shared.generated.resources.ic_cancel_circle
import mahalatk.shared.generated.resources.ic_check_circle
import mahalatk.shared.generated.resources.ic_delivery
import mahalatk.shared.generated.resources.ic_notification
import mahalatk.shared.generated.resources.ic_orders
import mahalatk.shared.generated.resources.ic_profile
import mahalatk.shared.generated.resources.new_orders
import mahalatk.shared.generated.resources.receive_new_orders
import mahalatk.shared.generated.resources.self_delivery
import mahalatk.shared.generated.resources.statistics
import mahalatk.shared.generated.resources.today
import mahalatk.shared.generated.resources.unavailable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel = koinViewModel()) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.ScreenBackground),
    ) {
        // ── Top Header with gradient ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            AppColor.Primary,
                            AppColor.Primary.copy(alpha = 0.85f),
                        ),
                    ),
                    shape = RoundedCornerShape(
                        bottomStart = 28.dp,
                        bottomEnd = 28.dp,
                    ),
                )
                .padding(
                    top = 50.dp,
                    bottom = 24.dp,
                    start = 20.dp,
                    end = 20.dp,
                ),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Notification bell
                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.15f),
                            shape = CircleShape,
                        ),
                ) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_notification),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp),
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Greeting + Name
                Column(
                    horizontalAlignment = Alignment.End,
                ) {
                    Text(
                        text = stringResource(Res.string.hello),
                        style = MahalatkTheme.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f),
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = state.userName,
                        style = MahalatkTheme.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Profile avatar
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center,
                ) {
                    if (state.userImage.isNotEmpty()) {
                        AsyncImage(
                            model = state.userImage,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize().clip(CircleShape),
                            contentScale = ContentScale.Crop,
                        )
                    } else {
                        Image(
                            painter = painterResource(Res.drawable.ic_profile),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp).padding(top = 4.dp),
                        )
                    }
                }
            }
        }

        // ── Scrollable Content ──
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            // ── Toggle Cards ──
            item {
                ToggleCard(
                    icon = Res.drawable.ic_orders,
                    title = stringResource(Res.string.receive_new_orders),
                    subtitle = if (state.receiveNewOrders) stringResource(Res.string.available)
                    else stringResource(Res.string.unavailable),
                    isChecked = state.receiveNewOrders,
                    onToggle = viewModel::toggleReceiveOrders,
                )
            }

            item {
                ToggleCard(
                    icon = Res.drawable.ic_delivery,
                    title = stringResource(Res.string.self_delivery),
                    subtitle = if (state.selfDelivery) stringResource(Res.string.available)
                    else stringResource(Res.string.unavailable),
                    isChecked = state.selfDelivery,
                    onToggle = viewModel::toggleSelfDelivery,
                )
            }

            // ── Statistics Section ──
            item {
                Text(
                    text = stringResource(Res.string.statistics),
                    style = MahalatkTheme.titleMedium,
                    color = MahalatkTheme.black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        count = state.completedOrders,
                        label = stringResource(Res.string.completed_orders),
                        icon = Res.drawable.ic_check_circle,
                        accentColor = AppColor.Success,
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        count = state.cancelledOrders,
                        label = stringResource(Res.string.cancelled_orders),
                        icon = Res.drawable.ic_cancel_circle,
                        accentColor = AppColor.Error,
                    )
                }
            }

            // ── New Orders Section ──
            item {
                Text(
                    text = stringResource(Res.string.new_orders),
                    style = MahalatkTheme.titleMedium,
                    color = MahalatkTheme.black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }

            items(state.newOrders, key = { it.id }) { order ->
                OrderCard(order = order)
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

// ──────────────────────────────────────────────
// Toggle Card Component
// ──────────────────────────────────────────────
@Composable
private fun ToggleCard(
    icon: org.jetbrains.compose.resources.DrawableResource,
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onToggle: (Boolean) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(CornerDimensions.lg),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Icon container
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = AppColor.Primary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = AppColor.Primary,
                    modifier = Modifier.size(22.dp),
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MahalatkTheme.titleSmall,
                    color = AppColor.TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MahalatkTheme.bodySmall,
                    color = if (isChecked) AppColor.Success else AppColor.TextHint,
                )
            }

            Switch(
                checked = isChecked,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = AppColor.Primary,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = AppColor.Gray,
                    uncheckedBorderColor = Color.Transparent,
                    checkedBorderColor = Color.Transparent,
                ),
            )
        }
    }
}

// ──────────────────────────────────────────────
// Stat Card Component
// ──────────────────────────────────────────────
@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    count: Int,
    label: String,
    icon: org.jetbrains.compose.resources.DrawableResource,
    accentColor: Color,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(CornerDimensions.lg),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(22.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = count.toString(),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColor.TextPrimary,
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = label,
                style = MahalatkTheme.bodySmall,
                color = AppColor.TextSecondary,
            )
        }
    }
}

// ──────────────────────────────────────────────
// Order Card Component
// ──────────────────────────────────────────────
@Composable
private fun OrderCard(order: OrderItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(CornerDimensions.lg),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Customer avatar
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
