package com.aait.base.fcm

import android.util.Log
import com.aait.domain.repository.PreferenceRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FirebaseMessagingReceiver : FirebaseMessagingService() {

    @Inject
    lateinit var preferenceRepository: PreferenceRepository

    @Inject
    lateinit var notificationHandler: NotificationHandler

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d("TAG", "onMessageReceived: " + remoteMessage.data)
        Log.d("TAG", "onMessageReceived: " + remoteMessage.notification)

        notificationHandler.handleNotification(remoteMessage.data)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        serviceScope.launch {
            preferenceRepository.setFirebaseToken(token)
        }
    }

    override fun onDestroy() {
        serviceScope.cancel()
        super.onDestroy()
    }
}
