package com.mahalatk.features.orders

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahalatk.common.component.header.ScreenHeader
import com.mahalatk.common.component.tabs.FilterTabs
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.CornerDimensions
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.completed_tab
import mahalatk.shared.generated.resources.currency
import mahalatk.shared.generated.resources.current_tab
import mahalatk.shared.generated.resources.ic_check_circle
import mahalatk.shared.generated.resources.my_orders
import mahalatk.shared.generated.resources.new_tab
import mahalatk.shared.generated.resources.no_orders
import mahalatk.shared.generated.resources.returns_tab
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OrdersScreen(viewModel: OrdersViewModel = koinViewModel()) {
    val state by viewModel.uiState.collectAsState()
    val filteredOrders by remember { derivedStateOf { state.filteredOrders } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.ScreenBackground),
    ) {
        ScreenHeader(title = stringResource(Res.string.my_orders))

        Spacer(modifier = Modifier.height(16.dp))

        FilterTabs(
            tabs = listOf(
                OrderTab.New to stringResource(Res.string.new_tab),
                OrderTab.Current to stringResource(Res.string.current_tab),
                OrderTab.Completed to stringResource(Res.string.completed_tab),
                OrderTab.Returns to stringResource(Res.string.returns_tab),
            ),
            selectedTab = state.selectedTab,
            onTabSelected = viewModel::selectTab,
            modifier = Modifier.padding(horizontal = 20.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (filteredOrders.isEmpty()) {
            EmptyOrdersPlaceholder()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(filteredOrders, key = { it.id }) { order ->
                    OrderCard(order = order)
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
private fun EmptyOrdersPlaceholder() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(Res.drawable.ic_check_circle),
                contentDescription = null,
                tint = AppColor.TextHint.copy(alpha = 0.4f),
                modifier = Modifier.size(64.dp),
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(Res.string.no_orders),
                style = MahalatkTheme.bodyLarge,
                color = AppColor.TextHint,
            )
        }
    }
}

@Composable
private fun OrderCard(order: Order) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(CornerDimensions.lg),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier.size(48.dp).clip(CircleShape)
                        .background(AppColor.Secondary.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = order.customerName.take(1).uppercase(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColor.Primary,
                    )
                }

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
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(AppColor.Outline))
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
    val (bgColor, textColor, label) = when (status) {
        OrderStatus.New -> Triple(AppColor.Primary.copy(alpha = 0.1f), AppColor.Primary, "New")
        OrderStatus.Preparing -> Triple(
            AppColor.Warning.copy(alpha = 0.15f),
            Color(0xFFE6A700),
            "Preparing"
        )

        OrderStatus.Delivered -> Triple(
            AppColor.Success.copy(alpha = 0.1f),
            AppColor.Success,
            "Delivered"
        )

        OrderStatus.Returned -> Triple(
            AppColor.Error.copy(alpha = 0.1f),
            AppColor.Error,
            "Returned"
        )

        OrderStatus.Cancelled -> Triple(
            AppColor.Error.copy(alpha = 0.1f),
            AppColor.Error,
            "Cancelled"
        )
    }

    Box(
        modifier = Modifier
            .background(color = bgColor, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 5.dp),
    ) {
        Text(
            text = label,
            style = MahalatkTheme.labelSmall,
            color = textColor,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
