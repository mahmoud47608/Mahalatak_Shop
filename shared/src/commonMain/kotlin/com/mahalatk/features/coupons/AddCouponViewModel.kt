package com.mahalatk.features.coupons

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.field_required

class AddCouponViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AddCouponState())
    val uiState: StateFlow<AddCouponState> = _uiState.asStateFlow()

    fun updateCode(v: String) {
        _uiState.update { it.copy(code = v.uppercase(), codeError = null) }
    }

    fun updateDiscountType(type: CouponDiscountType) {
        _uiState.update { it.copy(discountType = type) }
    }

    fun updateDiscountValue(v: String) {
        _uiState.update { it.copy(discountValue = v, discountError = null) }
    }

    fun updateMinCartValue(v: String) {
        _uiState.update { it.copy(minCartValue = v) }
    }

    fun updateMaxUses(v: String) {
        _uiState.update { it.copy(maxUses = v) }
    }

    fun updateStartDate(v: String) {
        _uiState.update { it.copy(startDate = v, dateError = null) }
    }

    fun updateEndDate(v: String) {
        _uiState.update { it.copy(endDate = v, dateError = null) }
    }

    fun save() {
        if (!validate()) return
        _uiState.update { it.copy(showSuccess = true) }
    }

    private fun validate(): Boolean {
        val state = _uiState.value
        var valid = true

        if (state.code.isBlank()) {
            _uiState.update { it.copy(codeError = Res.string.field_required) }
            valid = false
        }
        if (state.discountValue.isBlank()) {
            _uiState.update { it.copy(discountError = Res.string.field_required) }
            valid = false
        }
        if (state.startDate.isBlank() || state.endDate.isBlank()) {
            _uiState.update { it.copy(dateError = Res.string.field_required) }
            valid = false
        }
        return valid
    }
}
