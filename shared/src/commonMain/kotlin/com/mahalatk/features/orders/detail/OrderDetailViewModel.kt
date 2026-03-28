package com.mahalatk.features.orders.detail

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ── Models ─────────────────────────────────────────────

enum class DetailOrderStep(val index: Int) {
    WaitingPayment(0),
    Preparing(1),
    Ready(2),
    DeliveredToDriver(3),
    Completed(4),
}

@Immutable
data class OrderProduct(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val quantity: Int = 1,
    val imageUrl: String = "",
)

@Immutable
data class OrderDetailState(
    val orderId: String = "",
    val customerName: String = "Hader Al-Alawi",
    val orderNumber: String = "088308",
    val customerPhone: String = "00966521456876",
    val date: String = "Today",
    val time: String = "02:30 PM",
    val customerImageUrl: String = "",

    val currentStep: DetailOrderStep = DetailOrderStep.Preparing,

    val products: List<OrderProduct> = listOf(
        OrderProduct("1", "Flavors of Rose", "A bouquet of flowers chosen with care...", 250.0),
        OrderProduct("2", "Flavors of Rose", "A bouquet of flowers chosen with care...", 250.0),
    ),

    val deliveryAddress: String = "Riyadh, King Fahd Street 233",
    val paymentMethod: String = "Cash",
    val couponCode: String = "SD12",
    val orderType: String = "Normal Order",

    val productsTotal: Double = 1251.0,
    val deliveryFee: Double = 1251.0,
    val vatAmount: Double = 54.0,
    val taxAmount: Double = 54.0,
    val totalAmount: Double = 1251.0,

    val driverName: String = "Fahd Issa",
    val driverPhone: String = "009552228952",

    val customerRating: Int = 0,
    val driverRating: Int = 0,
    val isLoading: Boolean = true,
)

// ── ViewModel ──────────────────────────────────────────

class OrderDetailViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(OrderDetailState())
    val uiState: StateFlow<OrderDetailState> = _uiState.asStateFlow()

    fun loadOrder(orderId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(1500) // Simulate API call
            _uiState.update { it.copy(orderId = orderId, isLoading = false) }
        }
    }

    fun onActionClick() {
        _uiState.update { state ->
            val nextStep = when (state.currentStep) {
                DetailOrderStep.WaitingPayment -> DetailOrderStep.Preparing
                DetailOrderStep.Preparing -> DetailOrderStep.Ready
                DetailOrderStep.Ready -> DetailOrderStep.DeliveredToDriver
                DetailOrderStep.DeliveredToDriver -> DetailOrderStep.Completed
                DetailOrderStep.Completed -> DetailOrderStep.Completed
            }
            state.copy(currentStep = nextStep)
        }
    }

    fun rateCustomer(rating: Int) {
        _uiState.update { it.copy(customerRating = rating) }
    }

    fun rateDriver(rating: Int) {
        _uiState.update { it.copy(driverRating = rating) }
    }
}
