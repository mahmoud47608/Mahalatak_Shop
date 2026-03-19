package com.mahalatk.domain.repository

import com.mahalatk.domain.entity.AuthData
import com.mahalatk.domain.entity.base.BaseResponse
import com.mahalatk.domain.util.DataState
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
