package com.mahalatk.features.auth.forgotpassword

import androidx.compose.runtime.Immutable
import com.mahalatk.base.SimpleViewModel
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

class ResetPasswordViewModel : SimpleViewModel<ResetPasswordState, Nothing>(ResetPasswordState()) {

    fun onPasswordChanged(value: String) {
        updateState { copy(password = value, passwordError = null) }
    }

    fun onConfirmPasswordChanged(value: String) {
        updateState { copy(confirmPassword = value, confirmPasswordError = null) }
    }

    fun togglePasswordVisibility() {
        updateState { copy(passwordVisible = !passwordVisible) }
    }

    fun toggleConfirmPasswordVisibility() {
        updateState { copy(confirmPasswordVisible = !confirmPasswordVisible) }
    }

    fun submit(): Boolean {
        updateState { copy(passwordError = null, confirmPasswordError = null) }
        val state = uiState.value
        var hasError = false

        if (state.password.isBlank() || state.password.length < 8) {
            updateState { copy(passwordError = Res.string.please_enter_password) }
            hasError = true
        }
        if (state.confirmPassword.isBlank()) {
            updateState { copy(confirmPasswordError = Res.string.please_enter_password) }
            hasError = true
        } else if (state.password != state.confirmPassword) {
            updateState { copy(confirmPasswordError = Res.string.passwords_do_not_match) }
            hasError = true
        }

        if (!hasError) {
            updateState { copy(isSuccess = true) }
        }
        return !hasError
    }
}
