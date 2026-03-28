package com.mahalatk.navigation.graphs

import androidx.compose.runtime.Composable
import com.mahalatk.features.chat.ChatScreen
import com.mahalatk.features.home.HomeScreen
import com.mahalatk.features.more.MoreScreen
import com.mahalatk.features.orders.OrdersScreen
import com.mahalatk.features.products.ProductsScreen
import com.mahalatk.navigation.LocalNavigator
import com.mahalatk.navigation.Route

/** Main tab screens — only renders the 5 bottom nav destinations. */
@Composable
fun MainNavGraph(route: Route) {
    val navigator = LocalNavigator.current

    when (route) {
        is Route.Home -> HomeScreen(
            onOrderClick = { orderId -> navigator.push(Route.OrderDetail(orderId)) },
        )
        is Route.Products -> ProductsScreen()
        is Route.Orders -> OrdersScreen(
            onOrderClick = { orderId -> navigator.push(Route.OrderDetail(orderId)) },
        )
        is Route.Chat -> ChatScreen()
        is Route.Account -> MoreScreen()
        else -> error("Unexpected route in MainNavGraph: $route")
    }
}
