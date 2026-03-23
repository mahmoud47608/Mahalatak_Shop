package com.mahalatk.navigation.graphs

import androidx.compose.runtime.Composable
import com.mahalatk.common.component.bottomsheet.AppLanguage
import com.mahalatk.features.auth.login.LoginScreen
import com.mahalatk.features.auth.register.RegisterScreen
import com.mahalatk.features.splash.SplashScreen
import com.mahalatk.navigation.LocalNavigator
import com.mahalatk.navigation.Route

/**
 * Auth screens routing.
 * Takes lambdas (not navigator) → decoupled, testable, reusable.
 */
@Composable
fun AuthNavGraph(
    route: Route,
    onLanguageChanged: (AppLanguage) -> Unit,
) {
    val navigation = LocalNavigator.current

    when (route) {
        is Route.Splash -> SplashScreen(
            onNavigateToLogin = { navigation.replaceAll(Route.Login) },
            onNavigateToHome = { navigation.replaceAll(Route.Home) },
        )

        is Route.Login -> LoginScreen(
            onNavigateToHome = { navigation.replaceAll(Route.Home) },
            onNavigateToSignUp = { navigation.push(Route.Register) },
            onLanguageChanged = onLanguageChanged,
        )

        is Route.Register -> RegisterScreen(
            onNavigateToLogin = { navigation.pop() },
            onLanguageChanged = onLanguageChanged,
        )

        else -> error("Unexpected route in AuthNavGraph: $route")
    }
}
