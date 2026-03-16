package com.aait.domain.entity.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoomMessagesResponse(
    @SerialName("call") val call: CallResponse? = null,
    @SerialName("room") val room: RoomResponse? = null,
    @SerialName("members") val members: MutableList<RoomMemberItem>? = null,
    @SerialName("messages") val messages: MessagesResponse? = null,
)