package com.mahalatk.ui.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import com.mahalatk.common.component.bottomsheet.AppLanguage
import com.mahalatk.features.auth.login.LoginScreen
import com.mahalatk.features.auth.register.RegisterScreen
import com.mahalatk.features.splash.SplashScreen

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
            }
        }
    }
}
