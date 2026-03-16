package com.aait.domain.entity.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CallResponse(
    @SerialName("api_key") val apiKey: String? = null,
    @SerialName("api_secret") val apiSecret: String? = null,
    @SerialName("session_id") val sessionId: String? = null,
    @SerialName("token") val token: String? = null,
)