package com.mahalatk.features.notifications

import androidx.compose.runtime.Immutable
import com.mahalatk.base.SimpleViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Immutable
data class NotificationItem(
    val id: String,
    val title: String,
    val body: String,
    val time: String,
    val isRead: Boolean = false,
)

@Immutable
data class NotificationsState(
    val notifications: ImmutableList<NotificationItem> = persistentListOf(
        NotificationItem("1", "Dashboard", "You have a pending order #2225", "05:30 PM"),
        NotificationItem("2", "Royal Bouquet", "Order #5463 status has been updated", "05:30 PM"),
        NotificationItem("3", "New Order", "You received a new order #7891", "04:15 PM", true),
        NotificationItem(
            "4",
            "Spring Collection",
            "Order #3344 has been delivered",
            "02:00 PM",
            true
        ),
        NotificationItem(
            "5",
            "Payment Received",
            "Payment of 450 EGP confirmed for order #5463",
            "Yesterday",
            true
        ),
    ),
)

class NotificationsViewModel : SimpleViewModel<NotificationsState, Nothing>(NotificationsState()) {

    fun markAsRead(id: String) {
        updateState {
            copy(
                notifications = notifications.map {
                    if (it.id == id) it.copy(isRead = true) else it
                }.toImmutableList()
            )
        }
    }

    fun deleteNotification(id: String) {
        updateState {
            copy(notifications = notifications.filter { it.id != id }.toImmutableList())
        }
    }

    fun clearAll() {
        updateState { copy(notifications = persistentListOf()) }
    }
}
