package com.aait.data.remote

import com.aait.domain.entity.AuthData
import com.aait.domain.entity.base.AnyResponse
import com.aait.domain.entity.base.BaseResponse
import com.aait.domain.entity.general.CountriesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.parameters

class HomeEndPoint(private val client: HttpClient) {

    suspend fun login(
        countryCode: String,
        phone: String,
        password: String,
        deviceId: String,
        socialId: String?,
        deviceType: String = "android"
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

    suspend fun register(
        firstName: String,
        lastName: String,
        countryCode: String,
        phone: String,
        email: String,
        password: String
    ): BaseResponse<AuthData> = client.submitForm(
        url = "sign-up",
        formParameters = parameters {
            append("first_name", firstName)
            append("last_name", lastName)
            append("country_code", countryCode)
            append("phone", phone)
            append("email", email)
            append("password", password)
        }
    ).body()

    suspend fun activate(
        code: String,
        email: String,
        deviceId: String,
        macAddress: String,
        deviceType: String = "android",
        method: String = "patch"
    ): BaseResponse<AuthData> = client.submitForm(
        url = "activate",
        formParameters = parameters {
            append("code", code)
            append("email", email)
            append("device_id", deviceId)
            append("mac_address", macAddress)
            append("device_type", deviceType)
            append("_method", method)
        }
    ).body()

    suspend fun countries(): BaseResponse<CountriesResponse> =
        client.get("countries").body()

    suspend fun forgetPassword(
        email: String
    ): BaseResponse<AnyResponse> = client.submitForm(
        url = "forget-password-send-code",
        formParameters = parameters {
            append("email", email)
        }
    ).body()

    suspend fun resetPassword(
        code: String,
        email: String,
        password: String
    ): BaseResponse<AnyResponse> = client.submitForm(
        url = "reset-password",
        formParameters = parameters {
            append("code", code)
            append("email", email)
            append("password", password)
        }
    ).body()

    suspend fun resendCode(
        email: String
    ): BaseResponse<AnyResponse> =
        client.get("resend-code") {
            parameter("email", email)
        }.body()
}
