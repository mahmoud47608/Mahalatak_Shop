package com.mahalatk.features.offers.add

import com.mahalatk.base.SimpleViewModel
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.field_required

class AddOfferViewModel : SimpleViewModel<AddOfferState, Nothing>(
    AddOfferState(
        availableCategories = MockOfferData.categories.toImmutableList(),
        availableProducts = MockOfferData.products.toImmutableList(),
    ),
) {

    // ─── Step 0 — Offer Type ────────────────────────────────────────────────

    fun selectOfferType(type: OfferType) = updateOfferState { copy(offerType = type) }

    // ─── Step 1 — Offer Details ─────────────────────────────────────────────

    fun updateDiscountMode(mode: DiscountMode) = updateOfferState { copy(discountMode = mode) }
    fun updateDiscountValue(value: String) = updateOfferState { copy(discountValue = value) }
    fun updateMinCartValue(value: String) = updateOfferState { copy(minCartValue = value) }
    fun updateBuyQuantity(value: String) = updateOfferState { copy(buyQuantity = value) }
    fun updateGetQuantity(value: String) = updateOfferState { copy(getQuantity = value) }
    fun updateFreeShippingMinCart(value: String) =
        updateOfferState { copy(freeShippingMinCart = value) }

    fun updatePackageName(value: String) = updateOfferState { copy(packageName = value) }
    fun updatePackagePrice(value: String) = updateOfferState { copy(packagePrice = value) }

    // ─── Step 2 — Scope & Products ──────────────────────────────────────────

    fun selectScopeType(type: OfferScopeType) = updateOfferState { copy(scopeType = type) }

    fun toggleCategory(category: String) = updateOfferState {
        copy(selectedCategories = selectedCategories.toggle(category))
    }

    fun toggleProduct(productId: String) = updateOfferState {
        copy(selectedProductIds = selectedProductIds.toggle(productId))
    }

    fun togglePackageProduct(productId: String) = updateOfferState {
        copy(packageProductIds = packageProductIds.toggle(productId))
    }

    fun toggleFilterCategory(category: String) = updateOfferState {
        val updated = filterCategories.toggle(category)
        copy(
            filterCategories = updated,
            selectedProductIds = selectedProductIds.retainByCategory(updated, availableProducts),
        )
    }

    fun togglePackageFilterCategory(category: String) = updateOfferState {
        val updated = filterCategories.toggle(category)
        copy(
            filterCategories = updated,
            packageProductIds = packageProductIds.retainByCategory(updated, availableProducts),
        )
    }

    // ─── Step 3 — Duration ──────────────────────────────────────────────────

    fun updateStartDate(value: String) = updateOfferState { copy(startDate = value) }
    fun updateEndDate(value: String) = updateOfferState { copy(endDate = value) }

    // ─── Navigation ─────────────────────────────────────────────────────────

    fun nextStep() {
        if (!validateCurrentStep()) return
        updateState {
            copy(
                currentStep = (currentStep + 1).coerceAtMost(3),
                stepError = null
            )
        }
    }

    fun previousStep() {
        updateState {
            copy(
                currentStep = (currentStep - 1).coerceAtLeast(0),
                stepError = null
            )
        }
    }

    fun publish() {
        if (!validateStep(3)) return
        updateState { copy(showSuccess = true) }
    }

    // ─── Internals ──────────────────────────────────────────────────────────

    /** Clears stepError on every user action, then applies the block. */
    private inline fun updateOfferState(crossinline block: AddOfferState.() -> AddOfferState) {
        updateState { block().copy(stepError = null) }
    }

    private fun validateCurrentStep(): Boolean = validateStep(uiState.value.currentStep)

    private fun validateStep(step: Int): Boolean {
        val s = uiState.value
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
        if (!valid) updateState { copy(stepError = Res.string.field_required) }
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
