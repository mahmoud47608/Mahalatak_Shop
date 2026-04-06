package com.mahalatk.features.home

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.mahalatk.base.SimpleViewModel
import com.mahalatk.base.UserDataProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@Immutable
data class OrderItem(
    val id: String,
    val customerName: String,
    val orderNumber: String,
    val time: String,
    val imageUrl: String = "",
)

@Immutable
data class HomeState(
    val userName: String = "Ahmed Mohamed",
    val userImage: String = "",
    val receiveNewOrders: Boolean = true,
    val completedOrders: Int = 35,
    val cancelledOrders: Int = 18,
    val newOrders: ImmutableList<OrderItem> = persistentListOf(
        OrderItem("1", "Hader Al-Alawi", "088308", "02:30 PM"),
        OrderItem("2", "Fahd Al-Shehri", "088309", "02:30 PM"),
        OrderItem("3", "Hader Al-Alawi", "088310", "01:15 PM"),
    ),
) {
    val totalOrders: Int get() = completedOrders + cancelledOrders
    val completedProgress: Float get() = if (totalOrders > 0) completedOrders.toFloat() / totalOrders else 0f
    val cancelledProgress: Float get() = if (totalOrders > 0) cancelledOrders.toFloat() / totalOrders else 0f
}

class HomeViewModel(
    private val userDataProvider: UserDataProvider,
) : SimpleViewModel<HomeState, Nothing>(HomeState()) {

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            val name = userDataProvider.getUserName()
            val image = userDataProvider.getUserImage()
            updateState {
                copy(
                    userName = name.ifEmpty { userName },
                    userImage = image,
                )
            }
        }
    }

    fun toggleReceiveOrders(enabled: Boolean) {
        updateState { copy(receiveNewOrders = enabled) }
    }
}
