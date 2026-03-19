package com.mahalatk.domain.util

import com.mahalatk.domain.entity.base.BaseResponse

sealed class DataState<out R> {
    data class Success<out T>(val data: T) : DataState<T>()
    data class Error(val throwable: Throwable, val data: BaseResponse<*>? = null) :
        DataState<Nothing>()

    data object Loading : DataState<Nothing>()
    data object Idle : DataState<Nothing>()
}
