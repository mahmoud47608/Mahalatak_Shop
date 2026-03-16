package com.aait.ui.screens.auth.login

import androidx.lifecycle.viewModelScope
import com.aait.domain.entity.AuthData
import com.aait.domain.exceptions.ValidationException
import com.aait.domain.repository.PreferenceRepository
import com.aait.domain.usecase.auth.LoginUseCase
import com.aait.domain.util.DataState
import com.aait.domain.util.FirebaseTokenProvider
import com.aait.domain.util.TokenManager
import com.aait.ui.ui.UIRepo
import com.aait.ui.base.BaseViewModel
import com.aait.ui.util.applyCommonSideEffects
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    uiRepo: UIRepo,
    preferenceRepository: PreferenceRepository,
    tokenManager: TokenManager,
    private val firebaseTokenProvider: FirebaseTokenProvider
) : BaseViewModel<LoginState>(LoginState(), uiRepo, preferenceRepository, tokenManager) {

    private val _authData = MutableStateFlow<AuthData?>(null)
    val authData: StateFlow<AuthData?> = _authData.asStateFlow()

    fun login() {
        viewModelScope.launch {
            val deviceId = firebaseTokenProvider.getToken()
            viewModelScope.launch {
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
                                    uiRepo.showMsg("please_enter_phone_number", isKey = true)

                                is ValidationException.InValidPasswordException ->
                                    uiRepo.showMsg("please_enter_password", isKey = true)

                                else -> it.applyCommonSideEffects(
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
                                // Save token securely (automatically encrypted)
                                response.data?.user?.token?.let { token ->
                                    viewModelScope.launch {
                                        preferenceRepository.setToken(token)
                                        preferenceRepository.setIsLogin(true)
                                        tokenManager.refreshTokenCache()
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
}
