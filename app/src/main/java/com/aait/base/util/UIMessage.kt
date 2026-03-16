package com.aait.base.util

sealed class UIMessage {
    data class Text(val message: String) : UIMessage()
    data class Resource(val messageResId: Int) : UIMessage()
}