package com.mahalatk.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.mahalatk.features.auth.activation.ActivationScreen
import com.mahalatk.features.auth.forgotpassword.ForgotPasswordScreen
import com.mahalatk.features.auth.forgotpassword.ResetPasswordScreen
import com.mahalatk.features.auth.login.LoginScreen
import com.mahalatk.features.auth.register.LocationResultHolder
import com.mahalatk.features.auth.register.PickLocationScreen
import com.mahalatk.features.auth.register.RegisterScreen
import com.mahalatk.features.chat.ChatDetailScreen
import com.mahalatk.features.notifications.NotificationsScreen
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
                        onNavigateToForgotPassword = { navigator.push(Route.ForgotPassword) },
                    )
                }

                is Route.Register -> NavEntry(route) {
                    RegisterScreen(
                        onNavigateToLogin = { navigator.pop() },
                        onNavigateToPickLocation = { navigator.push(Route.PickLocation) },
                        onNavigateToActivation = { phone ->
                            navigator.push(Route.Activation(phone))
                        },
                    )
                }

                is Route.Activation -> NavEntry(route) {
                    if (route.isFromForgotPassword) {
                        // Forgot password flow → go to ResetPassword
                        ActivationScreen(
                            phoneNumber = route.phoneNumber,
                            onVerified = {
                                navigator.replace(Route.ResetPassword(route.phoneNumber))
                            },
                        )
                    } else {
                        // Registration flow → show success then go Home
                        ActivationScreen(
                            phoneNumber = route.phoneNumber,
                            showSuccessOnVerify = true,
                            successMessage = "Registration request sent successfully!",
                            onVerified = { navigator.replaceAll(Route.Home) },
                        )
                    }
                }

                // ─── Forgot Password Flow ────────────────
                is Route.ForgotPassword -> NavEntry(route) {
                    ForgotPasswordScreen(
                        onSendCode = { phone ->
                            navigator.push(
                                Route.Activation(phone, isFromForgotPassword = true)
                            )
                        },
                    )
                }

                is Route.ResetPassword -> NavEntry(route) {
                    ResetPasswordScreen(
                        onSuccess = { navigator.replaceAll(Route.Login) },
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

                // ─── Notifications ───────────────────────
                is Route.Notifications -> NavEntry(route) {
                    NotificationsScreen(onBack = { navigator.pop() })
                }

                // ─── Chat Detail ─────────────────────────
                is Route.ChatDetail -> NavEntry(route) {
                    ChatDetailScreen(
                        chatId = route.chatId,
                        customerName = route.customerName,
                        onBack = { navigator.pop() },
                    )
                }

                // ─── Main Tabs ───────────────────────────
                is Route.Home, is Route.Products, is Route.Orders, is Route.Chat,
                is Route.Account -> NavEntry(route) {
                    MainNavGraph(route = route)
                }
            }
        }
    )
}
