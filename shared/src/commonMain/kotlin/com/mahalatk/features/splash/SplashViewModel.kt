package com.mahalatk.features.splash

import androidx.lifecycle.viewModelScope
import com.mahalatk.domain.repository.PreferenceRepository
import com.mahalatk.ui.base.BaseViewModel
import com.mahalatk.ui.managers.LoadingManager
import com.mahalatk.ui.managers.MessageManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Checks if user is already logged in → navigate to Home or Login.
 */
class SplashViewModel(
    loadingManager: LoadingManager,
    messageManager: MessageManager,
    private val preferenceRepository: PreferenceRepository,
) : BaseViewModel<Unit>(Unit, loadingManager, messageManager) {

    private val _isLoggedIn = MutableStateFlow<Boolean?>(null) // null = still checking
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn.asStateFlow()

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            val loggedIn = preferenceRepository.getIsLogin().first()
            _isLoggedIn.value = loggedIn
        }
    }
}
