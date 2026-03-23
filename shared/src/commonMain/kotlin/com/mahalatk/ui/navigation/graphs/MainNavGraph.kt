package com.mahalatk.ui.navigation.graphs

import androidx.compose.runtime.Composable
import com.mahalatk.features.home.HomeScreen
import com.mahalatk.features.more.MoreScreen
import com.mahalatk.features.parts.PartsScreen
import com.mahalatk.ui.navigation.Route

/**
 * Main screens routing (bottom nav tabs).
 * No navigator needed - tabs are switched from AppBottomBar directly.
 */
@Composable
fun MainNavGraph(route: Route) {
    when (route) {
        is Route.Home -> HomeScreen()
        is Route.Parts -> PartsScreen()
        is Route.More -> MoreScreen()
        else -> Unit
    }
}
