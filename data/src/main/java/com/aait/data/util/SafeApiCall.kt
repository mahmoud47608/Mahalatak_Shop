package com.aait.data.util

import com.aait.domain.entity.base.AnyResponse
import com.aait.domain.entity.base.BaseResponse
import com.aait.domain.util.DataState
import com.aait.domain.util.FailRequestCode.BLOCKED
import com.aait.domain.util.FailRequestCode.EXCEPTION
import com.aait.domain.util.FailRequestCode.FAIL
import com.aait.domain.util.FailRequestCode.UN_AUTH
import com.aait.domain.util.NetworkExceptions
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

fun <T> safeApiCall(
    apiCall: suspend () -> T
): Flow<DataState<T>> = flow {
    withTimeout(120000L) {
        val response = apiCall.invoke()
        emit(handleSuccess(response))
    }
}.onStart {
    emit(DataState.Loading)
}.catch { throwable ->
    if (throwable is CancellationException) throw throwable
    emit(handleError(throwable))
}.flowOn(Dispatchers.IO)

fun <T> handleSuccess(response: T): DataState<T> {
    if (response != null) return DataState.Success(response)
    return DataState.Error(NetworkExceptions.UnknownException)
}

fun <T> handleError(it: Throwable): DataState<T> {
    it.printStackTrace()
    return when (it) {
        is TimeoutCancellationException -> {
            DataState.Error(NetworkExceptions.TimeoutException)
        }

        is UnknownHostException -> {
            DataState.Error(NetworkExceptions.ConnectionException)
        }

        is IOException -> {
            DataState.Error(NetworkExceptions.UnknownException)
        }

        is HttpException -> {
            DataState.Error(convertErrorBody(it))
        }

        else -> {
            DataState.Error(NetworkExceptions.UnknownException)
        }
    }
}

fun convertErrorBody(throwable: HttpException): Exception {
    return try {
        val errorBody = throwable.response()?.errorBody()?.charStream()
        val jsonString = errorBody?.buffered().use { it?.readText() }
        if (!jsonString.isNullOrBlank()) {
            val response: BaseResponse<AnyResponse> = Json.decodeFromString(jsonString)
            when (throwable.code()) {
                FAIL -> NetworkExceptions.CustomException(response.msg)
                UN_AUTH, BLOCKED -> NetworkExceptions.AuthorizationException
                EXCEPTION -> NetworkExceptions.ServerException
                else -> NetworkExceptions.UnknownException
            }
        } else {
            NetworkExceptions.UnknownException
        }
    } catch (e: Exception) {
        NetworkExceptions.UnknownException
    }
}