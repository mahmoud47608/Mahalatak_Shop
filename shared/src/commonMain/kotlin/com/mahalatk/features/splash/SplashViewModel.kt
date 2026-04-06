package com.mahalatk.features.splash

import androidx.lifecycle.viewModelScope
import com.mahalatk.base.BaseViewModel
import com.mahalatk.base.managers.LoadingManager
import com.mahalatk.base.managers.MessageManager
import com.mahalatk.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed interface SplashEvent {
    data object NavigateToHome : SplashEvent
    data object NavigateToLogin : SplashEvent
}

class SplashViewModel(
    loadingManager: LoadingManager,
    messageManager: MessageManager,
    private val preferenceRepository: PreferenceRepository,
) : BaseViewModel<Unit, SplashEvent>(Unit, loadingManager, messageManager) {

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            val loggedIn = preferenceRepository.getIsLogin().first()
            if (loggedIn) {
                sendEvent(SplashEvent.NavigateToHome)
            } else {
                sendEvent(SplashEvent.NavigateToLogin)
            }
        }
    }
}
