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
}
