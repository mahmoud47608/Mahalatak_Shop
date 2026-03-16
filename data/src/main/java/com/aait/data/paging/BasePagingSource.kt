package com.aait.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.aait.data.util.handlePagingResponse
import com.aait.domain.entity.base.BaseResponse
import com.aait.domain.entity.base.Pagination
import com.aait.domain.entity.base.PaginationResponse

private const val TAG = "BasePagingSource"

/**
 * Base PagingSource for handling paginated API responses.
 *
 * @param T The type of items in the list
 *
 * Usage example:
 * ```
 * class NotificationsPagingSource(
 *     private val endpoint: NotificationsEndPoint
 * ) : BasePagingSource<NotificationModel>() {
 *
 *     override suspend fun fetchPage(page: Int, pageSize: Int): PageResult<NotificationModel> {
 *         val response = endpoint.getNotifications(page = page, perPage = pageSize)
 *         return PageResult(
 *             response = response,
 *             list = response.data?.data,
 *             pagination = response.data?.pagination
 *         )
 *     }
 * }
 * ```
 */
abstract class BasePagingSource<T : Any> : PagingSource<Int, T>() {

    /**
     * Override this to fetch a page from your API endpoint.
     *
     * @param page The page number to fetch (1-indexed)
     * @param pageSize The number of items per page
     * @return A [PageResult] containing the response, list, and pagination data
     */
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

    /**
     * Container for API response data needed for pagination.
     */
    data class PageResult<T>(
        val response: BaseResponse<*>,
        val list: List<T>?,
        val pagination: Pagination?
    )
}

/**
 * Extension function to create a PageResult from a PaginationResponse.
 *
 * Usage:
 * ```
 * override suspend fun fetchPage(page: Int, pageSize: Int): PageResult<Item> {
 *     val response = endpoint.getItems(page, pageSize)
 *     return response.toPageResult(response.data)
 * }
 * ```
 */
fun <T> BaseResponse<PaginationResponse<T>>.toPageResult(): BasePagingSource.PageResult<T> {
    return BasePagingSource.PageResult(
        response = this,
        list = this.data?.data,
        pagination = this.data?.pagination
    )
}
