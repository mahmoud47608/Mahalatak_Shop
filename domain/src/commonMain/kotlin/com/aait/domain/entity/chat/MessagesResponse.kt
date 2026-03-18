package com.aait.domain.entity.chat

import com.aait.domain.entity.base.Pagination
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessagesResponse(
    @SerialName("data") val data: MutableList<MessageItem>? = null,
    @SerialName("pagination") var pagination: Pagination? = null
)