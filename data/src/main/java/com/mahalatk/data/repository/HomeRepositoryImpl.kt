package com.mahalatk.data.repository

import com.mahalatk.data.remote.HomeEndPoint
import com.mahalatk.data.util.safeApiCall
import com.mahalatk.domain.repository.HomeRepository

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
