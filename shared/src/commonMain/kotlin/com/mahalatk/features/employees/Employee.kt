package com.mahalatk.features.employees

import androidx.compose.runtime.Immutable

@Immutable
data class Employee(
    val id: String,
    val name: String,
    val phone: String,
    val imageUrl: String = "",
)
