package com.aait.ui.util.paging

import androidx.paging.PagingConfig

/**
 * Creates a default PagingConfig for common pagination use cases.
 *
 * @param pageSize Number of items per page (default: 20)
 * @param prefetchDistance Load more items when this many items from the end (default: 5)
 * @param enablePlaceholders Show placeholders for unloaded items (default: false)
 * @param initialLoadSize Initial load size, usually equals pageSize (default: 20)
 */
fun getDefaultPagingConfig(
    pageSize: Int = 20,
    prefetchDistance: Int = 5,
    enablePlaceholders: Boolean = false,
    initialLoadSize: Int = 20
) = PagingConfig(
    pageSize = pageSize,
    prefetchDistance = prefetchDistance,
    enablePlaceholders = enablePlaceholders,
    initialLoadSize = initialLoadSize
)
