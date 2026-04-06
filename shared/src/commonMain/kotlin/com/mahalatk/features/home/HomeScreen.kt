package com.mahalatk.features.home

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.animation.AnimatedListItem
import com.mahalatk.common.component.image.UserAvatar
import com.mahalatk.features.home.components.OrderCard
import com.mahalatk.features.home.components.StatCard
import com.mahalatk.features.home.components.ToggleCard
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.AppShapes
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.available
import mahalatk.shared.generated.resources.cancelled_orders
import mahalatk.shared.generated.resources.completed_orders
import mahalatk.shared.generated.resources.hello
import mahalatk.shared.generated.resources.ic_notification
import mahalatk.shared.generated.resources.ic_orders
import mahalatk.shared.generated.resources.new_orders
import mahalatk.shared.generated.resources.receive_new_orders
import mahalatk.shared.generated.resources.statistics
import mahalatk.shared.generated.resources.unavailable
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onOrderClick: (String) -> Unit = {},
) {
    val state by viewModel.uiState.collectAsState()
    val navigator = com.mahalatk.navigation.LocalNavigator.current

    val glassColors = AppColor.HeaderGradient

    Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColor.ScreenBackground),
    ) {
        // ── Top Header with glass morphism ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(AppShapes.Header)
                .drawBehind {
                    drawRect(Brush.verticalGradient(glassColors))
                    drawCircle(
                        Color.White.copy(alpha = 0.07f),
                        80.dp.toPx(),
                        Offset(-20.dp.toPx(), -10.dp.toPx()),
                    )
                    drawCircle(
                        Color.White.copy(alpha = 0.05f),
                        55.dp.toPx(),
                        Offset(size.width + 10.dp.toPx(), size.height * 0.4f),
                    )
                    drawCircle(
                        Color.White.copy(alpha = 0.03f),
                        35.dp.toPx(),
                        Offset(size.width * 0.5f, -15.dp.toPx()),
                    )
                }
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
                // Profile avatar
                UserAvatar(
                    imageUrl = state.userImage,
                    size = 48.dp,
                    backgroundColor = Color.White.copy(alpha = 0.2f),
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Greeting + Name
                Column(
                    horizontalAlignment = Alignment.Start,
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

                Spacer(modifier = Modifier.weight(1f))

                // Notification bell
                IconButton(
                    onClick = { navigator.push(com.mahalatk.navigation.Route.Notifications) },
                    modifier = Modifier
                        .size(40.dp)
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
                        accentColor = AppColor.Primary,
                        progress = state.completedProgress,
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        count = state.cancelledOrders,
                        label = stringResource(Res.string.cancelled_orders),
                        accentColor = AppColor.Error,
                        progress = state.cancelledProgress,
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

            itemsIndexed(state.newOrders, key = { _, o -> o.id }) { index, order ->
                AnimatedListItem(index) {
                    OrderCard(order = order, onClick = { onOrderClick(order.id) })
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}
