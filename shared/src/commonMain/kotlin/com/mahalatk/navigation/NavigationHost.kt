package com.mahalatk.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import com.mahalatk.navigation.graphs.AuthNavGraph
import com.mahalatk.navigation.graphs.MainNavGraph

@Composable
fun NavigationHost(changeLanguage: (String) -> Unit) {
    AnimatedContent(
        targetState = LocalNavigator.current.currentRoute,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "NavigationHost",
    ) { route ->
        when (route) {
            is Route.Splash, is Route.Login, is Route.Register, is Route.PickLocation -> {
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

