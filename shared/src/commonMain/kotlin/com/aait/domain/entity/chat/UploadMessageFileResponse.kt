package com.aait.domain.entity.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadMessageFileResponse(
    @SerialName("file_name") val fileName: String? = null,
    @SerialName("file_url") val fileUrl: String? = null,
    @SerialName("file_type") val fileType: String? = null
)