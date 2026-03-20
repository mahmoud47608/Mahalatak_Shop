package com.mahalatk.features.auth.register

import com.mahalatk.ui.base.BaseViewModel
import com.mahalatk.ui.managers.LoadingManager
import com.mahalatk.ui.managers.MessageManager
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.passwords_do_not_match
import mahalatk.shared.generated.resources.please_enter_confirm_password
import mahalatk.shared.generated.resources.please_enter_employee_name
import mahalatk.shared.generated.resources.please_enter_owner_name
import mahalatk.shared.generated.resources.please_enter_password
import mahalatk.shared.generated.resources.please_enter_phone_number
import mahalatk.shared.generated.resources.please_enter_shop_name
import mahalatk.shared.generated.resources.please_select_category
import mahalatk.shared.generated.resources.please_select_delivery_type
import mahalatk.shared.generated.resources.please_select_shop

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

    fun selectDeliveryType(type: DeliveryType) {
        updateState { copy(deliveryType = type, deliveryTypeError = null) }
    }

    fun selectShop(shop: String) {
        updateState { copy(selectedShop = shop, selectedShopError = null) }
    }

    fun register() {
        val state = uiState.value

        // Clear previous errors
        updateState {
            copy(
                shopNameError = null,
                ownerNameError = null,
                categoryError = null,
                deliveryTypeError = null,
                employeeNameError = null,
                selectedShopError = null,
                mobileError = null,
                passwordError = null,
                confirmPasswordError = null,
            )
        }

        var hasError = false

        when (state.accountType) {
            AccountType.SHOP_OWNER -> {
                if (state.shopName.isBlank()) {
                    updateState { copy(shopNameError = Res.string.please_enter_shop_name) }
                    hasError = true
                }
                if (state.ownerName.isBlank()) {
                    updateState { copy(ownerNameError = Res.string.please_enter_owner_name) }
                    hasError = true
                }
                if (state.selectedCategories.isEmpty()) {
                    updateState { copy(categoryError = Res.string.please_select_category) }
                    hasError = true
                }
                if (state.deliveryType == null) {
                    updateState { copy(deliveryTypeError = Res.string.please_select_delivery_type) }
                    hasError = true
                }
            }

            AccountType.EMPLOYEE -> {
                if (state.employeeName.isBlank()) {
                    updateState { copy(employeeNameError = Res.string.please_enter_employee_name) }
                    hasError = true
                }
                if (state.selectedShop == null) {
                    updateState { copy(selectedShopError = Res.string.please_select_shop) }
                    hasError = true
                }
            }
        }

        // Common validation
        if (state.mobile.isBlank() || state.mobile.length < 9) {
            updateState { copy(mobileError = Res.string.please_enter_phone_number) }
            hasError = true
        }

        if (state.password.isBlank() || state.password.length < 8) {
            updateState { copy(passwordError = Res.string.please_enter_password) }
            hasError = true
        }

        if (state.confirmPassword.isBlank()) {
            updateState { copy(confirmPasswordError = Res.string.please_enter_confirm_password) }
            hasError = true
        } else if (state.password != state.confirmPassword) {
            updateState { copy(confirmPasswordError = Res.string.passwords_do_not_match) }
            hasError = true
        }

        if (hasError) return

        // TODO: Call register use case
    }
}
