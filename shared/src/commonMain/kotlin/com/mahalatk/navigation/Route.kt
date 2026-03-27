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

    data object Account : Route

    // Legacy – kept so existing references don't break
    data object Parts : Route

    data object More : Route
}
