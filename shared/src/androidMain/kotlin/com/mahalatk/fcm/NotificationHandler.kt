package com.mahalatk.fcm

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

class NotificationHandler(
    private val context: Context,
    private val fcmEventHandler: FcmEventHandler,
    private val notificationActivityClass: Class<*> = context::class.java,
) {

    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    fun destroy() {
        scope.cancel()
    }

    companion object {
        private val notificationIdGenerator = AtomicInteger(1000)
        const val ACTION_NAVIGATE_TO_LOGIN = "NAVIGATE_TO_LOGIN"
        const val ACTION_NAVIGATE_TO_HOME = "NAVIGATE_TO_HOME"
        const val ACTION_NAVIGATE_TO_NOTIFICATIONS = "NAVIGATE_TO_NOTIFICATIONS"
        const val ACTION_NAVIGATE_TO_CHAT = "NAVIGATE_TO_CHAT"
        const val EXTRA_CHAT_ROOM_ID = "CHAT_ROOM_ID"
        const val EXTRA_CHAT_TITLE = "CHAT_TITLE"
    }

    fun handleNotification(data: Map<String, String>) {
        if (data.isEmpty()) return

        fcmEventHandler.handleNotificationData(data)
        displayNotification(data)
    }

    private fun displayNotification(data: Map<String, String>) {
        val item = fcmEventHandler.parseNotification(data)

        scope.launch(Dispatchers.IO) {
            val language = fcmEventHandler.getCurrentLanguage()
            val (title, message) = fcmEventHandler.getLocalizedContent(item, language)

            showNotification(
                title = title,
                message = message,
                pendingIntent = getPendingIntent(item),
            )
        }
    }

    private fun getPendingIntent(item: NotificationItem): PendingIntent {
        return when (item.type) {
            NotificationKey.ACCOUNT_BLOCK,
            NotificationKey.ACCOUNT_DELETED -> navigateToLoginIntent()

            NotificationKey.ACCEPT_JOIN_REQUEST -> navigateToHomeIntent()

            NotificationKey.NEW_MESSAGE -> navigateToChatIntent(item)

            else -> navigateToNotificationsIntent()
        }
    }

    private fun navigateToLoginIntent(): PendingIntent {
        val intent = Intent(context, notificationActivityClass).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            action = ACTION_NAVIGATE_TO_LOGIN
        }
        return PendingIntent.getActivity(
            context,
            notificationIdGenerator.incrementAndGet(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    private fun navigateToHomeIntent(): PendingIntent {
        val intent = Intent(context, notificationActivityClass).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
            action = ACTION_NAVIGATE_TO_HOME
        }
        return PendingIntent.getActivity(
            context,
            notificationIdGenerator.incrementAndGet(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    private fun navigateToNotificationsIntent(): PendingIntent {
        val intent = Intent(context, notificationActivityClass).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
            action = ACTION_NAVIGATE_TO_NOTIFICATIONS
        }
        return PendingIntent.getActivity(
            context,
            notificationIdGenerator.incrementAndGet(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    private fun navigateToChatIntent(item: NotificationItem): PendingIntent {
        val intent = Intent(context, notificationActivityClass).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
            action = ACTION_NAVIGATE_TO_CHAT
            putExtra(EXTRA_CHAT_ROOM_ID, item.roomId ?: 0)
            putExtra(EXTRA_CHAT_TITLE, item.titleEn ?: "")
        }
        return PendingIntent.getActivity(
            context,
            notificationIdGenerator.incrementAndGet(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    private fun showNotification(
        title: String?,
        message: String?,
        pendingIntent: PendingIntent,
    ) {
        val builder = NotificationCompat.Builder(context, NotificationChannelConfig.CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setDefaults(Notification.DEFAULT_LIGHTS)
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pendingIntent)

        notificationManager.notify(notificationIdGenerator.incrementAndGet(), builder.build())
    }
}
