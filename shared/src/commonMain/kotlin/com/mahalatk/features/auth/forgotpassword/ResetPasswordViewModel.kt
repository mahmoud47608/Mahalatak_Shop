package com.mahalatk.features.auth.forgotpassword

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.passwords_do_not_match
import mahalatk.shared.generated.resources.please_enter_password
import org.jetbrains.compose.resources.StringResource

@Immutable
data class ResetPasswordState(
    val password: String = "",
    val confirmPassword: String = "",
    val passwordVisible: Boolean = false,
    val confirmPasswordVisible: Boolean = false,
    val passwordError: StringResource? = null,
    val confirmPasswordError: StringResource? = null,
    val isSuccess: Boolean = false,
)

class ResetPasswordViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ResetPasswordState())
    val uiState: StateFlow<ResetPasswordState> = _uiState.asStateFlow()

    fun onPasswordChanged(value: String) {
        _uiState.update { it.copy(password = value, passwordError = null) }
    }

    fun onConfirmPasswordChanged(value: String) {
        _uiState.update { it.copy(confirmPassword = value, confirmPasswordError = null) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.update { it.copy(confirmPasswordVisible = !it.confirmPasswordVisible) }
    }

    fun submit(): Boolean {
        _uiState.update { it.copy(passwordError = null, confirmPasswordError = null) }
        val state = _uiState.value
        var hasError = false

        if (state.password.isBlank() || state.password.length < 8) {
            _uiState.update { it.copy(passwordError = Res.string.please_enter_password) }
            hasError = true
        }
        if (state.confirmPassword.isBlank()) {
            _uiState.update { it.copy(confirmPasswordError = Res.string.please_enter_password) }
            hasError = true
        } else if (state.password != state.confirmPassword) {
            _uiState.update { it.copy(confirmPasswordError = Res.string.passwords_do_not_match) }
            hasError = true
        }

        if (!hasError) {
            _uiState.update { it.copy(isSuccess = true) }
        }
        return !hasError
    }
}
