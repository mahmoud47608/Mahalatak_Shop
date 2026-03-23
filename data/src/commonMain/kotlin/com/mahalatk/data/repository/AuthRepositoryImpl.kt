package com.mahalatk.data.repository

import com.mahalatk.data.remote.AuthEndPoint
import com.mahalatk.data.util.safeApiCall
import com.mahalatk.domain.repository.AuthRepository

class AuthRepositoryImpl(private val authEndPoint: AuthEndPoint) :
    AuthRepository {

    override suspend fun login(
        countryCode: String,
        phone: String,
        password: String,
        deviceId: String,
        socialId: String?
    ) = safeApiCall {
        authEndPoint.login(
            countryCode = countryCode,
            phone = phone,
            password = password,
            deviceId = deviceId,
            socialId = socialId
        )
    }
}
