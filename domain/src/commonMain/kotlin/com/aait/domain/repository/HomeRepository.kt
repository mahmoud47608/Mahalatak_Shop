package com.aait.domain.repository

import com.aait.domain.entity.AuthData
import com.aait.domain.entity.base.AnyResponse
import com.aait.domain.entity.base.BaseResponse
import com.aait.domain.entity.general.CountriesResponse
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

    suspend fun register(
        firstName: String,
        lastName: String,
        countryCode: String,
        phone: String,
        email: String,
        password: String
    ): Flow<DataState<BaseResponse<AuthData>>>

    suspend fun activate(
        code: String,
        email: String,
        deviceId: String,
        macAddress: String
    ): Flow<DataState<BaseResponse<AuthData>>>

    suspend fun countries(): Flow<DataState<BaseResponse<CountriesResponse>>>

    suspend fun forgetPassword(email: String): Flow<DataState<BaseResponse<AnyResponse>>>

    suspend fun resetPassword(
        code: String,
        email: String,
        password: String
    ): Flow<DataState<BaseResponse<AnyResponse>>>

    suspend fun resendCode(email: String): Flow<DataState<BaseResponse<AnyResponse>>>
}
