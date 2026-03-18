package com.aait.domain.entity.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageItem(
    @SerialName("id") val id: Int? = null,
    @SerialName("is_sender") val isSender: Int? = null,
    @SerialName("sender_id") val senderId: String? = null,
    @SerialName("body") val body: String? = null,
    @SerialName("type") val type: String? = null,
    @SerialName("duration") val duration: Double? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("created_dt") val createdAt: String? = null,
    var maxPosition: Int = 0,
    var mediaPosition: Int = 0,
)