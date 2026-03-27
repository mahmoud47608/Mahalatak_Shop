package com.mahalatk.features.auth.forgotpassword

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.please_enter_phone_number
import org.jetbrains.compose.resources.StringResource

@Immutable
data class ForgotPasswordState(
    val phone: String = "",
    val phoneError: StringResource? = null,
)

class ForgotPasswordViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordState())
    val uiState: StateFlow<ForgotPasswordState> = _uiState.asStateFlow()

    fun onPhoneChanged(value: String) {
        _uiState.update { it.copy(phone = value, phoneError = null) }
    }

    /** Returns true if validation passes. */
    fun validate(): Boolean {
        val state = _uiState.value
        if (state.phone.isBlank() || state.phone.length < 9) {
            _uiState.update { it.copy(phoneError = Res.string.please_enter_phone_number) }
            return false
        }
        return true
    }
}
