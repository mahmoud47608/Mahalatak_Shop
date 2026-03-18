package com.aait.data.repo_impl

import com.aait.data.remote.HomeEndPoint
import com.aait.data.util.safeApiCall
import com.aait.domain.repository.HomeRepository

class HomeRepositoryImpl(private val homeEndPoint: HomeEndPoint) :
    HomeRepository {

    override suspend fun login(
        countryCode: String,
        phone: String,
        password: String,
        deviceId: String,
        socialId: String?
    ) = safeApiCall {
        homeEndPoint.login(
            countryCode = countryCode,
            phone = phone,
            password = password,
            deviceId = deviceId,
            socialId = socialId
        )
    }

    override suspend fun register(
        firstName: String,
        lastName: String,
        countryCode: String,
        phone: String,
        email: String,
        password: String
    ) = safeApiCall {
        homeEndPoint.register(
            firstName = firstName,
            lastName = lastName,
            countryCode = countryCode,
            phone = phone,
            email = email,
            password = password
        )
    }

    override suspend fun activate(
        code: String,
        email: String,
        deviceId: String,
        macAddress: String
    ) = safeApiCall {
        homeEndPoint.activate(
            code = code,
            email = email,
            deviceId = deviceId,
            macAddress = macAddress
        )
    }

    override suspend fun countries() = safeApiCall {
        homeEndPoint.countries()
    }

    override suspend fun forgetPassword(email: String) = safeApiCall {
        homeEndPoint.forgetPassword(email = email)
    }

    override suspend fun resetPassword(
        code: String,
        email: String,
        password: String
    ) = safeApiCall {
        homeEndPoint.resetPassword(
            code = code,
            email = email,
            password = password
        )
    }

    override suspend fun resendCode(email: String) = safeApiCall {
        homeEndPoint.resendCode(email = email)
    }
}
