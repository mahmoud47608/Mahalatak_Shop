package com.aait.data.util

import com.aait.domain.entity.base.AnyResponse
import com.aait.domain.entity.base.BaseResponse
import com.aait.domain.util.DataState
import com.aait.domain.util.FailRequestCode.BLOCKED
import com.aait.domain.util.FailRequestCode.EXCEPTION
import com.aait.domain.util.FailRequestCode.FAIL
import com.aait.domain.util.FailRequestCode.UN_AUTH
import com.aait.domain.util.NetworkExceptions
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.bodyAsText
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
}.flowOn(Dispatchers.Default)

fun <T> handleSuccess(response: T): DataState<T> {
    if (response != null) return DataState.Success(response)
    return DataState.Error(NetworkExceptions.UnknownException())
}

suspend fun <T> handleError(it: Throwable): DataState<T> {
    it.printStackTrace()
    return when (it) {
        is TimeoutCancellationException -> {
            DataState.Error(NetworkExceptions.TimeoutException())
        }

        is ResponseException -> {
            DataState.Error(convertErrorBody(it))
        }

        else -> {
            DataState.Error(NetworkExceptions.UnknownException())
        }
    }
}

suspend fun convertErrorBody(throwable: ResponseException): Exception {
    return try {
        val jsonString = throwable.response.bodyAsText()
        if (jsonString.isNotBlank()) {
            val response: BaseResponse<AnyResponse> = Json.decodeFromString(jsonString)
            when (throwable.response.status.value) {
                FAIL -> NetworkExceptions.CustomException(response.msg)
                UN_AUTH, BLOCKED -> NetworkExceptions.AuthorizationException()
                EXCEPTION -> NetworkExceptions.ServerException()
                else -> NetworkExceptions.UnknownException()
            }
        } else {
            NetworkExceptions.UnknownException()
        }
    } catch (e: Exception) {
        NetworkExceptions.UnknownException()
    }
}
