package com.mahalatk.ui.util

sealed class UIMessage {
    data class Text(val message: String) : UIMessage()
    data class StringKey(val key: String) : UIMessage()
}
