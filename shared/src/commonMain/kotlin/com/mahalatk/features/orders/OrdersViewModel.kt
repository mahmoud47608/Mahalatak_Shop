package com.mahalatk.features.orders

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class OrderTab { New, Current, Completed, Returns }

enum class OrderStatus { New, Preparing, Delivered, Returned, Cancelled }

@Immutable
data class Order(
    val id: String,
    val customerName: String,
    val orderNumber: String,
    val itemsCount: Int,
    val totalPrice: Double,
    val time: String,
    val date: String,
    val status: OrderStatus,
    val imageUrl: String = "",
)

@Immutable
data class OrdersState(
    val selectedTab: OrderTab = OrderTab.New,
    val orders: List<Order> = listOf(
        Order("1", "Hader Al-Alawi", "088308", 3, 750.0, "02:30 PM", "Today", OrderStatus.New),
        Order("2", "Fahd Al-Shehri", "088309", 1, 250.0, "02:30 PM", "Today", OrderStatus.New),
        Order("3", "Mohamed Ali", "088310", 2, 500.0, "01:15 PM", "Today", OrderStatus.New),
        Order(
            "4",
            "Sara Ahmed",
            "088311",
            4,
            1200.0,
            "11:00 AM",
            "Yesterday",
            OrderStatus.Preparing
        ),
        Order(
            "5",
            "Khalid Omar",
            "088312",
            1,
            320.0,
            "10:30 AM",
            "Yesterday",
            OrderStatus.Preparing
        ),
        Order("6", "Nour Hassan", "088313", 2, 600.0, "09:00 AM", "Mar 25", OrderStatus.Delivered),
        Order("7", "Youssef Kamal", "088314", 3, 900.0, "03:00 PM", "Mar 24", OrderStatus.Returned),
    ),
) {
    val filteredOrders: List<Order>
        get() = when (selectedTab) {
            OrderTab.New -> orders.filter { it.status == OrderStatus.New }
            OrderTab.Current -> orders.filter { it.status == OrderStatus.Preparing }
            OrderTab.Completed -> orders.filter { it.status == OrderStatus.Delivered }
            OrderTab.Returns -> orders.filter { it.status == OrderStatus.Returned || it.status == OrderStatus.Cancelled }
        }
}

class OrdersViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(OrdersState())
    val uiState: StateFlow<OrdersState> = _uiState.asStateFlow()

    fun selectTab(tab: OrderTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }
}
