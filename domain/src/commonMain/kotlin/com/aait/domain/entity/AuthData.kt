package com.aait.domain.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthData(
    @SerialName("user") val user: User? = null
) {
    @Serializable
    data class User(
        @SerialName("id") val id: Int? = null,
        @SerialName("first_name") val firstName: String? = null,
        @SerialName("last_name") val lastName: String? = null,
        @SerialName("user_type") val userType: String? = null,
        @SerialName("email") val email: String? = null,
        @SerialName("country_code") val countryCode: String? = null,
        @SerialName("phone") val phone: String? = null,
        @SerialName("full_phone") val fullPhone: String? = null,
        @SerialName("image") val image: String? = null,
        @SerialName("lang") val lang: String? = null,
        @SerialName("new_subscription_notify") val newSubscriptionNotify: Boolean? = null,
        @SerialName("new_video_comment_notify") val newVideoCommentNotify: Boolean? = null,
        @SerialName("new_voice_comment_notify") val newVoiceCommentNotify: Boolean? = null,
        @SerialName("new_video_publish_notify") val newVideoPublishNotify: Boolean? = null,
        @SerialName("allow_receive_email") val allowReceiveEmail: Boolean? = null,
        @SerialName("videos_count") val videosCount: Int? = null,
        @SerialName("follows") val follows: Int? = null,
        @SerialName("followers") val followers: Int? = null,
        @SerialName("views") val views: Int? = null,
        @SerialName("is_followed") val isFollowed: Boolean? = null,
        @SerialName("is_completed") val isCompleted: Boolean? = null,
        @SerialName("token") val token: String? = null
    )
}
