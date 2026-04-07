package com.mahalatk.features.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.animation.AnimatedListItem
import com.mahalatk.common.component.card.GlassCard
import com.mahalatk.common.component.empty.EmptyStatePlaceholder
import com.mahalatk.common.component.header.ScreenHeader
import com.mahalatk.common.component.image.UserAvatar
import com.mahalatk.common.component.loading.ShimmerBox
import com.mahalatk.common.component.loading.ShimmerCircle
import com.mahalatk.common.component.tabs.FilterTabs
import com.mahalatk.common.component.utilis.noRippleClickable
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.AppShapes
import com.mahalatk.theme.CornerDimensions
import com.mahalatk.theme.MahalatkTheme
import kotlinx.coroutines.launch
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.accepted_tab
import mahalatk.shared.generated.resources.cancelled_tab
import mahalatk.shared.generated.resources.completed_tab
import mahalatk.shared.generated.resources.currency
import mahalatk.shared.generated.resources.current_tab
import mahalatk.shared.generated.resources.ic_check_circle
import mahalatk.shared.generated.resources.my_orders
import mahalatk.shared.generated.resources.no_orders
import mahalatk.shared.generated.resources.order_status_cancelled
import mahalatk.shared.generated.resources.order_status_delivered
import mahalatk.shared.generated.resources.order_status_new
import mahalatk.shared.generated.resources.order_status_preparing
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OrdersScreen(
    viewModel: OrdersViewModel = koinViewModel(),
    onOrderClick: (String) -> Unit = {},
) {
    val state by viewModel.uiState.collectAsState()
    val tabs = OrderTab.entries
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = tabs.indexOf(state.selectedTab).coerceAtLeast(0),
        pageCount = { tabs.size },
    )

    // Sync pager swipes -> viewModel
    LaunchedEffect(pagerState.currentPage) {
        val tab = tabs[pagerState.currentPage]
        if (state.selectedTab != tab) viewModel.selectTab(tab)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.ScreenBackground),
    ) {
        ScreenHeader(title = stringResource(Res.string.my_orders))

        Spacer(modifier = Modifier.height(16.dp))

        FilterTabs(
            tabs = listOf(
                OrderTab.Accepted to stringResource(Res.string.accepted_tab),
                OrderTab.Current to stringResource(Res.string.current_tab),
                OrderTab.Completed to stringResource(Res.string.completed_tab),
                OrderTab.Cancelled to stringResource(Res.string.cancelled_tab),
            ),
            selectedTab = tabs[pagerState.currentPage],
            onTabSelected = { tab ->
                coroutineScope.launch { pagerState.animateScrollToPage(tabs.indexOf(tab)) }
            },
            modifier = Modifier.padding(horizontal = 20.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            val tab = tabs[page]
            val pageOrders = state.orders.filter { order ->
                when (tab) {
                    OrderTab.Accepted -> order.status == OrderStatus.New
                    OrderTab.Current -> order.status == OrderStatus.Preparing
                    OrderTab.Completed -> order.status == OrderStatus.Delivered
                    OrderTab.Cancelled -> order.status == OrderStatus.Returned || order.status == OrderStatus.Cancelled
                }
            }

            if (state.isLoading) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 20.dp,
                        end = 20.dp,
                        top = 4.dp,
                        bottom = 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(4) { index ->
                        AnimatedListItem(index) { OrderCardSkeleton() }
                    }
                }
            } else if (pageOrders.isEmpty()) {
                EmptyStatePlaceholder(
                    icon = Res.drawable.ic_check_circle,
                    message = stringResource(Res.string.no_orders),
                    iconTint = AppColor.TextHint.copy(alpha = 0.4f),
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    itemsIndexed(
                        pageOrders,
                        key = { _, o -> o.id },
                        contentType = { _, _ -> "order" }) { index, order ->
                        AnimatedListItem(index) {
                            OrderCard(
                                order = order,
                                onClick = { onOrderClick(order.id) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderCard(order: Order, onClick: () -> Unit = {}) {
    GlassCard(
        modifier = Modifier.noRippleClickable { onClick() },
        cornerRadius = CornerDimensions.lg,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                UserAvatar(
                    initials = order.customerName,
                    size = 48.dp,
                    backgroundColor = AppColor.Secondary.copy(alpha = 0.3f),
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = order.customerName,
                        style = MahalatkTheme.titleSmall,
                        color = AppColor.TextPrimary,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "#${order.orderNumber}",
                        style = MahalatkTheme.bodySmall,
                        color = AppColor.TextHint,
                    )
                }

                StatusBadge(status = order.status)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(
                        color = Color.LightGray.copy(alpha = if (AppColor.isDark) 0.5f else 1f)
                    )
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "${order.totalPrice.toInt()} ${stringResource(Res.string.currency)}",
                    style = MahalatkTheme.titleSmall,
                    color = AppColor.Primary,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${order.date} • ${order.time}",
                    style = MahalatkTheme.labelSmall,
                    color = AppColor.TextHint,
                )
            }
        }
    }
}

@Composable
private fun StatusBadge(status: OrderStatus) {
    val bgColor = when (status) {
        OrderStatus.New -> AppColor.Primary.copy(alpha = 0.1f)
        OrderStatus.Preparing -> AppColor.Warning.copy(alpha = 0.15f)
        OrderStatus.Delivered -> AppColor.Success.copy(alpha = 0.1f)
        OrderStatus.Returned, OrderStatus.Cancelled -> AppColor.Error.copy(alpha = 0.1f)
    }
    val textColor = when (status) {
        OrderStatus.New -> AppColor.Primary
        OrderStatus.Preparing -> Color(0xFFE6A700)
        OrderStatus.Delivered -> AppColor.Success
        OrderStatus.Returned, OrderStatus.Cancelled -> AppColor.Error
    }
    val label = when (status) {
        OrderStatus.New -> stringResource(Res.string.order_status_new)
        OrderStatus.Preparing -> stringResource(Res.string.order_status_preparing)
        OrderStatus.Delivered -> stringResource(Res.string.order_status_delivered)
        OrderStatus.Returned, OrderStatus.Cancelled -> stringResource(Res.string.order_status_cancelled)
    }

    Box(
        modifier = Modifier
            .background(color = bgColor, shape = AppShapes.Small)
            .border(
                0.5.dp,
                textColor.copy(alpha = 0.15f),
                AppShapes.Small,
            )
            .padding(horizontal = 12.dp, vertical = 4.dp),
    ) {
        Text(
            text = label,
            style = MahalatkTheme.labelSmall,
            color = textColor,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun OrderCardSkeleton() {
    GlassCard(
        cornerRadius = CornerDimensions.lg,
        contentPadding = 16.dp,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ShimmerCircle(size = 48.dp)
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    ShimmerBox(width = 120.dp, height = 14.dp)
                    Spacer(modifier = Modifier.height(8.dp))
                    ShimmerBox(width = 80.dp, height = 10.dp)
                }
                ShimmerBox(width = 60.dp, height = 24.dp, shape = RoundedCornerShape(8.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(
                        color = Color.LightGray.copy(alpha = if (AppColor.isDark) 0.5f else 1f)
                    )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                ShimmerBox(width = 80.dp, height = 14.dp)
                Spacer(modifier = Modifier.weight(1f))
                ShimmerBox(width = 100.dp, height = 10.dp)
            }
        }
    }
}
