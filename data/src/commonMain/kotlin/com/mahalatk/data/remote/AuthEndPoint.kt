package com.mahalatk.data.remote

import com.mahalatk.data.platform.platformDeviceType
import com.mahalatk.domain.entity.AuthData
import com.mahalatk.domain.entity.base.BaseResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.submitForm
import io.ktor.client.statement.readRawBytes
import io.ktor.http.parameters
import kotlinx.serialization.json.Json

class AuthEndPoint(private val client: HttpClient, private val json: Json) {

    suspend fun login(
        countryCode: String,
        phone: String,
        password: String,
        deviceId: String,
        socialId: String?,
        deviceType: String = platformDeviceType
    ): BaseResponse<AuthData> {
        val responseText = client.submitForm(
            url = "sign-in",
            formParameters = parameters {
                append("country_code", countryCode)
                append("phone", phone)
                append("password", password)
                append("device_id", deviceId)
                socialId?.let { append("social_id", it) }
                append("device_type", deviceType)
            }
        ).readRawBytes().decodeToString()
        println("HTTP RESPONSE BODY: $responseText")
        return json.decodeFromString(responseText)
    }
}
