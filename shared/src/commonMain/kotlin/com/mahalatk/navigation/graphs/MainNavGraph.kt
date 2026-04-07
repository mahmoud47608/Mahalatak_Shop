package com.mahalatk.navigation.graphs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.mahalatk.features.chat.ChatScreen
import com.mahalatk.features.home.HomeScreen
import com.mahalatk.features.more.MoreScreen
import com.mahalatk.features.orders.OrdersScreen
import com.mahalatk.features.products.ProductsScreen
import com.mahalatk.navigation.LocalNavigator
import com.mahalatk.navigation.Route

/**
 * Hosts the 5 main tab screens.
 *
 * **Key behavior:**
 * - Each tab is composed once on first visit, then stays alive in the tree.
 * - Switching tabs only toggles visibility (alpha 0/1) — no destroy/recreate.
 * - ViewModels, scroll positions, and all state survive across tab switches.
 */
@Composable
fun MainNavGraph() {
    val navigator = LocalNavigator.current
    val currentTab = navigator.currentTab

    // Track visited tabs — lazy compose on first visit
    val visited = remember { mutableStateMapOf("home" to true) }
    visited[currentTab.tabKey] = true

    Box(modifier = Modifier.padding(bottom = 64.dp)) {
        key("home") {
            TabSlot(visible = currentTab is Route.Home) {
                HomeScreen(
                    onOrderClick = { id -> navigator.push(Route.OrderDetail(id)) },
                )
            }
        }

        if (visited["products"] == true) {
            key("products") {
                TabSlot(visible = currentTab is Route.Products) {
                    ProductsScreen(
                        onAddProduct = { navigator.push(Route.AddProduct) },
                    )
                }
            }
        }

        if (visited["orders"] == true) {
            key("orders") {
                TabSlot(visible = currentTab is Route.Orders) {
                    OrdersScreen(
                        onOrderClick = { id -> navigator.push(Route.OrderDetail(id)) },
                    )
                }
            }
        }

        if (visited["chat"] == true) {
            key("chat") {
                TabSlot(visible = currentTab is Route.Chat) {
                    ChatScreen()
                }
            }
        }

        if (visited["account"] == true) {
            key("account") {
                TabSlot(visible = currentTab is Route.Account) {
                    MoreScreen()
                }
            }
        }
    }
}

// ─── Helpers ────────────────────────────────────────

/**
 * Hides content without removing it from the composition tree.
 *
 * Hidden tabs are moved far off-screen via [offset] so they:
 * 1. Cannot receive any touch events (outside hit-test bounds).
 * 2. Stay composed — ViewModels, scroll positions, all state preserved.
 * 3. No alpha/zIndex overlap issues.
 */
@Composable
private fun TabSlot(visible: Boolean, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(if (visible) 1f else 0f)
            .graphicsLayer {
                alpha = if (visible) 1f else 0f
                // Move hidden tabs far off-screen so they can't receive touches
                translationX = if (visible) 0f else 99999f
            },
    ) {
        content()
    }
}

/** Stable key for each tab route. */
private val Route.tabKey: String
    get() = when (this) {
        is Route.Home -> "home"
        is Route.Products -> "products"
        is Route.Orders -> "orders"
        is Route.Chat -> "chat"
        is Route.Account -> "account"
        else -> "home"
    }
