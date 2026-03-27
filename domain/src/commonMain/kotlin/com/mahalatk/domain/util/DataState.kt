package com.mahalatk.domain.util

/**
 * Represents the state of a data operation.
 * Domain-pure: no dependency on BaseResponse or serialization.
 */
sealed class DataState<out R> {
    data class Success<out T>(val data: T) : DataState<T>()
    data class Error(val throwable: Throwable) : DataState<Nothing>()
    data object Loading : DataState<Nothing>()
    data object Idle : DataState<Nothing>()
}
