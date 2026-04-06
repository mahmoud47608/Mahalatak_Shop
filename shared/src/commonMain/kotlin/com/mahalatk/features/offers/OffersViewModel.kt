package com.mahalatk.features.offers

import androidx.lifecycle.viewModelScope
import com.mahalatk.base.SimpleViewModel
import com.mahalatk.features.offers.add.Offer
import com.mahalatk.features.offers.add.OfferType
import com.mahalatk.features.offers.add.OffersState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OffersViewModel : SimpleViewModel<OffersState, Nothing>(
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
) {

    init {
        viewModelScope.launch {
            delay(500)
            updateState { copy(isLoading = false) }
        }
    }

    fun deleteOffer(id: String) {
        updateState { copy(offers = offers.filter { it.id != id }.toImmutableList()) }
    }

    fun toggleOfferActive(id: String) {
        updateState {
            copy(
                offers = offers.map { offer ->
                    if (offer.id == id) offer.copy(isActive = !offer.isActive) else offer
                }.toImmutableList(),
            )
        }
    }
}
