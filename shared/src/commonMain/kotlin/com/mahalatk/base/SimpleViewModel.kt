package com.mahalatk.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Lightweight ViewModel base for screens that don't need
 * LoadingManager, MessageManager, or network integration.
 *
 * Provides:
 * - [uiState] / [updateState] for reactive UI state
 * - [events] / [sendEvent] for one-time effects (navigation, toasts)
 */
open class SimpleViewModel<UiState : Any, UiEvent : Any>(
    initialState: UiState,
) : ViewModel() {

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
}
