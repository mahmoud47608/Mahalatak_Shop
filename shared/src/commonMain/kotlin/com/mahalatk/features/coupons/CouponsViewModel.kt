package com.mahalatk.features.coupons

import androidx.lifecycle.viewModelScope
import com.mahalatk.base.SimpleViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CouponsViewModel : SimpleViewModel<CouponsState, Nothing>(
    CouponsState(
        coupons = persistentListOf(
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
) {

    init {
        viewModelScope.launch {
            delay(500)
            updateState { copy(isLoading = false) }
        }
    }

    fun toggleActive(id: String) {
        updateState {
            copy(coupons = coupons.map { if (it.id == id) it.copy(isActive = !it.isActive) else it }
                .toImmutableList())
        }
    }

    fun deleteCoupon(id: String) {
        updateState { copy(coupons = coupons.filter { it.id != id }.toImmutableList()) }
    }
}
