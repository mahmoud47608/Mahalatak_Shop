package com.mahalatk.domain.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryData(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
)

@Serializable
data class SubCategoryData(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("category_id") val categoryId: Int,
)
