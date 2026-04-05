package com.mahalatk.features.coupons

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.StringResource

enum class CouponDiscountType { PERCENTAGE, FIXED_AMOUNT }

@Immutable
data class Coupon(
    val id: String,
    val code: String,
    val discountType: CouponDiscountType,
    val discountValue: String,
    val minCartValue: String = "",
    val maxUses: String = "",
    val usedCount: Int = 0,
    val startDate: String,
    val endDate: String,
    val isActive: Boolean = true,
)

@Immutable
data class CouponsState(
    val coupons: ImmutableList<Coupon> = persistentListOf(),
    val isLoading: Boolean = true,
)

@Immutable
data class AddCouponState(
    val code: String = "",
    val discountType: CouponDiscountType = CouponDiscountType.PERCENTAGE,
    val discountValue: String = "",
    val minCartValue: String = "",
    val maxUses: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val codeError: StringResource? = null,
    val discountError: StringResource? = null,
    val dateError: StringResource? = null,
    val showSuccess: Boolean = false,
)
