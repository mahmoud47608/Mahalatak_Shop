package com.mahalatk.base.managers

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class SessionManager {

    private val _isAuthFailed = MutableStateFlow(false)
    val isAuthFailed: StateFlow<Boolean> = _isAuthFailed.asStateFlow()

    private val _isBlocked = MutableStateFlow(false)
    val isBlocked: StateFlow<Boolean> = _isBlocked.asStateFlow()

    private val _fcmUpdate = MutableSharedFlow<Map<String, String>>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val fcmUpdate: SharedFlow<Map<String, String>> = _fcmUpdate.asSharedFlow()

    fun setAuthFailed(value: Boolean) {
        _isAuthFailed.value = value
    }

    fun setBlocked(value: Boolean) {
        _isBlocked.value = value
    }

    suspend fun emitFcmUpdate(data: Map<String, String>) {
        _fcmUpdate.emit(data)
    }
}
