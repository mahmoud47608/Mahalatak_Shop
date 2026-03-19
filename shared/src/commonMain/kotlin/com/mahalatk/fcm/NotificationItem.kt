package com.mahalatk.fcm

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationItem(
    @SerialName("title_ar") val titleAr: String? = "",
    @SerialName("title_en") val titleEn: String? = "",
    @SerialName("body_ar") val bodyAr: String? = "",
    @SerialName("body_en") val bodyEn: String? = "",
    @SerialName("type") val type: String? = "",
    @SerialName("order_id") val orderId: String? = "",
    @SerialName("room_id") val roomId: String? = "",
    @SerialName("sent_or_received") val sentOrReceived: String? = "",
    @SerialName("order_type") val orderType: String? = ""
)
