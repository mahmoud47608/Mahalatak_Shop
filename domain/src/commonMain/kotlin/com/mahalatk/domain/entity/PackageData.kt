package com.mahalatk.domain.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PackageData(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("price") val price: Double? = null,
    @SerialName("type") val type: String? = null,
    @SerialName("image") val image: String? = null,
    @SerialName("status") val status: String? = null,
)
