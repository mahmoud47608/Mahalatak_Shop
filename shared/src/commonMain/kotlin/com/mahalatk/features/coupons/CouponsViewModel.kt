package com.mahalatk.features.coupons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CouponsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        CouponsState(
            coupons = listOf(
                Coupon(
                    "1",
                    "SALE20",
                    CouponDiscountType.PERCENTAGE,
                    "20",
                    "100",
                    "50",
                    12,
                    "01/04/2026",
                    "30/04/2026"
                ),
                Coupon(
                    "2",
                    "WELCOME50",
                    CouponDiscountType.FIXED_AMOUNT,
                    "50",
                    "",
                    "100",
                    45,
                    "01/04/2026",
                    "15/04/2026"
                ),
                Coupon(
                    "3",
                    "VIP10",
                    CouponDiscountType.PERCENTAGE,
                    "10",
                    "500",
                    "",
                    3,
                    "10/04/2026",
                    "10/05/2026",
                    false
                ),
            ),
        ),
    )
    val uiState: StateFlow<CouponsState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            delay(500)
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun toggleActive(id: String) {
        _uiState.update { state ->
            state.copy(coupons = state.coupons.map { if (it.id == id) it.copy(isActive = !it.isActive) else it })
        }
    }

    fun deleteCoupon(id: String) {
        _uiState.update { state -> state.copy(coupons = state.coupons.filter { it.id != id }) }
    }
}
