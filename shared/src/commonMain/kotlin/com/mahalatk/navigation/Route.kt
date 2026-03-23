package com.mahalatk.navigation

sealed interface Route {

    data object Splash : Route

    data object Login : Route

    data object Register : Route

    data object Home : Route

    data object Parts : Route

    data object More : Route
}
