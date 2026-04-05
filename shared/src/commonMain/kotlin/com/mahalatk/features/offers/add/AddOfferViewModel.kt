package com.mahalatk.features.offers.add

import androidx.lifecycle.ViewModel
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.field_required

class AddOfferViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        AddOfferState(
            availableCategories = MockOfferData.categories.toImmutableList(),
            availableProducts = MockOfferData.products.toImmutableList(),
        ),
    )
    val uiState: StateFlow<AddOfferState> = _uiState.asStateFlow()

    // ─── Step 0 — Offer Type ────────────────────────────────────────────────

    fun selectOfferType(type: OfferType) = updateState { copy(offerType = type) }

    // ─── Step 1 — Offer Details ─────────────────────────────────────────────

    fun updateDiscountMode(mode: DiscountMode) = updateState { copy(discountMode = mode) }
    fun updateDiscountValue(value: String) = updateState { copy(discountValue = value) }
    fun updateMinCartValue(value: String) = updateState { copy(minCartValue = value) }
    fun updateBuyQuantity(value: String) = updateState { copy(buyQuantity = value) }
    fun updateGetQuantity(value: String) = updateState { copy(getQuantity = value) }
    fun updateFreeShippingMinCart(value: String) = updateState { copy(freeShippingMinCart = value) }
    fun updatePackageName(value: String) = updateState { copy(packageName = value) }
    fun updatePackagePrice(value: String) = updateState { copy(packagePrice = value) }

    // ─── Step 2 — Scope & Products ──────────────────────────────────────────

    fun selectScopeType(type: OfferScopeType) = updateState { copy(scopeType = type) }

    fun toggleCategory(category: String) = updateState {
        copy(selectedCategories = selectedCategories.toggle(category))
    }

    fun toggleProduct(productId: String) = updateState {
        copy(selectedProductIds = selectedProductIds.toggle(productId))
    }

    fun togglePackageProduct(productId: String) = updateState {
        copy(packageProductIds = packageProductIds.toggle(productId))
    }

    fun toggleFilterCategory(category: String) = updateState {
        val updated = filterCategories.toggle(category)
        copy(
            filterCategories = updated,
            selectedProductIds = selectedProductIds.retainByCategory(updated, availableProducts),
        )
    }

    fun togglePackageFilterCategory(category: String) = updateState {
        val updated = filterCategories.toggle(category)
        copy(
            filterCategories = updated,
            packageProductIds = packageProductIds.retainByCategory(updated, availableProducts),
        )
    }

    // ─── Step 3 — Duration ──────────────────────────────────────────────────

    fun updateStartDate(value: String) = updateState { copy(startDate = value) }
    fun updateEndDate(value: String) = updateState { copy(endDate = value) }

    // ─── Navigation ─────────────────────────────────────────────────────────

    fun nextStep() {
        if (!validateCurrentStep()) return
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

    // ─── Internals ──────────────────────────────────────────────────────────

    /** Central state updater — clears error on every user action. */
    private inline fun updateState(crossinline block: AddOfferState.() -> AddOfferState) {
        _uiState.update { it.block().copy(stepError = null) }
    }

    private fun validateCurrentStep(): Boolean = validateStep(_uiState.value.currentStep)

    private fun validateStep(step: Int): Boolean {
        val s = _uiState.value
        val valid = when (step) {
            0 -> s.offerType != null
            1 -> when (s.offerType) {
                OfferType.DISCOUNT -> s.discountValue.isNotBlank()
                OfferType.BUY_X_GET_Y -> s.buyQuantity.isNotBlank() && s.getQuantity.isNotBlank()
                OfferType.FREE_SHIPPING -> true
                OfferType.PACKAGE -> s.packageName.isNotBlank() && s.packagePrice.isNotBlank()
                null -> false
            }

            2 -> if (s.offerType == OfferType.PACKAGE) {
                s.packageProductIds.isNotEmpty()
            } else when (s.scopeType) {
                OfferScopeType.ALL_PRODUCTS -> true
                OfferScopeType.CATEGORIES -> s.selectedCategories.isNotEmpty()
                OfferScopeType.SPECIFIC_PRODUCTS -> s.selectedProductIds.isNotEmpty()
            }

            3 -> s.startDate.isNotBlank() && s.endDate.isNotBlank()
            else -> true
        }
        if (!valid) _uiState.update { it.copy(stepError = Res.string.field_required) }
        return valid
    }
}

// ─── Set Helpers ────────────────────────────────────────────────────────────

private fun Set<String>.toggle(item: String): ImmutableSet<String> =
    (if (item in this) this - item else this + item).toImmutableSet()

private fun Set<String>.retainByCategory(
    categories: Set<String>,
    products: List<ProductItem>,
): ImmutableSet<String> {
    if (categories.isEmpty()) return this.toImmutableSet()
    val validIds = products.filter { it.category in categories }.map { it.id }.toSet()
    return intersect(validIds).toImmutableSet()
}

// ─── Mock Data ──────────────────────────────────────────────────────────────

internal object MockOfferData {
    val categories = listOf("رجالي", "حريمي", "أطفالي", "أحذية رجالي", "أحذية حريمي")

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
