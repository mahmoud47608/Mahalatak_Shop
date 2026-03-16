package com.aait.domain.entity.general

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DataItem(
    @SerialName("id") val id: Int? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("image") val image: String? = null,
    @SerialName("key") val key: String? = null,
    var isSelected: Boolean=false
)