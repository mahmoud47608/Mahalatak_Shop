package com.aait.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aait.ui.managers.LoadingManager
import com.aait.ui.managers.MessageManager
import com.aait.ui.util.NetworkExtensionsActions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

open class BaseViewModel<UiState : Any>(
    initialState: UiState,
    protected val loadingManager: LoadingManager,
    protected val messageManager: MessageManager,
) : ViewModel(), NetworkExtensionsActions {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun updateState(reducer: UiState.() -> UiState) {
        _uiState.update { it.reducer() }
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
