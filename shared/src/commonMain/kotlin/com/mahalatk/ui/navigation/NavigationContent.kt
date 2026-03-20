package com.mahalatk.ui.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mahalatk.common.component.bottomsheet.AppLanguage
import com.mahalatk.features.auth.login.LoginScreen
import com.mahalatk.features.auth.register.RegisterScreen
import com.mahalatk.features.splash.SplashScreen

@Composable
fun NavigationContent(
    modifier: Modifier = Modifier,
    currentRoute: Route?,
    navigator: AppNavigator,
    onLanguageChanged: (AppLanguage) -> Unit = {},
) {
    Crossfade(targetState = currentRoute, modifier = modifier) { route ->
        when (route) {
            is Route.Splash -> SplashScreen(
                onNavigateToLogin = { navigator.replaceAll(Route.Login) }
            )

            is Route.Login -> LoginScreen(
                onNavigateToHome = { navigator.replaceAll(Route.Home) },
                onNavigateToSignUp = { navigator.push(Route.Register) },
                onLanguageChanged = onLanguageChanged
            )

            is Route.Register -> RegisterScreen(
                onNavigateToLogin = { navigator.pop() },
                onLanguageChanged = onLanguageChanged
            )

            is Route.Home -> {
                // TODO: HomeScreen
            }

            is Route.More -> {
                // TODO: MoreScreen
            }

            null -> {}
        }
    }
}
