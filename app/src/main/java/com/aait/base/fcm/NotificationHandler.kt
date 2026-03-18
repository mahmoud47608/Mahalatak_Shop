package com.aait.base.fcm

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.aait.base.App
import com.aait.base.MainActivity
import com.aait.base.fcm.NotificationKey.ACCEPT_JOIN_REQUEST
import com.aait.base.fcm.NotificationKey.ACCOUNT_BLOCK
import com.aait.base.fcm.NotificationKey.ACCOUNT_DELETED
import com.aait.base.fcm.NotificationKey.NEW_MESSAGE
import com.aait.base.ui.UIRepo
import com.aait.base.util.Constants
import com.aait.domain.repository.PreferenceRepository
import com.aait.domain.util.fromJson
import com.aait.domain.util.toJson
import com.mahalatak.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.atomic.AtomicInteger

class NotificationHandler(
    private val context: Context,
    private val preferenceRepository: PreferenceRepository,
    private val uiRepo: UIRepo
) {

    private var notificationManager: NotificationManager? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

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
        if (data.isNotEmpty()) {
            EventBus.getDefault().post(data)
            scope.launch(Dispatchers.Main) {
                uiRepo.emitFcmUpdate(data)
            }
            displayNotification(data)
        }
    }

    private fun displayNotification(data: Map<String, String>) {
        var title: String?
        var message: String?

        val notificationItem = data.toJson().fromJson<NotificationItem>()

        scope.launch(Dispatchers.IO) {
            if (preferenceRepository.getLanguage().first() == Constants.ARABIC) {
                title = notificationItem.titleAr ?: ""
                message = notificationItem.bodyAr ?: ""
            } else {
                title = notificationItem.titleEn ?: ""
                message = notificationItem.bodyEn ?: ""
            }

            showNotification(
                title = title,
                message = message,
                pendingIntent = getPendingIntent(notificationItem)
            )
        }

        // Handle immediate navigation for blocking actions
        when (notificationItem.type) {
            ACCOUNT_BLOCK, ACCOUNT_DELETED -> {
                scope.launch(Dispatchers.IO) {
                    preferenceRepository.onLogout()
                    runCatching { navigateToLoginIntent().send() }
                }
            }
        }
    }

    private fun getPendingIntent(notificationItem: NotificationItem): PendingIntent {
        return when (notificationItem.type) {
            // Navigate to login
            ACCOUNT_BLOCK, ACCOUNT_DELETED -> navigateToLoginIntent()

            // Navigate to home
            ACCEPT_JOIN_REQUEST -> navigateToHomeIntent()

            // Navigate to Chat
            NEW_MESSAGE -> navigateToChatIntent(notificationItem)

            else -> navigateToNotificationsIntent()
        }
    }

    private fun navigateToLoginIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            action = ACTION_NAVIGATE_TO_LOGIN
        }
        return PendingIntent.getActivity(
            context,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun navigateToHomeIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
            action = ACTION_NAVIGATE_TO_HOME
        }
        return PendingIntent.getActivity(
            context,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun navigateToNotificationsIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
            action = ACTION_NAVIGATE_TO_NOTIFICATIONS
        }
        return PendingIntent.getActivity(
            context,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
    private fun navigateToChatIntent(notificationItem: NotificationItem): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
            action = ACTION_NAVIGATE_TO_CHAT
            putExtra(EXTRA_CHAT_ROOM_ID, notificationItem.roomId?.toIntOrNull() ?: 0)
            // Use titleEn as default title, or empty
            putExtra(EXTRA_CHAT_TITLE, notificationItem.titleEn ?: "")
        }
        return PendingIntent.getActivity(
            context,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun showNotification(
        title: String?,
        message: String?,
        pendingIntent: PendingIntent
    ) {
        val builder = NotificationCompat.Builder(context, App.CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setDefaults(Notification.DEFAULT_LIGHTS)
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pendingIntent)

        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager?.notify(notificationIdGenerator.incrementAndGet(), builder.build())
    }
}
