package com.aait.base.cycles.more.test

import androidx.lifecycle.ViewModel
import com.aait.base.fcm.NotificationHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FCMTestViewModel @Inject constructor(
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
