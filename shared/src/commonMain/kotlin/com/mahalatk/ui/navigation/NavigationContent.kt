package com.mahalatk.ui.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mahalatk.features.auth.login.LoginScreen
import com.mahalatk.features.splash.SplashScreen

@Composable
fun NavigationContent(
    modifier: Modifier = Modifier,
    currentRoute: Route?,
    navigator: AppNavigator,
) {
    Crossfade(targetState = currentRoute, modifier = modifier) { route ->
        when (route) {
            is Route.Splash -> SplashScreen(
                onNavigateToLogin = { navigator.replaceAll(Route.Login) }
            )

            is Route.Login -> LoginScreen(
                onNavigateToHome = { navigator.replaceAll(Route.Home) }
            )

            is Route.Home -> {
                // TODO: HomeScreen
            }

            is Route.More -> {
                // TODO: MoreScreen
            }

            is Route.PickLocation -> {
                PlaceholderScreen("Pick Location")
            }

            is Route.Chat -> {
                PlaceholderScreen("Chat #${route.roomId}${route.title?.let { " - $it" } ?: ""}")
            }

            null -> {}
        }
    }
}

@Composable
private fun PlaceholderScreen(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}
