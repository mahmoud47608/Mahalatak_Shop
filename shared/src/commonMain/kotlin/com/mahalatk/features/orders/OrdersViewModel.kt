package com.mahalatk.features.orders

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
    val isLoading: Boolean = true,
    val selectedTab: OrderTab = OrderTab.New,
    val orders: ImmutableList<Order> = persistentListOf(
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
)

class OrdersViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(OrdersState())

    init {
        viewModelScope.launch {
            delay(1000)
            _uiState.update { it.copy(isLoading = false) }
        }
    }
    val uiState: StateFlow<OrdersState> = _uiState.asStateFlow()

    val filteredOrders: StateFlow<ImmutableList<Order>> = _uiState.map { state ->
        when (state.selectedTab) {
            OrderTab.New -> state.orders.filter { it.status == OrderStatus.New }.toImmutableList()
            OrderTab.Current -> state.orders.filter { it.status == OrderStatus.Preparing }
                .toImmutableList()

            OrderTab.Completed -> state.orders.filter { it.status == OrderStatus.Delivered }
                .toImmutableList()

            OrderTab.Returns -> state.orders.filter { it.status == OrderStatus.Returned || it.status == OrderStatus.Cancelled }
                .toImmutableList()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), persistentListOf())

    fun selectTab(tab: OrderTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }
}
