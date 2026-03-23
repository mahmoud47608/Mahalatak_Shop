package com.mahalatk.fcm

import com.mahalatk.base.managers.SessionManager
import com.mahalatk.domain.repository.PreferenceRepository
import com.mahalatk.domain.util.fromJson
import com.mahalatk.domain.util.toJson
import com.mahalatk.ui.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FcmEventHandler(
    private val sessionManager: SessionManager,
    private val preferenceRepository: PreferenceRepository,
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    fun handleNotificationData(data: Map<String, String>) {
        if (data.isEmpty()) return

        scope.launch { sessionManager.emitFcmUpdate(data) }

        val item = data.toJson().fromJson<NotificationItem>()
        when (item.type) {
            NotificationKey.ACCOUNT_BLOCK, NotificationKey.ACCOUNT_DELETED -> {
                scope.launch {
                    preferenceRepository.onLogout()
                    sessionManager.setAuthFailed(true)
                }
            }
        }
    }

    fun getLocalizedContent(
        item: NotificationItem,
        language: String,
    ): Pair<String, String> {
        return if (language == Constants.ARABIC) {
            (item.titleAr ?: "") to (item.bodyAr ?: "")
        } else {
            (item.titleEn ?: "") to (item.bodyEn ?: "")
        }
    }

    fun parseNotification(data: Map<String, String>): NotificationItem {
        return data.toJson().fromJson<NotificationItem>()
    }

    suspend fun getCurrentLanguage(): String {
        return preferenceRepository.getLanguage().first()
    }
}
