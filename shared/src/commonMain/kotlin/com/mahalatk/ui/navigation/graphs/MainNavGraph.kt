package com.mahalatk.ui.navigation.graphs

import androidx.compose.runtime.Composable
import com.mahalatk.features.home.HomeScreen
import com.mahalatk.features.more.MoreScreen
import com.mahalatk.features.parts.PartsScreen
import com.mahalatk.ui.navigation.AppNavigator
import com.mahalatk.ui.navigation.Route

@Composable
fun MainNavGraph(
    route: Route,
    navigator: AppNavigator,
) {
    when (route) {
        is Route.Home -> HomeScreen()

        is Route.Parts -> PartsScreen()

        is Route.More -> MoreScreen()

        else -> Unit
    }
}
