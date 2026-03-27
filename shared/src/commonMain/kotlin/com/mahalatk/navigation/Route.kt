package com.mahalatk.navigation

sealed interface Route {

    data object Splash : Route

    data object Login : Route

    data object Register : Route

    data object PickLocation : Route

    data object Home : Route

    data object Products : Route

    data object Orders : Route

    data object Chat : Route

    data class ChatDetail(val chatId: String, val customerName: String) : Route

    data class Activation(val phoneNumber: String, val isFromForgotPassword: Boolean = false) :
        Route

    data object ForgotPassword : Route

    data class ResetPassword(val phoneNumber: String) : Route

    data object Notifications : Route

    data object Account : Route

    data object More : Route
}
