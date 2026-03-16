package com.aait.base.ui

import com.aait.base.common.component.toolbar.ToolBarState
import com.aait.base.util.UIMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UIRepo @Inject constructor() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // One-time UI messages (snack bar/toast)
    private val _uiMessage = MutableSharedFlow<UIMessage>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val uiMessage: SharedFlow<UIMessage> = _uiMessage.asSharedFlow()

    fun showLoading() {
        _isLoading.value = true
    }

    fun hideLoading() {
        _isLoading.value = false
    }

    suspend fun showMsg(message: String) {
        _uiMessage.emit(UIMessage.Text(message))
    }

    suspend fun showMsg(messageResId: Int) {
        _uiMessage.emit(UIMessage.Resource(messageResId))
    }

    private val _fcmUpdate = MutableSharedFlow<Map<String, String>>(replay = 0, extraBufferCapacity = 1)
    val fcmUpdate = _fcmUpdate.asSharedFlow()

    suspend fun emitFcmUpdate(data: Map<String, String>) {
        _fcmUpdate.emit(data)
    }

    private val _isAuthFailed = MutableStateFlow(false)
    val isAuthFailed: StateFlow<Boolean> = _isAuthFailed.asStateFlow()

    private val _isBlocked = MutableStateFlow(false)
    val isBlocked: StateFlow<Boolean> = _isBlocked.asStateFlow()

    fun setAuthFailed(value: Boolean) {
        _isAuthFailed.value = value
    }

    fun setBlocked(value: Boolean) {
        _isBlocked.value = value
    }

}
