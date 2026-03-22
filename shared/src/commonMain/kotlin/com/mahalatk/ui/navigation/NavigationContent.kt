package com.mahalatk.ui.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import com.mahalatk.common.component.bottomsheet.AppLanguage
import com.mahalatk.ui.navigation.graphs.AuthNavGraph
import com.mahalatk.ui.navigation.graphs.MainNavGraph

@Composable
fun NavigationContent(
    modifier: Modifier = Modifier,
    currentEntry: NavEntry?,
    navigator: AppNavigator,
    onLanguageChanged: (AppLanguage) -> Unit = {},
) {
    Crossfade(targetState = currentEntry, modifier = modifier) { entry ->
        if (entry == null) return@Crossfade
        key(entry.id) {
            when (entry.route) {
                is Route.Splash, is Route.Login, is Route.Register -> {
                    AuthNavGraph(
                        route = entry.route,
                        navigator = navigator,
                        onLanguageChanged = onLanguageChanged,
                    )
                }

                is Route.Home, is Route.Parts, is Route.More -> {
                    MainNavGraph(
                        route = entry.route,
                        navigator = navigator,
                    )
                }
            }
        }
    }
}
