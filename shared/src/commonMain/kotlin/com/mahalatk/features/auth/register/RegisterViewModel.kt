package com.mahalatk.features.auth.register

import com.mahalatk.base.BaseViewModel
import com.mahalatk.base.managers.LoadingManager
import com.mahalatk.base.managers.MessageManager
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.please_enter_phone_number

class RegisterViewModel(
    loadingManager: LoadingManager,
    messageManager: MessageManager,
) : BaseViewModel<RegisterState>(
    RegisterState(),
    loadingManager,
    messageManager,
) {

    fun switchAccountType(type: AccountType) {
        updateState {
            RegisterState(accountType = type)
        }
    }

    fun toggleCategory(category: ShopCategory) {
        updateState {
            val updated = if (category in selectedCategories) {
                selectedCategories - category
            } else {
                selectedCategories + category
            }
            copy(selectedCategories = updated, categoryError = null)
        }
    }

    fun selectCity(city: CityItem) {
        updateState { copy(selectedCity = city, cityError = null) }
    }

    fun updateLocation(lat: Double, lng: Double, address: String) {
        updateState {
            copy(
                locationLat = lat,
                locationLng = lng,
                locationAddress = address,
                locationError = null,
            )
        }
    }

    fun selectReturnPolicy(policy: ReturnPolicy) {
        updateState { copy(returnPolicy = policy) }
    }

    fun selectReturnPeriod(period: ReturnPeriod) {
        updateState { copy(returnPeriod = period) }
    }

    fun selectShop(shop: String) {
        updateState { copy(selectedShop = shop, selectedShopError = null) }
    }

    fun register() {
        // Clear previous errors
        updateState { copy(mobileError = null) }

        val state = uiState.value

        // Temporarily validate mobile only
        if (state.mobile.isBlank() || state.mobile.length < 9) {
            updateState { copy(mobileError = Res.string.please_enter_phone_number) }
            return
        }

        // TODO: Add back full validation and call register use case
    }
}
