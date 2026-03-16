package com.aait.ui.util.paging

import androidx.paging.PagingConfig

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
