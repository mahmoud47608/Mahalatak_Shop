package com.mahalatk.features.employees

data class Employee(
    val id: String,
    val name: String,
    val phone: String,
    val imageUrl: String = "",
)
