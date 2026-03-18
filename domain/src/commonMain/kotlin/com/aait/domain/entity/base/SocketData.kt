package com.aait.domain.entity.base

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SocketData(
    @SerialName("id") val id: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("type") val type: String? = null,
    @SerialName("avatar") val avatar: String? = null
)