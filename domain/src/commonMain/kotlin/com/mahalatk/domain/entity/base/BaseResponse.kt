package com.mahalatk.domain.entity.base

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    @SerialName("key") val key: String = "",
    @SerialName("msg") val msg: String = "",
    @SerialName("data") val data: T? = null
)