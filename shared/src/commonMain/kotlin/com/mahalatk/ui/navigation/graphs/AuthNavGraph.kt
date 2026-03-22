package com.mahalatk.ui.navigation.graphs

import androidx.compose.runtime.Composable
import com.mahalatk.common.component.bottomsheet.AppLanguage
import com.mahalatk.features.auth.login.LoginScreen
import com.mahalatk.features.auth.register.RegisterScreen
import com.mahalatk.features.splash.SplashScreen
import com.mahalatk.ui.navigation.AppNavigator
import com.mahalatk.ui.navigation.Route

@Composable
fun AuthNavGraph(
    route: Route,
    navigator: AppNavigator,
    onLanguageChanged: (AppLanguage) -> Unit,
) {
    when (route) {
        is Route.Splash -> SplashScreen(
            onNavigateToLogin = { navigator.replaceAll(Route.Login) },
        )

        is Route.Login -> LoginScreen(
            onNavigateToHome = { navigator.replaceAll(Route.Home) },
            onNavigateToSignUp = { navigator.push(Route.Register) },
            onLanguageChanged = onLanguageChanged,
        )

        is Route.Register -> RegisterScreen(
            onNavigateToLogin = { navigator.pop() },
            onLanguageChanged = onLanguageChanged,
        )

        else -> Unit
    }
}
