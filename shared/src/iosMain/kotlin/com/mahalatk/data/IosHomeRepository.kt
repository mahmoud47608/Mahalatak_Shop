package com.mahalatk.data

import com.mahalatk.domain.entity.AuthData
import com.mahalatk.domain.entity.base.BaseResponse
import com.mahalatk.domain.repository.HomeRepository
import com.mahalatk.domain.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class IosHomeRepository : HomeRepository {

    override suspend fun login(
        countryCode: String,
        phone: String,
        password: String,
        deviceId: String,
        socialId: String?
    ): Flow<DataState<BaseResponse<AuthData>>> = flow {
        // TODO: Implement iOS API calls using Ktor
        emit(DataState.Error(Exception("Not yet implemented for iOS")))
    }
}
