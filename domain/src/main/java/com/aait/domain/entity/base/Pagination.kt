package com.aait.domain.entity.base

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pagination(
    @SerialName("per_page") val perPage: Int? = null,
    @SerialName("total") val total: Int? = null,
    @SerialName("total_pages") val totalPages: Int? = null,
    @SerialName("last_page") val lastPage: Int? = null,
    @SerialName("count") val count: Int? = null,
    @SerialName("current_page") val currentPage: Int? = null,
    @SerialName("next_page_url") val nextPageUrl: String? = null
) {
    val hasNextPage: Boolean
        get() = currentPage != null && currentPage < (totalPages ?: 1)
}

@Serializable
data class PaginationResponse<T>(
    @SerialName("data") val data: List<T>? = null,
    val pagination: Pagination? = null
)