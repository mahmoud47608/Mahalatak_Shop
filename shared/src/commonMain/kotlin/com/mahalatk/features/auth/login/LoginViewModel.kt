package com.mahalatk.features.auth.login

import androidx.lifecycle.viewModelScope
import com.mahalatk.domain.entity.AuthData
import com.mahalatk.domain.exceptions.ValidationException
import com.mahalatk.domain.repository.PreferenceRepository
import com.mahalatk.domain.usecase.auth.LoginUseCase
import com.mahalatk.domain.util.DataState
import com.mahalatk.domain.util.TokenCacheManager
import com.mahalatk.ui.base.SessionAwareViewModel
import com.mahalatk.ui.managers.LoadingManager
import com.mahalatk.ui.managers.MessageManager
import com.mahalatk.ui.managers.SessionManager
import com.mahalatk.ui.util.applyCommonSideEffects
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
        updateState { copy(mobileError = null, passwordError = null) }
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
                        when (val error = it.throwable) {
                            is ValidationException.MultipleValidationException -> {
                                error.errors.forEach { validationError ->
                                    when (validationError) {
                                        is ValidationException.InValidPhoneException ->
                                            updateState { copy(mobileError = "please_enter_phone_number") }

                                        is ValidationException.InValidPasswordException ->
                                            updateState { copy(passwordError = "please_enter_password") }

                                        else -> {}
                                    }
                                }
                            }

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
