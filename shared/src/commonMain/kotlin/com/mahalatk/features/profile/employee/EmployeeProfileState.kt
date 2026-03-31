package com.mahalatk.features.profile.employee

import androidx.compose.runtime.Immutable
import org.jetbrains.compose.resources.StringResource

@Immutable
data class EmployeeProfileState(
    // Employee fields
    val employeeName: String = "",
    val selectedShop: String? = null,
    val employeeImage: ByteArray? = null,
    val employeeImageUrl: String = "",

    // Error fields
    val employeeNameError: StringResource? = null,
    val selectedShopError: StringResource? = null,
    val imageError: StringResource? = null,

    // Available data for dropdowns
    val availableShops: List<String> = listOf(
        "محل الأناقة",
        "محل الموضة",
        "محل الأحذية",
        "محل الأطفال",
        "محل السعادة",
    ),
)
