package com.mahalatk.ui.base

data class BaseState<T : Any?>(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val data: T? = null
)
