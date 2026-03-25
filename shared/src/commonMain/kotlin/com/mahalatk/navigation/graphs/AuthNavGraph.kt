package com.mahalatk.navigation.graphs

import androidx.compose.runtime.Composable
import com.mahalatk.features.auth.login.LoginScreen
import com.mahalatk.features.auth.register.LocationResultHolder
import com.mahalatk.features.auth.register.PickLocationScreen
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
        )

        is Route.Register -> RegisterScreen(
            onNavigateToLogin = { navigation.pop() },
            onNavigateToPickLocation = { navigation.push(Route.PickLocation) },
        )

        is Route.PickLocation -> PickLocationScreen(
            onBackWithResult = { lat, lng, address ->
                LocationResultHolder.setResult(lat, lng, address)
                navigation.pop()
            },
            onBack = { navigation.pop() },
        )

        else -> error("Unexpected route in AuthNavGraph: $route")
    }
}
