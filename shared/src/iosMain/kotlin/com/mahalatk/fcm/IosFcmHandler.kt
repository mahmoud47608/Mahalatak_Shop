package com.mahalatk.fcm

class IosFcmHandler(private val fcmEventHandler: FcmEventHandler) {

    fun onNotificationReceived(data: Map<String, String>) {
        fcmEventHandler.handleNotificationData(data)
    }

    fun parseNotification(data: Map<String, String>): NotificationItem {
        return fcmEventHandler.parseNotification(data)
    }

    suspend fun getLocalizedContent(item: NotificationItem): Pair<String, String> {
        val language = fcmEventHandler.getCurrentLanguage()
        return fcmEventHandler.getLocalizedContent(item, language)
    }
}
