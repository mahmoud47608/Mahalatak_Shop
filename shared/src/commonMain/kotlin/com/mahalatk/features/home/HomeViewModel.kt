package com.mahalatk.features.home

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahalatk.base.UserDataProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
    val newOrders: List<OrderItem> = listOf(
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
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            val name = userDataProvider.getUserName()
            val image = userDataProvider.getUserImage()
            _uiState.update {
                it.copy(
                    userName = name.ifEmpty { it.userName },
                    userImage = image,
                )
            }
        }
    }

    fun toggleReceiveOrders(enabled: Boolean) {
        _uiState.update { it.copy(receiveNewOrders = enabled) }
    }

}
