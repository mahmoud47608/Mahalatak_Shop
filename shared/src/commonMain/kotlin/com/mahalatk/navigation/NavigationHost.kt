package com.mahalatk.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.mahalatk.features.auth.login.LoginScreen
import com.mahalatk.features.auth.register.LocationResultHolder
import com.mahalatk.features.auth.register.PickLocationScreen
import com.mahalatk.features.auth.register.RegisterScreen
import com.mahalatk.features.chat.ChatDetailScreen
import com.mahalatk.features.splash.SplashScreen
import com.mahalatk.navigation.graphs.MainNavGraph

@Composable
fun NavigationHost() {
    val navigator = LocalNavigator.current

    NavDisplay(
        backStack = navigator.backStack,
        onBack = { navigator.pop() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = { route ->
            when (route) {
                // ─── Auth ────────────────────────────────
                is Route.Splash -> NavEntry(route) {
                    SplashScreen(
                        onNavigateToLogin = { navigator.replaceAll(Route.Login) },
                        onNavigateToHome = { navigator.replaceAll(Route.Home) },
                    )
                }

                is Route.Login -> NavEntry(route) {
                    LoginScreen(
                        onNavigateToHome = { navigator.replaceAll(Route.Home) },
                        onNavigateToSignUp = { navigator.push(Route.Register) },
                    )
                }

                is Route.Register -> NavEntry(route) {
                    RegisterScreen(
                        onNavigateToLogin = { navigator.pop() },
                        onNavigateToPickLocation = { navigator.push(Route.PickLocation) },
                    )
                }

                is Route.PickLocation -> NavEntry(route) {
                    PickLocationScreen(
                        onBackWithResult = { lat, lng, address ->
                            LocationResultHolder.setResult(lat, lng, address)
                            navigator.pop()
                        },
                        onBack = { navigator.pop() },
                    )
                }

                // ─── Chat Detail ──────────────────────────
                is Route.ChatDetail -> NavEntry(route) {
                    ChatDetailScreen(
                        chatId = route.chatId,
                        customerName = route.customerName,
                        onBack = { navigator.pop() },
                    )
                }

                // ─── Main ────────────────────────────────
                is Route.Home, is Route.Products, is Route.Orders, is Route.Chat, is Route.Account,
                is Route.More -> NavEntry(route) {
                    MainNavGraph(route = route)
                }
            }
        }
    )
}
