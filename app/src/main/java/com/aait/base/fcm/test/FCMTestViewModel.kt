package com.aait.base.fcm.test

import androidx.lifecycle.ViewModel
import com.aait.base.fcm.NotificationHandler

class FCMTestViewModel(
    private val notificationHandler: NotificationHandler
) : ViewModel() {

    fun testNotification(type: String, extraData: Map<String, String> = emptyMap()) {
        val data = mutableMapOf(
            "type" to type,
            "title_en" to "Test Title $type",
            "body_en" to "Test Body $type",
            "title_ar" to "Test Title AR",
            "body_ar" to "Test Body AR"
        )
        data.putAll(extraData)
        notificationHandler.handleNotification(data)
    }
}
