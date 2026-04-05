package com.mahalatk.features.profile.employee

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
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
    val availableShops: ImmutableList<String> = persistentListOf(
        "محل الأناقة",
        "محل الموضة",
        "محل الأحذية",
        "محل الأطفال",
        "محل السعادة",
    ),
)
