package com.mahalatk.navigation.graphs

import androidx.compose.runtime.Composable
import com.mahalatk.features.chat.ChatScreen
import com.mahalatk.features.home.HomeScreen
import com.mahalatk.features.more.MoreScreen
import com.mahalatk.features.orders.OrdersScreen
import com.mahalatk.features.products.ProductsScreen
import com.mahalatk.navigation.Route

/**
 * Main screens routing (bottom nav tabs).
 * No navigator needed - tabs are switched from AppBottomBar directly.
 */
@Composable
fun MainNavGraph(route: Route) {
    when (route) {
        is Route.Home -> HomeScreen()
        is Route.Products -> ProductsScreen()
        is Route.Orders -> OrdersScreen()
        is Route.Chat -> ChatScreen()
        is Route.Account -> MoreScreen()
        else -> HomeScreen()
    }
}
