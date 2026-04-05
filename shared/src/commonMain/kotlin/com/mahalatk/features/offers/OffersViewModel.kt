package com.mahalatk.features.offers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahalatk.features.offers.add.Offer
import com.mahalatk.features.offers.add.OfferType
import com.mahalatk.features.offers.add.OffersState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OffersViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        OffersState(
            offers = persistentListOf(
                Offer(
                    "1",
                    OfferType.DISCOUNT,
                    "خصم 20% على جميع المنتجات",
                    "01/04/2026",
                    "15/04/2026"
                ),
                Offer(
                    "2",
                    OfferType.BUY_X_GET_Y,
                    "اشتري 2 واحصل على 1 مجاناً - قسم الأحذية",
                    "05/04/2026",
                    "20/04/2026"
                ),
                Offer(
                    "3",
                    OfferType.PACKAGE,
                    "باكدج طقم العريس - قميص وبنطلون وكرافتة بـ 800 ج",
                    "10/04/2026",
                    "30/04/2026",
                    false
                ),
            ),
        ),
    )
    val uiState: StateFlow<OffersState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            delay(500)
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun deleteOffer(id: String) {
        _uiState.update { state ->
            state.copy(offers = state.offers.filter { it.id != id }.toImmutableList())
        }
    }

    fun toggleOfferActive(id: String) {
        _uiState.update { state ->
            state.copy(
                offers = state.offers.map { offer ->
                    if (offer.id == id) offer.copy(isActive = !offer.isActive) else offer
                }.toImmutableList(),
            )
        }
    }
}
