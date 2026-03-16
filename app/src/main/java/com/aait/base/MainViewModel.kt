package com.aait.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aait.data.util.TokenHeaderProvider
import com.aait.domain.repository.PreferenceRepository
import com.aait.ui.ui.UIRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainViewModel(
    private val uiRepo: UIRepo,
    private val tokenHeaderProvider: TokenHeaderProvider,
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    init {
        refreshTokenCache()
    }

    val isLoading = uiRepo.isLoading
    val uiMessages = uiRepo.uiMessage

    // ==================== Auth & Security State ====================

    private val _isAuthFailed = MutableStateFlow(false)
    val isAuthFailed: StateFlow<Boolean> = _isAuthFailed.asStateFlow()

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
        }
    }

    fun getLanguage(): Flow<String> = flow {
        emitAll(preferenceRepository.getLanguage())
    }

    fun refreshTokenCache() {
        tokenHeaderProvider.refreshTokenCache()
    }
}
