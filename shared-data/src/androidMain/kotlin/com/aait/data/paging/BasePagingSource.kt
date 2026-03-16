package com.aait.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.aait.data.util.handlePagingResponse
import com.aait.domain.entity.base.BaseResponse
import com.aait.domain.entity.base.Pagination
import com.aait.domain.entity.base.PaginationResponse

private const val TAG = "BasePagingSource"

abstract class BasePagingSource<T : Any> : PagingSource<Int, T>() {

    protected abstract suspend fun fetchPage(page: Int, pageSize: Int): PageResult<T>

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return try {
            val pageIndex = params.key ?: 1
            val result = fetchPage(pageIndex, params.loadSize)

            handlePagingResponse(
                pageIndex = pageIndex,
                response = result.response,
                list = result.list,
                pagination = result.pagination
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error loading page: ${e.localizedMessage}", e)
            LoadResult.Error(e)
        }
    }

    data class PageResult<T>(
        val response: BaseResponse<*>,
        val list: List<T>?,
        val pagination: Pagination?
    )
}

fun <T> BaseResponse<PaginationResponse<T>>.toPageResult(): BasePagingSource.PageResult<T> {
    return BasePagingSource.PageResult(
        response = this,
        list = this.data?.data,
        pagination = this.data?.pagination
    )
}
