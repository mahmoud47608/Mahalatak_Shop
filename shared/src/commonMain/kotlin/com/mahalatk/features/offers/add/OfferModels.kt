package com.mahalatk.features.offers.add

import androidx.compose.runtime.Immutable
import org.jetbrains.compose.resources.StringResource

enum class OfferType { DISCOUNT, BUY_X_GET_Y, PACKAGE, FREE_SHIPPING }
enum class DiscountMode { PERCENTAGE, FIXED_AMOUNT }
enum class OfferScopeType { ALL_PRODUCTS, CATEGORIES, SPECIFIC_PRODUCTS }

@Immutable
data class ProductItem(
    val id: String,
    val name: String,
    val category: String = "",
    val imageUrl: String = "",
)

@Immutable
data class AddOfferState(
    val currentStep: Int = 0,
    // Step 1
    val offerType: OfferType? = null,
    // Step 2 - Discount
    val discountMode: DiscountMode = DiscountMode.PERCENTAGE,
    val discountValue: String = "",
    val minCartValue: String = "",
    // Step 2 - Buy X Get Y
    val buyQuantity: String = "",
    val getQuantity: String = "",
    // Step 2 - Free Shipping
    val freeShippingMinCart: String = "",
    // Step 2 - Package
    val packageName: String = "",
    val packagePrice: String = "",
    val packageProductIds: Set<String> = emptySet(),
    // Step 3
    val scopeType: OfferScopeType = OfferScopeType.ALL_PRODUCTS,
    val selectedCategories: Set<String> = emptySet(),
    val selectedProductIds: Set<String> = emptySet(),
    val filterCategories: Set<String> = emptySet(),
    val availableCategories: List<String> = listOf(
        "رجالي",
        "حريمي",
        "أطفالي",
        "أحذية رجالي",
        "أحذية حريمي"
    ),
    val availableProducts: List<ProductItem> = listOf(
        ProductItem("1", "قميص رجالي كلاسيك", "رجالي"),
        ProductItem("2", "بنطلون جينز", "رجالي"),
        ProductItem("3", "تيشيرت قطن", "رجالي"),
        ProductItem("4", "حذاء رياضي", "أحذية رجالي"),
        ProductItem("5", "جاكيت شتوي", "حريمي"),
        ProductItem("6", "فستان سهرة", "حريمي"),
        ProductItem("7", "بلوزة أطفال", "أطفالي"),
        ProductItem("8", "حذاء حريمي", "أحذية حريمي"),
    ),
    // Step 4
    val startDate: String = "",
    val endDate: String = "",
    // Errors
    val stepError: StringResource? = null,
    val showSuccess: Boolean = false,
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

@Immutable
data class OffersState(
    val offers: List<Offer> = emptyList(),
    val isLoading: Boolean = true,
)
