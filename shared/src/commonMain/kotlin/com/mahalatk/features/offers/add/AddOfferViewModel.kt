package com.mahalatk.features.offers.add

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.field_required

class AddOfferViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        AddOfferState(
            availableCategories = MockOfferData.categories,
            availableProducts = MockOfferData.products,
        )
    )
    val uiState: StateFlow<AddOfferState> = _uiState.asStateFlow()

    // ─── Step 0 - Offer Type ─────────────────────────────────────────────────

    fun selectOfferType(type: OfferType) {
        _uiState.update { it.copy(offerType = type, stepError = null) }
    }

    // ─── Step 1 - Offer Details ──────────────────────────────────────────────

    fun updateDiscountMode(mode: DiscountMode) {
        _uiState.update { it.copy(discountMode = mode) }
    }

    fun updateDiscountValue(value: String) {
        _uiState.update { it.copy(discountValue = value, stepError = null) }
    }

    fun updateMinCartValue(value: String) {
        _uiState.update { it.copy(minCartValue = value) }
    }

    fun updateBuyQuantity(value: String) {
        _uiState.update { it.copy(buyQuantity = value, stepError = null) }
    }

    fun updateGetQuantity(value: String) {
        _uiState.update { it.copy(getQuantity = value, stepError = null) }
    }

    fun updateFreeShippingMinCart(value: String) {
        _uiState.update { it.copy(freeShippingMinCart = value, stepError = null) }
    }

    fun updatePackageName(value: String) {
        _uiState.update { it.copy(packageName = value, stepError = null) }
    }

    fun updatePackagePrice(value: String) {
        _uiState.update { it.copy(packagePrice = value, stepError = null) }
    }

    // ─── Step 2 - Scope & Product Selection ──────────────────────────────────

    fun selectScopeType(type: OfferScopeType) {
        _uiState.update { it.copy(scopeType = type, stepError = null) }
    }

    fun toggleCategory(category: String) {
        _uiState.update { state ->
            state.copy(
                selectedCategories = state.selectedCategories.toggle(category),
                stepError = null,
            )
        }
    }

    fun toggleProduct(productId: String) {
        _uiState.update { state ->
            state.copy(
                selectedProductIds = state.selectedProductIds.toggle(productId),
                stepError = null,
            )
        }
    }

    fun togglePackageProduct(productId: String) {
        _uiState.update { state ->
            state.copy(
                packageProductIds = state.packageProductIds.toggle(productId),
                stepError = null,
            )
        }
    }

    fun toggleFilterCategory(category: String) {
        _uiState.update { state ->
            val updatedFilters = state.filterCategories.toggle(category)
            state.copy(
                filterCategories = updatedFilters,
                selectedProductIds = state.selectedProductIds.retainValidProducts(
                    updatedFilters, state.availableProducts,
                ),
                stepError = null,
            )
        }
    }

    fun togglePackageFilterCategory(category: String) {
        _uiState.update { state ->
            val updatedFilters = state.filterCategories.toggle(category)
            state.copy(
                filterCategories = updatedFilters,
                packageProductIds = state.packageProductIds.retainValidProducts(
                    updatedFilters, state.availableProducts,
                ),
                stepError = null,
            )
        }
    }

    // ─── Step 3 - Duration ───────────────────────────────────────────────────

    fun updateStartDate(value: String) {
        _uiState.update { it.copy(startDate = value, stepError = null) }
    }

    fun updateEndDate(value: String) {
        _uiState.update { it.copy(endDate = value, stepError = null) }
    }

    // ─── Navigation ──────────────────────────────────────────────────────────

    fun nextStep() {
        if (!validateCurrentStep()) return
        _uiState.update {
            it.copy(
                currentStep = (it.currentStep + 1).coerceAtMost(3),
                stepError = null,
            )
        }
    }

    fun previousStep() {
        _uiState.update {
            it.copy(
                currentStep = (it.currentStep - 1).coerceAtLeast(0),
                stepError = null,
            )
        }
    }

    fun publish() {
        if (!validateStep(3)) return
        _uiState.update { it.copy(showSuccess = true) }
    }

    // ─── Validation ──────────────────────────────────────────────────────────

    private fun validateCurrentStep(): Boolean = validateStep(_uiState.value.currentStep)

    private fun validateStep(step: Int): Boolean {
        val state = _uiState.value
        val isValid = when (step) {
            0 -> state.offerType != null
            1 -> validateOfferDetails(state)
            2 -> validateScope(state)
            3 -> state.startDate.isNotBlank() && state.endDate.isNotBlank()
            else -> true
        }
        if (!isValid) setStepError()
        return isValid
    }

    private fun validateOfferDetails(state: AddOfferState): Boolean = when (state.offerType) {
        OfferType.DISCOUNT -> state.discountValue.isNotBlank()
        OfferType.BUY_X_GET_Y -> state.buyQuantity.isNotBlank() && state.getQuantity.isNotBlank()
        OfferType.FREE_SHIPPING -> true
        OfferType.PACKAGE -> state.packageName.isNotBlank() && state.packagePrice.isNotBlank()
        null -> false
    }

    private fun validateScope(state: AddOfferState): Boolean {
        if (state.offerType == OfferType.PACKAGE) {
            return state.packageProductIds.isNotEmpty()
        }
        return when (state.scopeType) {
            OfferScopeType.ALL_PRODUCTS -> true
            OfferScopeType.CATEGORIES -> state.selectedCategories.isNotEmpty()
            OfferScopeType.SPECIFIC_PRODUCTS -> state.selectedProductIds.isNotEmpty()
        }
    }

    private fun setStepError() {
        _uiState.update { it.copy(stepError = Res.string.field_required) }
    }
}

// ─── Extension Helpers ───────────────────────────────────────────────────────

private fun Set<String>.toggle(item: String): Set<String> =
    if (item in this) this - item else this + item

private fun Set<String>.retainValidProducts(
    filterCategories: Set<String>,
    availableProducts: List<ProductItem>,
): Set<String> {
    if (filterCategories.isEmpty()) return this
    val validIds = availableProducts
        .filter { it.category in filterCategories }
        .map { it.id }
        .toSet()
    return intersect(validIds)
}

// ─── Mock Data (replace with repository/API later) ───────────────────────────

internal object MockOfferData {
    val categories = listOf(
        "رجالي",
        "حريمي",
        "أطفالي",
        "أحذية رجالي",
        "أحذية حريمي",
    )

    val products = listOf(
        ProductItem("1", "قميص رجالي كلاسيك", "رجالي"),
        ProductItem("2", "بنطلون جينز", "رجالي"),
        ProductItem("3", "تيشيرت قطن", "رجالي"),
        ProductItem("4", "حذاء رياضي", "أحذية رجالي"),
        ProductItem("5", "جاكيت شتوي", "حريمي"),
        ProductItem("6", "فستان سهرة", "حريمي"),
        ProductItem("7", "بلوزة أطفال", "أطفالي"),
        ProductItem("8", "حذاء حريمي", "أحذية حريمي"),
    )
}
