package com.mahalatk.data.remote

import com.mahalatk.data.platform.platformDeviceType
import com.mahalatk.domain.entity.AuthData
import com.mahalatk.domain.entity.base.BaseResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.http.parameters

class HomeEndPoint(private val client: HttpClient) {

    suspend fun login(
        countryCode: String,
        phone: String,
        password: String,
        deviceId: String,
        socialId: String?,
        deviceType: String = platformDeviceType
    ): BaseResponse<AuthData> = client.submitForm(
        url = "sign-in",
        formParameters = parameters {
            append("country_code", countryCode)
            append("phone", phone)
            append("password", password)
            append("device_id", deviceId)
            socialId?.let { append("social_id", it) }
            append("device_type", deviceType)
        }
    ).body()
}
