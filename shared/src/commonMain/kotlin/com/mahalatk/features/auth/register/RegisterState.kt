package com.mahalatk.features.auth.register

import androidx.compose.runtime.Immutable
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.category_kids
import mahalatk.shared.generated.resources.category_men
import mahalatk.shared.generated.resources.category_men_shoes
import mahalatk.shared.generated.resources.category_women
import mahalatk.shared.generated.resources.category_women_shoes
import org.jetbrains.compose.resources.StringResource

enum class AccountType {
    SHOP_OWNER,
    EMPLOYEE
}

enum class ShopCategory(val labelRes: StringResource) {
    MEN(Res.string.category_men),
    WOMEN(Res.string.category_women),
    KIDS(Res.string.category_kids),
    MEN_SHOES(Res.string.category_men_shoes),
    WOMEN_SHOES(Res.string.category_women_shoes),
}

enum class ShopType {
    PHYSICAL,
    ONLINE
}

enum class ReturnPolicy {
    EXCHANGE,
    EXCHANGE_AND_RETURN,
    NOT_AVAILABLE
}

enum class ReturnPeriod {
    DAYS_2,
    DAYS_3,
    DAYS_7,
    DAYS_14
}

data class CityItem(
    val id: Int,
    val name: String,
)

@Immutable
data class RegisterState(
    val accountType: AccountType = AccountType.SHOP_OWNER,

    // Shop Owner fields
    val shopName: String = "",
    val ownerName: String = "",
    val shopType: ShopType = ShopType.PHYSICAL,
    val selectedCategories: Set<ShopCategory> = emptySet(),
    val returnPolicy: ReturnPolicy = ReturnPolicy.EXCHANGE,
    val returnPeriod: ReturnPeriod = ReturnPeriod.DAYS_2,
    val shopImage: ByteArray? = null,
    val selectedCity: CityItem? = null,
    val locationAddress: String = "",
    val locationLat: Double? = null,
    val locationLng: Double? = null,

    // Employee fields
    val employeeName: String = "",
    val selectedShop: String? = null,
    val employeeImage: ByteArray? = null,

    // Common fields
    val mobile: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordVisible: Boolean = false,
    val confirmPasswordVisible: Boolean = false,

    // Error fields
    val shopNameError: StringResource? = null,
    val ownerNameError: StringResource? = null,
    val categoryError: StringResource? = null,
    val cityError: StringResource? = null,
    val locationError: StringResource? = null,
    val employeeNameError: StringResource? = null,
    val selectedShopError: StringResource? = null,
    val imageError: StringResource? = null,
    val mobileError: StringResource? = null,
    val passwordError: StringResource? = null,
    val confirmPasswordError: StringResource? = null,

    // Available data for dropdowns
    val availableCities: List<CityItem> = listOf(
        CityItem(1, "الرياض"),
        CityItem(2, "جدة"),
        CityItem(3, "مكة المكرمة"),
        CityItem(4, "المدينة المنورة"),
        CityItem(5, "الدمام"),
        CityItem(6, "الخبر"),
        CityItem(7, "الطائف"),
        CityItem(8, "تبوك"),
        CityItem(9, "بريدة"),
        CityItem(10, "أبها"),
    ),
    val availableShops: List<String> = listOf(
        "محل الأناقة",
        "محل الموضة",
        "محل الأحذية",
        "محل الأطفال",
        "محل السعادة",
    ),
)
