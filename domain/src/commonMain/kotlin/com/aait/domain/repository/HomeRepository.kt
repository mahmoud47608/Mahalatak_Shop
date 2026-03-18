package com.aait.domain.repository

import com.aait.domain.entity.AuthData
import com.aait.domain.entity.base.BaseResponse
import com.aait.domain.util.DataState
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    suspend fun login(
        countryCode: String,
        phone: String,
        password: String,
        deviceId: String,
        socialId: String?
    ): Flow<DataState<BaseResponse<AuthData>>>
}
