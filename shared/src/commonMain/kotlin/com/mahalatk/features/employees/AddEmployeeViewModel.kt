package com.mahalatk.features.employees

import androidx.compose.runtime.Immutable
import com.mahalatk.base.SimpleViewModel
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.field_required
import mahalatk.shared.generated.resources.passwords_do_not_match
import org.jetbrains.compose.resources.StringResource

@Immutable
data class AddEmployeeState(
    val name: String = "",
    val phone: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordVisible: Boolean = false,
    val confirmPasswordVisible: Boolean = false,
    val image: ByteArray? = null,
    val nameError: StringResource? = null,
    val phoneError: StringResource? = null,
    val passwordError: StringResource? = null,
    val confirmPasswordError: StringResource? = null,
    val imageError: StringResource? = null,
)

class AddEmployeeViewModel : SimpleViewModel<AddEmployeeState, Nothing>(AddEmployeeState()) {

    fun onNameChanged(value: String) {
        updateState { copy(name = value, nameError = null) }
    }

    fun onPhoneChanged(value: String) {
        updateState { copy(phone = value, phoneError = null) }
    }

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

    fun validate(): Boolean {
        var valid = true
        val state = uiState.value

        if (state.name.isBlank()) {
            updateState { copy(nameError = Res.string.field_required) }
            valid = false
        }
        if (state.phone.isBlank() || state.phone.length < 9) {
            updateState { copy(phoneError = Res.string.field_required) }
            valid = false
        }
        if (state.password.isBlank()) {
            updateState { copy(passwordError = Res.string.field_required) }
            valid = false
        }
        if (state.confirmPassword.isBlank()) {
            updateState { copy(confirmPasswordError = Res.string.field_required) }
            valid = false
        } else if (state.confirmPassword != state.password) {
            updateState { copy(confirmPasswordError = Res.string.passwords_do_not_match) }
            valid = false
        }

        return valid
    }

    fun save() {
        if (validate()) {
            // TODO: API call to add employee
        }
    }
}
