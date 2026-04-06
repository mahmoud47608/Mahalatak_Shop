package com.mahalatk.features.auth.forgotpassword

import androidx.compose.runtime.Immutable
import com.mahalatk.base.SimpleViewModel
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.please_enter_phone_number
import org.jetbrains.compose.resources.StringResource

@Immutable
data class ForgotPasswordState(
    val phone: String = "",
    val phoneError: StringResource? = null,
)

class ForgotPasswordViewModel :
    SimpleViewModel<ForgotPasswordState, Nothing>(ForgotPasswordState()) {

    fun onPhoneChanged(value: String) {
        updateState { copy(phone = value, phoneError = null) }
    }

    fun validate(): Boolean {
        val state = uiState.value
        if (state.phone.isBlank() || state.phone.length < 9) {
            updateState { copy(phoneError = Res.string.please_enter_phone_number) }
            return false
        }
        return true
    }
}
