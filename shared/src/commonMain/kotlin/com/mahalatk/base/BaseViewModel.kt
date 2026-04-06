package com.mahalatk.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahalatk.base.managers.LoadingManager
import com.mahalatk.base.managers.MessageManager
import com.mahalatk.util.NetworkExtensionsActions
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

open class BaseViewModel<UiState : Any, UiEvent : Any>(
    initialState: UiState,
    protected val loadingManager: LoadingManager,
    protected val messageManager: MessageManager,
) : ViewModel(), NetworkExtensionsActions {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events: Flow<UiEvent> = _events.receiveAsFlow()

    fun updateState(reducer: UiState.() -> UiState) {
        _uiState.update { it.reducer() }
    }

    protected fun sendEvent(event: UiEvent) {
        viewModelScope.launch { _events.send(event) }
    }

    override fun onLoad(showLoading: Boolean) {
        if (showLoading) loadingManager.show()
        else loadingManager.hide()
    }

    override fun onCommonError(errorKey: String) {
        onLoad(false)
        viewModelScope.launch { messageManager.showByKey(errorKey) }
    }

    override fun onShowSuccessToast(msg: String?) {
        msg?.takeIf { it.isNotEmpty() }?.let {
            viewModelScope.launch { messageManager.show(it) }
        }
    }

    override fun onFail(msg: String?) {
        msg?.takeIf { it.isNotEmpty() }?.let {
            viewModelScope.launch { messageManager.show(it) }
        }
    }
}
