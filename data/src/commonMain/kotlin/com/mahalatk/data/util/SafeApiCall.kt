package com.mahalatk.data.util

import com.mahalatk.domain.entity.base.AnyResponse
import com.mahalatk.domain.entity.base.BaseResponse
import com.mahalatk.domain.util.DataState
import com.mahalatk.domain.util.FailRequestCode.BLOCKED
import com.mahalatk.domain.util.FailRequestCode.EXCEPTION
import com.mahalatk.domain.util.FailRequestCode.FAIL
import com.mahalatk.domain.util.FailRequestCode.UN_AUTH
import com.mahalatk.domain.util.NetworkExceptions
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.serialization.json.Json

/** Shared Json instance for error body parsing. */
private val errorJson = Json { ignoreUnknownKeys = true }

/**
 * Wraps a suspend API call in a Flow emitting Loading → Success/Error.
 * Respects CancellationException to avoid swallowing structured concurrency.
 */
fun <T> safeApiCall(apiCall: suspend () -> T): Flow<DataState<T>> = flow {
    val response = apiCall()
    emit(if (response != null) DataState.Success(response) else DataState.Error(NetworkExceptions.UnknownException()))
}.onStart {
    emit(DataState.Loading)
}.catch { throwable ->
    if (throwable is CancellationException) throw throwable
    emit(mapThrowableToError(throwable))
}.flowOn(Dispatchers.Default)

private suspend fun <T> mapThrowableToError(throwable: Throwable): DataState<T> = when (throwable) {
    is HttpRequestTimeoutException -> DataState.Error(NetworkExceptions.TimeoutException())
    is ResponseException -> DataState.Error(parseErrorResponse(throwable))
    else -> DataState.Error(NetworkExceptions.UnknownException())
}

private suspend fun parseErrorResponse(exception: ResponseException): Exception = try {
    val body = exception.response.bodyAsText()
    if (body.isNotBlank()) {
        val response: BaseResponse<AnyResponse> = errorJson.decodeFromString(body)
        when (exception.response.status.value) {
            FAIL -> NetworkExceptions.CustomException(response.msg)
            UN_AUTH, BLOCKED -> NetworkExceptions.AuthorizationException()
            EXCEPTION -> NetworkExceptions.ServerException()
            else -> NetworkExceptions.UnknownException()
        }
    } else {
        NetworkExceptions.UnknownException()
    }
} catch (_: Exception) {
    NetworkExceptions.UnknownException()
}
