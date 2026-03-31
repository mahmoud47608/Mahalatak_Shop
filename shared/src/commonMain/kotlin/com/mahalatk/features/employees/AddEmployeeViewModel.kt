package com.mahalatk.features.employees

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.jetbrains.compose.resources.StringResource

data class AddEmployeeState(
    val name: String = "",
    val phone: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val image: ByteArray? = null,
    val nameError: StringResource? = null,
    val phoneError: StringResource? = null,
    val passwordError: StringResource? = null,
    val imageError: StringResource? = null,
)

class AddEmployeeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AddEmployeeState())
    val uiState: StateFlow<AddEmployeeState> = _uiState.asStateFlow()

    fun updateState(reducer: AddEmployeeState.() -> AddEmployeeState) {
        _uiState.update { it.reducer() }
    }

    fun onNameChanged(value: String) {
        _uiState.update { it.copy(name = value, nameError = null) }
    }

    fun onPhoneChanged(value: String) {
        _uiState.update { it.copy(phone = value, phoneError = null) }
    }

    fun onPasswordChanged(value: String) {
        _uiState.update { it.copy(password = value, passwordError = null) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun validate(): Boolean {
        var valid = true
        val state = _uiState.value

        if (state.name.isBlank()) {
            _uiState.update { it.copy(nameError = mahalatk.shared.generated.resources.Res.string.field_required) }
            valid = false
        }
        if (state.phone.isBlank() || state.phone.length < 9) {
            _uiState.update { it.copy(phoneError = mahalatk.shared.generated.resources.Res.string.field_required) }
            valid = false
        }
        if (state.password.isBlank()) {
            _uiState.update { it.copy(passwordError = mahalatk.shared.generated.resources.Res.string.field_required) }
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
