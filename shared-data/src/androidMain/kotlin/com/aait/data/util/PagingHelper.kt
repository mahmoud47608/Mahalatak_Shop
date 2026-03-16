package com.aait.data.util

import android.util.Log
import androidx.paging.PagingSource
import com.aait.domain.entity.base.BaseResponse
import com.aait.domain.entity.base.Pagination
import com.aait.domain.util.NetworkExceptions
import com.aait.domain.util.ResponseStatus.ACTIVE
import com.aait.domain.util.ResponseStatus.BLOCK
import com.aait.domain.util.ResponseStatus.EXCEPTION
import com.aait.domain.util.ResponseStatus.FAILED
import com.aait.domain.util.ResponseStatus.NEED_ACTIVATE
import com.aait.domain.util.ResponseStatus.NOT_ACTIVE
import com.aait.domain.util.ResponseStatus.PENDING
import com.aait.domain.util.ResponseStatus.SUCCESS
import com.aait.domain.util.ResponseStatus.UN_AUTH
import io.ktor.client.plugins.ResponseException
import java.io.IOException

private const val TAG = "PagingHelper"

fun <T : Any> handlePagingResponse(
    pageIndex: Int,
    response: BaseResponse<*>,
    list: List<T>?,
    pagination: Pagination?
): PagingSource.LoadResult<Int, T> {
    return try {
        val nextKey = if (pageIndex >= (pagination?.totalPages ?: 0)) null else pageIndex + 1

        when (response.key) {
            SUCCESS,
            ACTIVE -> PagingSource.LoadResult.Page(
                data = list ?: emptyList(),
                prevKey = if (pageIndex == 1) null else pageIndex - 1,
                nextKey = nextKey
            )

            UN_AUTH,
            BLOCK -> PagingSource.LoadResult.Error(NetworkExceptions.AuthorizationException())

            NEED_ACTIVATE -> PagingSource.LoadResult.Error(
                NetworkExceptions.NeedActiveException(response.msg)
            )

            FAILED,
            EXCEPTION,
            PENDING,
            NOT_ACTIVE -> PagingSource.LoadResult.Error(
                NetworkExceptions.CustomException(response.msg)
            )

            else -> PagingSource.LoadResult.Error(NetworkExceptions.UnknownException())
        }
    } catch (exception: IOException) {
        Log.e(TAG, "IOException: ${exception.message}")
        PagingSource.LoadResult.Error(NetworkExceptions.ConnectionException())
    } catch (exception: ResponseException) {
        Log.e(TAG, "ResponseException: ${exception.message}")
        PagingSource.LoadResult.Error(NetworkExceptions.ServerException())
    } catch (exception: Exception) {
        Log.e(TAG, "Exception: ${exception.message}")
        PagingSource.LoadResult.Error(NetworkExceptions.UnknownException())
    }
}
