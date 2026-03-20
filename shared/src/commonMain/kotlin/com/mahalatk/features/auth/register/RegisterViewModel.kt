package com.mahalatk.features.auth.register

import com.mahalatk.ui.base.BaseViewModel
import com.mahalatk.ui.managers.LoadingManager
import com.mahalatk.ui.managers.MessageManager
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.passwords_do_not_match
import mahalatk.shared.generated.resources.please_enter_confirm_password
import mahalatk.shared.generated.resources.please_enter_email
import mahalatk.shared.generated.resources.please_enter_name
import mahalatk.shared.generated.resources.please_enter_password
import mahalatk.shared.generated.resources.please_enter_phone_number
import mahalatk.shared.generated.resources.please_enter_valid_email

class RegisterViewModel(
    loadingManager: LoadingManager,
    messageManager: MessageManager,
) : BaseViewModel<RegisterState>(
    RegisterState(),
    loadingManager,
    messageManager,
) {

    fun register() {
        // Clear previous errors
        updateState {
            copy(
                nameError = null,
                mobileError = null,
                emailError = null,
                passwordError = null,
                confirmPasswordError = null,
            )
        }

        // Validate all fields at once
        val state = uiState.value
        var hasError = false

        if (state.name.isBlank()) {
            updateState { copy(nameError = Res.string.please_enter_name) }
            hasError = true
        }

        if (state.mobile.isBlank() || state.mobile.length < 9) {
            updateState { copy(mobileError = Res.string.please_enter_phone_number) }
            hasError = true
        }

        if (state.email.isBlank()) {
            updateState { copy(emailError = Res.string.please_enter_email) }
            hasError = true
        } else if (!state.email.contains("@") || !state.email.contains(".")) {
            updateState { copy(emailError = Res.string.please_enter_valid_email) }
            hasError = true
        }

        if (state.password.isBlank() || state.password.length < 8) {
            updateState { copy(passwordError = Res.string.please_enter_password) }
            hasError = true
        }

        if (state.confirmPassword.isBlank()) {
            updateState { copy(confirmPasswordError = Res.string.please_enter_confirm_password) }
            hasError = true
        } else if (state.password != state.confirmPassword) {
            updateState { copy(confirmPasswordError = Res.string.passwords_do_not_match) }
            hasError = true
        }

        if (hasError) return

        // TODO: Call register use case
    }
}
