package com.aait.base

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aait.base.ui.UIRepo
import com.aait.base.ui.navigation.ChatNavKey
import com.aait.base.ui.navigation.HomeNavKey
import com.aait.base.ui.navigation.LoginNavKey
import com.aait.base.ui.navigation.NavScreen
import com.aait.base.ui.navigation.NavigationEvent
import com.aait.base.ui.navigation.SplashNavKey
import com.aait.data.util.TokenHeaderProvider
import com.aait.domain.repository.PreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val uiRepo: UIRepo,
    private val tokenHeaderProvider: TokenHeaderProvider,
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    init {
        refreshTokenCache()
    }

    val navigationStack = mutableStateListOf<NavScreen>(SplashNavKey)

    val isLoading = uiRepo.isLoading
    val uiMessages = uiRepo.uiMessage


    // ==================== Auth & Security State ====================

    /**
     * Tracks authentication failure state (e.g., 401 from API)
     * When true, triggers session expired dialog in MainActivity
     */
    private val _isAuthFailed = MutableStateFlow(false)
    val isAuthFailed: StateFlow<Boolean> = _isAuthFailed.asStateFlow()

    /**
     * Tracks account blocking state (e.g., account banned/suspended)
     * When true, triggers account blocked dialog in MainActivity
     */
    private val _isBlocked = MutableStateFlow(false)
    val isBlocked: StateFlow<Boolean> = _isBlocked.asStateFlow()

    fun onAuthFailed(failed: Boolean) {
        uiRepo.setAuthFailed(failed)
    }

    fun onBlocked(blocked: Boolean) {
        uiRepo.setBlocked(blocked)
    }

    fun onLogout() {
        viewModelScope.launch {
            preferenceRepository.onLogout()
            tokenHeaderProvider.removeToken()
            uiRepo.setAuthFailed(false)
            uiRepo.setBlocked(false)
            navigationStack.clear()
            navigationStack.add(LoginNavKey)
        }
    }

    fun getLanguage(): Flow<String> = flow {
        emitAll(preferenceRepository.getLanguage())
    }

    fun refreshTokenCache() {
        tokenHeaderProvider.refreshTokenCache()
    }

    fun handleNavigationEvent(event: NavigationEvent) {
        when (event) {
            is NavigationEvent.NavigateToLogin -> {
                navigationStack.clear()
                navigationStack.add(LoginNavKey)
            }

            is NavigationEvent.NavigateToHome -> {
                navigationStack.clear()
                navigationStack.add(HomeNavKey)
            }

            is NavigationEvent.NavigateToChat -> {
                navigationStack.add(
                    ChatNavKey(roomId = event.roomId, title = event.title)
                )
            }
        }
    }

}
