package com.aait.base.cycles.auth.login

import androidx.lifecycle.viewModelScope
import com.aait.base.base.BaseViewModel
import com.aait.base.util.applyCommonSideEffects
import com.aait.domain.entity.AuthData
import com.aait.domain.exceptions.ValidationException
import com.aait.domain.usecase.auth.LoginUseCase
import com.aait.domain.util.DataState
import com.mahalatak.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : BaseViewModel<LoginState>(LoginState()) {

    private val _authData = MutableStateFlow<AuthData?>(null)
    val authData: StateFlow<AuthData?> = _authData.asStateFlow()

    fun login() {
        getFirebaseToken { deviceId ->
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
                                    uiRepo.showMsg(R.string.please_enter_phone_number)

                                is ValidationException.InValidPasswordException ->
                                    uiRepo.showMsg(R.string.please_enter_password)

                                else -> it.applyCommonSideEffects(
                                    this@LoginViewModel,
                                    showSuccessToast = true
                                )
                            }
                        }

                        else -> {
                            it.applyCommonSideEffects(this@LoginViewModel, showLoading = true) { response ->
                                // Save token securely (automatically encrypted)
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
}
