package com.mahalatk.features.profile.employee

import androidx.lifecycle.viewModelScope
import com.mahalatk.base.BaseViewModel
import com.mahalatk.base.UserDataProvider
import com.mahalatk.base.managers.LoadingManager
import com.mahalatk.base.managers.MessageManager
import kotlinx.coroutines.launch

class EmployeeProfileViewModel(
    loadingManager: LoadingManager,
    messageManager: MessageManager,
    private val userDataProvider: UserDataProvider,
) : BaseViewModel<EmployeeProfileState, Nothing>(
    EmployeeProfileState(),
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
                    employeeName = authData.name
                        ?: "${authData.firstName.orEmpty()} ${authData.lastName.orEmpty()}".trim(),
                    employeeImageUrl = authData.image.orEmpty(),
                )
            }
        }
    }

    fun selectShop(shop: String) {
        updateState { copy(selectedShop = shop, selectedShopError = null) }
    }

    fun saveProfile() {
        // TODO: Add validation and API call to update employee profile
    }
}
