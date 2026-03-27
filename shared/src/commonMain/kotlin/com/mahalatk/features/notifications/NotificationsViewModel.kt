package com.mahalatk.features.notifications

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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
    val notifications: List<NotificationItem> = listOf(
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

class NotificationsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsState())
    val uiState: StateFlow<NotificationsState> = _uiState.asStateFlow()

    fun markAsRead(id: String) {
        _uiState.update { state ->
            state.copy(
                notifications = state.notifications.map {
                    if (it.id == id) it.copy(isRead = true) else it
                }
            )
        }
    }

    fun deleteNotification(id: String) {
        _uiState.update { state ->
            state.copy(notifications = state.notifications.filter { it.id != id })
        }
    }

    fun clearAll() {
        _uiState.update { it.copy(notifications = emptyList()) }
    }
}
