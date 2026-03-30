package com.mahalatk.navigation

sealed interface Route {

    // ─── Auth ──────────────────────────
    data object Splash : Route
    data object Login : Route
    data object Register : Route
    data object PickLocation : Route
    data class Activation(val phoneNumber: String, val isFromForgotPassword: Boolean = false) :
        Route
    data object ForgotPassword : Route
    data class ResetPassword(val phoneNumber: String) : Route

    // ─── Main Tabs ─────────────────────
    data object Home : Route
    data object Products : Route
    data object Orders : Route
    data object Chat : Route
    data object Account : Route

    // ─── Detail Screens ────────────────
    data class ChatDetail(val chatId: String, val customerName: String) : Route
    data class OrderDetail(val orderId: String) : Route
    data object Notifications : Route
    data object AddProduct : Route
    data object ShopOwnerProfile : Route
    data object EmployeeProfile : Route
    data object Settings : Route
}

/** True for screens that show the auth background image. */
val Route.isAuthScreen: Boolean
    get() = when (this) {
        is Route.Splash, is Route.Login, is Route.Register,
        is Route.PickLocation, is Route.ForgotPassword,
        is Route.ResetPassword -> true

        else -> false
    }
