package com.mahalatk.features.coupons

import com.mahalatk.base.SimpleViewModel
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.field_required

class AddCouponViewModel : SimpleViewModel<AddCouponState, Nothing>(AddCouponState()) {

    fun updateCode(v: String) {
        updateState { copy(code = v.uppercase(), codeError = null) }
    }

    fun updateDiscountType(type: CouponDiscountType) {
        updateState { copy(discountType = type) }
    }

    fun updateDiscountValue(v: String) {
        updateState { copy(discountValue = v, discountError = null) }
    }

    fun updateMinCartValue(v: String) {
        updateState { copy(minCartValue = v) }
    }

    fun updateMaxUses(v: String) {
        updateState { copy(maxUses = v) }
    }

    fun updateStartDate(v: String) {
        updateState { copy(startDate = v, dateError = null) }
    }

    fun updateEndDate(v: String) {
        updateState { copy(endDate = v, dateError = null) }
    }

    fun save() {
        if (!validate()) return
        updateState { copy(showSuccess = true) }
    }

    private fun validate(): Boolean {
        val state = uiState.value
        var valid = true

        if (state.code.isBlank()) {
            updateState { copy(codeError = Res.string.field_required) }
            valid = false
        }
        if (state.discountValue.isBlank()) {
            updateState { copy(discountError = Res.string.field_required) }
            valid = false
        }
        if (state.startDate.isBlank() || state.endDate.isBlank()) {
            updateState { copy(dateError = Res.string.field_required) }
            valid = false
        }
        return valid
    }
}
