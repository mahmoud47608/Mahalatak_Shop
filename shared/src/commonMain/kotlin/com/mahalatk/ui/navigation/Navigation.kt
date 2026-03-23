package com.mahalatk.ui.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import com.mahalatk.ui.navigation.graphs.AuthNavGraph
import com.mahalatk.ui.navigation.graphs.MainNavGraph

@Composable
fun Navigation(currentRoute: Route, navigator: AppNavigator, changeLanguage: (String) -> Unit) {
    Crossfade(targetState = currentRoute) { route ->
        when (route) {
            is Route.Splash, is Route.Login, is Route.Register -> {
                AuthNavGraph(
                    route = route,
                    onNavigateToLogin = { navigator.replaceAll(Route.Login) },
                    onNavigateToHome = { navigator.replaceAll(Route.Home) },
                    onNavigateToRegister = { navigator.push(Route.Register) },
                    onBack = { navigator.pop() },
                    onLanguageChanged = { language ->
                        changeLanguage(language.code)
                    },
                )
            }

            is Route.Home, is Route.Parts, is Route.More -> {
                MainNavGraph(route = route)
            }
        }
    }
}

