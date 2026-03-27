package com.mahalatk.domain.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Auth response data — maps directly to the API "data" object.
 * The API returns user fields at the top level of "data", not nested inside "user".
 */
@Serializable
data class AuthData(
    @SerialName("id") val id: Int? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("first_name") val firstName: String? = null,
    @SerialName("last_name") val lastName: String? = null,
    @SerialName("user_type") val userType: String? = null,
    @SerialName("type") val type: String? = null,
    @SerialName("email") val email: String? = null,
    @SerialName("country_code") val countryCode: String? = null,
    @SerialName("phone") val phone: String? = null,
    @SerialName("full_phone") val fullPhone: String? = null,
    @SerialName("image") val image: String? = null,
    @SerialName("lang") val lang: String? = null,
    @SerialName("is_notify") val isNotify: Boolean? = null,
    @SerialName("token") val token: String? = null,
    @SerialName("city") val city: City? = null,
) {
    @Serializable
    data class City(
        @SerialName("id") val id: Int? = null,
        @SerialName("name") val name: String? = null,
    )
}
