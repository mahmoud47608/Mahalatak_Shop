package com.mahalatk.navigation

sealed interface Route {

    // ─── Auth ──────────────────────────
    data object Splash : Route
    data object Login : Route
    data object Register : Route
    data object PickLocation : Route
    data class Activation(
        val phoneNumber: String,
        val isFromForgotPassword: Boolean = false,
        val isFromChangePhone: Boolean = false,
        val isFromNewPhone: Boolean = false,
    ) : Route
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
    data object Profile : Route
    data object ShopOwnerProfile : Route
    data object EmployeeProfile : Route
    data object ChangePhone : Route
    data object NewPhone : Route
    data object EditShopOwnerProfile : Route
    data object EditEmployeeProfile : Route
    data object Complaints : Route
    data object Coupons : Route
    data object AddCoupon : Route
    data object Offers : Route
    data object AddOffer : Route
    data object MyRatings : Route
    data object Employees : Route
    data object EmployeeRequests : Route
    data object EmployeesList : Route
    data object AddEmployee : Route
    data object ChangePassword : Route
    data object Settings : Route
    data object About : Route
    data object Terms : Route
    data object PrivacyPolicy : Route
}

// ─── Route Helpers ──────────────────────

/** True for the 5 bottom-nav tab routes. */
val Route.isTabRoute: Boolean
    get() = this is Route.Home || this is Route.Products ||
            this is Route.Orders || this is Route.Chat || this is Route.Account

/** True for screens that show the auth background image. */
val Route.isAuthScreen: Boolean
    get() = when (this) {
        is Route.Splash, is Route.Login, is Route.Register,
        is Route.PickLocation, is Route.ForgotPassword,
        is Route.ResetPassword -> true

        is Route.Activation -> !isFromChangePhone && !isFromNewPhone

        else -> false
    }
