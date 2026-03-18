package com.aait.domain.entity.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoomResponse(
    @SerialName("id") val id: Int? = null
)