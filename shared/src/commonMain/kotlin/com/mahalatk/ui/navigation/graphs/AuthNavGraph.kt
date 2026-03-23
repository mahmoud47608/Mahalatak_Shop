package com.mahalatk.ui.navigation.graphs

import androidx.compose.runtime.Composable
import com.mahalatk.common.component.bottomsheet.AppLanguage
import com.mahalatk.features.auth.login.LoginScreen
import com.mahalatk.features.auth.register.RegisterScreen
import com.mahalatk.features.splash.SplashScreen
import com.mahalatk.ui.navigation.Route

/**
 * Auth screens routing.
 * Takes lambdas (not navigator) → decoupled, testable, reusable.
 */
@Composable
fun AuthNavGraph(
    route: Route,
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onBack: () -> Unit,
    onLanguageChanged: (AppLanguage) -> Unit,
) {
    when (route) {
        is Route.Splash -> SplashScreen(
            onNavigateToLogin = onNavigateToLogin,
            onNavigateToHome = onNavigateToHome,
        )

        is Route.Login -> LoginScreen(
            onNavigateToHome = onNavigateToHome,
            onNavigateToSignUp = onNavigateToRegister,
            onLanguageChanged = onLanguageChanged,
        )

        is Route.Register -> RegisterScreen(
            onNavigateToLogin = onBack,
            onLanguageChanged = onLanguageChanged,
        )

        else -> Unit
    }
}
