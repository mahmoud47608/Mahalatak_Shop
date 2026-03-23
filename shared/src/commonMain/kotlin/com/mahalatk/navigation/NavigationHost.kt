package com.mahalatk.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import com.mahalatk.navigation.graphs.AuthNavGraph
import com.mahalatk.navigation.graphs.MainNavGraph

@Composable
fun NavigationHost(changeLanguage: (String) -> Unit) {
    Crossfade(targetState = LocalNavigator.current.currentRoute) { route ->
        when (route) {
            is Route.Splash, is Route.Login, is Route.Register -> {
                AuthNavGraph(
                    route = route,
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

