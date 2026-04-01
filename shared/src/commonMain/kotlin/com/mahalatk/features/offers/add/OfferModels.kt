package com.mahalatk.features.offers.add

import androidx.compose.runtime.Immutable
import org.jetbrains.compose.resources.StringResource

// ─── Enums ───────────────────────────────────────────────────────────────────

enum class OfferType { DISCOUNT, BUY_X_GET_Y, PACKAGE, FREE_SHIPPING }

enum class DiscountMode { PERCENTAGE, FIXED_AMOUNT }

enum class OfferScopeType { ALL_PRODUCTS, CATEGORIES, SPECIFIC_PRODUCTS }

// ─── Domain Models ───────────────────────────────────────────────────────────

@Immutable
data class ProductItem(
    val id: String,
    val name: String,
    val category: String = "",
    val imageUrl: String = "",
)

@Immutable
data class Offer(
    val id: String,
    val type: OfferType,
    val description: String,
    val startDate: String,
    val endDate: String,
    val isActive: Boolean = true,
)

// ─── UI State ────────────────────────────────────────────────────────────────

@Immutable
data class AddOfferState(
    val currentStep: Int = 0,

    // Step 0 - Offer Type
    val offerType: OfferType? = null,

    // Step 1 - Discount
    val discountMode: DiscountMode = DiscountMode.PERCENTAGE,
    val discountValue: String = "",
    val minCartValue: String = "",

    // Step 1 - Buy X Get Y
    val buyQuantity: String = "",
    val getQuantity: String = "",

    // Step 1 - Free Shipping
    val freeShippingMinCart: String = "",

    // Step 1 - Package
    val packageName: String = "",
    val packagePrice: String = "",
    val packageProductIds: Set<String> = emptySet(),

    // Step 2 - Scope
    val scopeType: OfferScopeType = OfferScopeType.ALL_PRODUCTS,
    val selectedCategories: Set<String> = emptySet(),
    val selectedProductIds: Set<String> = emptySet(),
    val filterCategories: Set<String> = emptySet(),
    val availableCategories: List<String> = emptyList(),
    val availableProducts: List<ProductItem> = emptyList(),

    // Step 3 - Duration
    val startDate: String = "",
    val endDate: String = "",

    // Feedback
    val stepError: StringResource? = null,
    val showSuccess: Boolean = false,
)

@Immutable
data class OffersState(
    val offers: List<Offer> = emptyList(),
    val isLoading: Boolean = true,
)
