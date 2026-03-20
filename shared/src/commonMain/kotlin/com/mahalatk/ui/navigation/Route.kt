package com.mahalatk.ui.navigation

import com.mahalatk.domain.entity.general.LatLngModel

sealed interface Route {

    data object Splash : Route

    data object Login : Route

    data object Register : Route

    data object Home : Route

    data object More : Route
}
