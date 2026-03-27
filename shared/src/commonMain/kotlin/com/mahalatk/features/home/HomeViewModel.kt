package com.mahalatk.features.home

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahalatk.domain.entity.AuthData
import com.mahalatk.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

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
    val selfDelivery: Boolean = false,
    val completedOrders: Int = 35,
    val cancelledOrders: Int = 18,
    val newOrders: List<OrderItem> = listOf(
        OrderItem("1", "Hader Al-Alawi", "088308", "02:30 PM"),
        OrderItem("2", "Fahd Al-Shehri", "088309", "02:30 PM"),
        OrderItem("3", "Hader Al-Alawi", "088310", "01:15 PM"),
    ),
)

class HomeViewModel(
    private val preferenceRepository: PreferenceRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()

    private val json = Json { ignoreUnknownKeys = true }

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            val userData = preferenceRepository.getUserData().firstOrNull()
            if (!userData.isNullOrEmpty()) {
                try {
                    val authData = json.decodeFromString<AuthData>(userData)
                    val user = authData.user
                    _uiState.update {
                        it.copy(
                            userName = "${user?.firstName.orEmpty()} ${user?.lastName.orEmpty()}".trim()
                                .ifEmpty { it.userName },
                            userImage = user?.image.orEmpty(),
                        )
                    }
                } catch (_: Exception) {
                }
            }
        }
    }

    fun toggleReceiveOrders(enabled: Boolean) {
        _uiState.update { it.copy(receiveNewOrders = enabled) }
    }

    fun toggleSelfDelivery(enabled: Boolean) {
        _uiState.update { it.copy(selfDelivery = enabled) }
    }
}
