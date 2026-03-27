package com.mahalatk.base

import androidx.compose.runtime.Immutable

/**
 * Generic wrapper for simple loading/success/error states.
 * Use feature-specific @Immutable data classes for complex screens.
 */
@Immutable
data class BaseState<out T>(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val data: T? = null,
)
