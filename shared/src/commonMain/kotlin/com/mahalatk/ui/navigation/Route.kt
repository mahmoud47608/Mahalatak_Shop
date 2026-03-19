package com.mahalatk.ui.navigation

import com.mahalatk.domain.entity.general.LatLngModel

sealed interface Route {

    data object Splash : Route

    data object Login : Route

    data object Home : Route

    data object More : Route

    data class PickLocation(val latLng: LatLngModel?) : Route

    data class Chat(val roomId: Int, val title: String? = null) : Route
}
