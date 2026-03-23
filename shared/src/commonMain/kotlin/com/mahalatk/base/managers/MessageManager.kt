package com.mahalatk.base.managers

import com.mahalatk.util.UIMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class MessageManager {

    private val _messages = MutableSharedFlow<UIMessage>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val messages: SharedFlow<UIMessage> = _messages.asSharedFlow()

    suspend fun show(message: String) {
        _messages.emit(UIMessage.Text(message))
    }

    suspend fun showByKey(messageKey: String) {
        _messages.emit(UIMessage.StringKey(messageKey))
    }
}
