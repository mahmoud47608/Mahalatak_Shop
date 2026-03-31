package com.mahalatk.features.offers.add

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.field_required

class AddOfferViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AddOfferState())
    val uiState: StateFlow<AddOfferState> = _uiState.asStateFlow()

    fun selectOfferType(type: OfferType) {
        _uiState.update { it.copy(offerType = type, stepError = null) }
    }

    fun updateDiscountMode(mode: DiscountMode) {
        _uiState.update { it.copy(discountMode = mode) }
    }

    fun updateDiscountValue(v: String) {
        _uiState.update { it.copy(discountValue = v, stepError = null) }
    }

    fun updateMinCartValue(v: String) {
        _uiState.update { it.copy(minCartValue = v) }
    }

    fun updateBuyQuantity(v: String) {
        _uiState.update { it.copy(buyQuantity = v, stepError = null) }
    }

    fun updateGetQuantity(v: String) {
        _uiState.update { it.copy(getQuantity = v, stepError = null) }
    }

    fun updateBundlePrice(v: String) {
        _uiState.update { it.copy(bundlePrice = v, stepError = null) }
    }

    fun updatePackageName(v: String) {
        _uiState.update { it.copy(packageName = v, stepError = null) }
    }

    fun updatePackagePrice(v: String) {
        _uiState.update { it.copy(packagePrice = v, stepError = null) }
    }

    fun togglePackageProduct(id: String) {
        _uiState.update { state ->
            val newSet = state.packageProductIds.toMutableSet()
            if (id in newSet) newSet.remove(id) else newSet.add(id)
            state.copy(packageProductIds = newSet, stepError = null)
        }
    }

    fun updateStartDate(v: String) {
        _uiState.update { it.copy(startDate = v, stepError = null) }
    }

    fun updateEndDate(v: String) {
        _uiState.update { it.copy(endDate = v, stepError = null) }
    }

    fun selectScopeType(type: OfferScopeType) {
        _uiState.update { it.copy(scopeType = type, stepError = null) }
    }

    fun toggleCategory(cat: String) {
        _uiState.update { state ->
            val newSet = state.selectedCategories.toMutableSet()
            if (cat in newSet) newSet.remove(cat) else newSet.add(cat)
            state.copy(selectedCategories = newSet, stepError = null)
        }
    }

    fun toggleProduct(id: String) {
        _uiState.update { state ->
            val newSet = state.selectedProductIds.toMutableSet()
            if (id in newSet) newSet.remove(id) else newSet.add(id)
            state.copy(selectedProductIds = newSet, stepError = null)
        }
    }

    fun nextStep() {
        val state = _uiState.value
        if (!validateStep(state.currentStep)) return
        _uiState.update {
            it.copy(
                currentStep = (it.currentStep + 1).coerceAtMost(3),
                stepError = null
            )
        }
    }

    fun previousStep() {
        _uiState.update {
            it.copy(
                currentStep = (it.currentStep - 1).coerceAtLeast(0),
                stepError = null
            )
        }
    }

    fun publish() {
        if (!validateStep(3)) return
        _uiState.update { it.copy(showSuccess = true) }
    }

    private fun validateStep(step: Int): Boolean {
        val state = _uiState.value
        return when (step) {
            0 -> {
                if (state.offerType == null) {
                    _uiState.update { it.copy(stepError = Res.string.field_required) }
                    false
                } else true
            }

            1 -> when (state.offerType) {
                OfferType.DISCOUNT -> state.discountValue.isNotBlank()
                    .also { if (!it) _uiState.update { s -> s.copy(stepError = Res.string.field_required) } }

                OfferType.BUY_X_GET_Y -> (state.buyQuantity.isNotBlank() && state.getQuantity.isNotBlank()).also {
                    if (!it) _uiState.update { s ->
                        s.copy(
                            stepError = Res.string.field_required
                        )
                    }
                }

                OfferType.BUNDLE -> state.bundlePrice.isNotBlank()
                    .also { if (!it) _uiState.update { s -> s.copy(stepError = Res.string.field_required) } }

                OfferType.PACKAGE -> (state.packageName.isNotBlank() && state.packagePrice.isNotBlank()).also {
                    if (!it) _uiState.update { s ->
                        s.copy(
                            stepError = Res.string.field_required
                        )
                    }
                }

                null -> false
            }

            2 -> if (state.offerType == OfferType.PACKAGE) {
                // Package: Step 3 is product selection
                state.packageProductIds.isNotEmpty()
                    .also { if (!it) _uiState.update { s -> s.copy(stepError = Res.string.field_required) } }
            } else {
                when (state.scopeType) {
                    OfferScopeType.ALL_PRODUCTS -> true
                    OfferScopeType.CATEGORIES -> state.selectedCategories.isNotEmpty()
                        .also { if (!it) _uiState.update { s -> s.copy(stepError = Res.string.field_required) } }

                    OfferScopeType.SPECIFIC_PRODUCTS -> state.selectedProductIds.isNotEmpty()
                        .also { if (!it) _uiState.update { s -> s.copy(stepError = Res.string.field_required) } }
                }
            }

            3 -> (state.startDate.isNotBlank() && state.endDate.isNotBlank()).also {
                if (!it) _uiState.update { s ->
                    s.copy(
                        stepError = Res.string.field_required
                    )
                }
            }

            else -> true
        }
    }
}
