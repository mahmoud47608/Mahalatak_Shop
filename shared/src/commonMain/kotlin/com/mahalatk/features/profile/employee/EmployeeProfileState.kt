package com.mahalatk.features.profile.employee

import androidx.compose.runtime.Immutable
import org.jetbrains.compose.resources.StringResource

@Immutable
data class EmployeeProfileState(
    // Employee fields
    val employeeName: String = "",
    val mobile: String = "",
    val selectedShop: String? = null,
    val employeeImage: ByteArray? = null,
    val employeeImageUrl: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordVisible: Boolean = false,
    val confirmPasswordVisible: Boolean = false,

    // Error fields
    val employeeNameError: StringResource? = null,
    val mobileError: StringResource? = null,
    val selectedShopError: StringResource? = null,
    val imageError: StringResource? = null,
    val passwordError: StringResource? = null,
    val confirmPasswordError: StringResource? = null,

    // Available data for dropdowns
    val availableShops: List<String> = listOf(
        "محل الأناقة",
        "محل الموضة",
        "محل الأحذية",
        "محل الأطفال",
        "محل السعادة",
    ),
)
