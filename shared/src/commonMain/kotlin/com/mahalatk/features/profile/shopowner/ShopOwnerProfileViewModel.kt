package com.mahalatk.features.profile.shopowner

import androidx.lifecycle.viewModelScope
import com.mahalatk.base.BaseViewModel
import com.mahalatk.base.UserDataProvider
import com.mahalatk.base.managers.LoadingManager
import com.mahalatk.base.managers.MessageManager
import com.mahalatk.features.auth.register.CityItem
import com.mahalatk.features.auth.register.ReturnPeriod
import com.mahalatk.features.auth.register.ReturnPolicy
import com.mahalatk.features.auth.register.ShopCategory
import com.mahalatk.features.auth.register.ShopType
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.launch

class ShopOwnerProfileViewModel(
    loadingManager: LoadingManager,
    messageManager: MessageManager,
    private val userDataProvider: UserDataProvider,
) : BaseViewModel<ShopOwnerProfileState, Nothing>(
    ShopOwnerProfileState(),
    loadingManager,
    messageManager,
) {

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            val authData = userDataProvider.getAuthData() ?: return@launch
            updateState {
                copy(
                    ownerName = authData.name
                        ?: "${authData.firstName.orEmpty()} ${authData.lastName.orEmpty()}".trim(),
                    phone = authData.fullPhone ?: authData.phone.orEmpty(),
                    shopImageUrl = authData.image.orEmpty(),
                    selectedCity = authData.city?.let { CityItem(it.id ?: 0, it.name.orEmpty()) },
                )
            }
        }
    }

    fun toggleCategory(category: ShopCategory) {
        updateState {
            val updated = if (category in selectedCategories) {
                (selectedCategories - category).toImmutableSet()
            } else {
                (selectedCategories + category).toImmutableSet()
            }
            copy(selectedCategories = updated, categoryError = null)
        }
    }

    fun selectShopType(type: ShopType) {
        updateState { copy(shopType = type) }
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

    fun saveProfile() {
        // TODO: Add validation and API call to update shop owner profile
    }
}
