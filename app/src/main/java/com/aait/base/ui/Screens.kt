package com.aait.base.ui

import kotlinx.serialization.Serializable

sealed class Screens {
    @Serializable
    object Splash : Screens()

    @Serializable
    object Login : Screens()
}