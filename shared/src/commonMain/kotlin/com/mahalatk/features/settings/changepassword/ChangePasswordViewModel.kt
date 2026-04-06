package com.mahalatk.features.settings.changepassword

import com.mahalatk.base.BaseViewModel
import com.mahalatk.base.managers.LoadingManager
import com.mahalatk.base.managers.MessageManager
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.passwords_do_not_match
import mahalatk.shared.generated.resources.please_enter_confirm_password
import mahalatk.shared.generated.resources.please_enter_password

class ChangePasswordViewModel(
    loadingManager: LoadingManager,
    messageManager: MessageManager,
) : BaseViewModel<ChangePasswordState, Nothing>(
    ChangePasswordState(),
    loadingManager,
    messageManager,
) {

    fun onOldPasswordChanged(value: String) {
        updateState { copy(oldPassword = value, oldPasswordError = null) }
    }

    fun onNewPasswordChanged(value: String) {
        updateState { copy(newPassword = value, newPasswordError = null) }
    }

    fun onConfirmPasswordChanged(value: String) {
        updateState { copy(confirmPassword = value, confirmPasswordError = null) }
    }

    fun validate(): Boolean {
        val state = uiState.value
        var valid = true

        if (state.oldPassword.isBlank()) {
            updateState { copy(oldPasswordError = Res.string.please_enter_password) }
            valid = false
        }
        if (state.newPassword.isBlank()) {
            updateState { copy(newPasswordError = Res.string.please_enter_password) }
            valid = false
        }
        if (state.confirmPassword.isBlank()) {
            updateState { copy(confirmPasswordError = Res.string.please_enter_confirm_password) }
            valid = false
        } else if (state.newPassword != state.confirmPassword) {
            updateState { copy(confirmPasswordError = Res.string.passwords_do_not_match) }
            valid = false
        }

        return valid
    }

    fun changePassword() {
        if (!validate()) return
        // TODO: Add API call to change password
    }
}
