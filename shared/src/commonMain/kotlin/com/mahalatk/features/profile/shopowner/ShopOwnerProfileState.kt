package com.mahalatk.features.profile.shopowner

import androidx.compose.runtime.Immutable
import com.mahalatk.features.auth.register.CityItem
import com.mahalatk.features.auth.register.DeliveryType
import com.mahalatk.features.auth.register.ShopCategory
import org.jetbrains.compose.resources.StringResource

@Immutable
data class ShopOwnerProfileState(
    // Shop Owner fields
    val shopName: String = "",
    val ownerName: String = "",
    val mobile: String = "",
    val selectedCategories: Set<ShopCategory> = emptySet(),
    val deliveryType: DeliveryType? = null,
    val shopImage: ByteArray? = null,
    val shopImageUrl: String = "",
    val selectedCity: CityItem? = null,
    val locationAddress: String = "",
    val locationLat: Double? = null,
    val locationLng: Double? = null,
    val password: String = "",
    val confirmPassword: String = "",
    val passwordVisible: Boolean = false,
    val confirmPasswordVisible: Boolean = false,

    // Error fields
    val shopNameError: StringResource? = null,
    val ownerNameError: StringResource? = null,
    val mobileError: StringResource? = null,
    val categoryError: StringResource? = null,
    val deliveryTypeError: StringResource? = null,
    val cityError: StringResource? = null,
    val locationError: StringResource? = null,
    val imageError: StringResource? = null,
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
)
