package com.aait.base.ui.navigation

sealed class NavigationEvent {
    data object NavigateToLogin : NavigationEvent()
    data object NavigateToHome : NavigationEvent()
    data class NavigateToChat(val roomId: Int, val title: String?) : NavigationEvent()
}
