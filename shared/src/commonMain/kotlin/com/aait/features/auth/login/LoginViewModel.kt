package com.aait.features.auth.login

import androidx.lifecycle.viewModelScope
import com.aait.domain.entity.AuthData
import com.aait.domain.exceptions.ValidationException
import com.aait.domain.repository.PreferenceRepository
import com.aait.domain.usecase.auth.LoginUseCase
import com.aait.domain.util.DataState
import com.aait.domain.util.TokenCacheManager
import com.aait.ui.base.SessionAwareViewModel
import com.aait.ui.managers.LoadingManager
import com.aait.ui.managers.MessageManager
import com.aait.ui.managers.SessionManager
import com.aait.ui.util.applyCommonSideEffects
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    loadingManager: LoadingManager,
    messageManager: MessageManager,
    sessionManager: SessionManager,
    preferenceRepository: PreferenceRepository,
    tokenCacheManager: TokenCacheManager,
) : SessionAwareViewModel<LoginState>(
    LoginState(),
    loadingManager,
    messageManager,
    sessionManager,
    preferenceRepository,
    tokenCacheManager
) {

    private val _authData = MutableStateFlow<AuthData?>(null)
    val authData: StateFlow<AuthData?> = _authData.asStateFlow()

    fun login() {
        viewModelScope.launch {
            val deviceId = preferenceRepository.getFirebaseToken().first()
            loginUseCase(
                countryCode = "966",
                phone = uiState.value.mobile,
                password = uiState.value.password,
                deviceId = deviceId,
                socialId = null
            ).collectLatest {
                when (it) {
                    is DataState.Error -> {
                        when (it.throwable) {
                            is ValidationException.InValidPhoneException ->
                                messageManager.showByKey("please_enter_phone_number")

                            is ValidationException.InValidPasswordException ->
                                messageManager.showByKey("please_enter_password")

                            else -> it.applyCommonSideEffects<AuthData>(
                                this@LoginViewModel,
                                showSuccessToast = true
                            )
                        }
                    }

                    else -> {
                        it.applyCommonSideEffects(
                            this@LoginViewModel,
                            showLoading = true
                        ) { response ->
                            response.data?.user?.token?.let { token ->
                                viewModelScope.launch {
                                    preferenceRepository.setToken(token)
                                    preferenceRepository.setIsLogin(true)
                                    tokenCacheManager.refreshTokenCache()
                                }
                            }
                            _authData.value = response.data
                        }
                    }
                }
            }
        }
    }
}
