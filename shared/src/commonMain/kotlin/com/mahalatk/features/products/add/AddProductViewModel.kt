package com.mahalatk.features.products.add

import com.mahalatk.base.BaseViewModel
import com.mahalatk.base.managers.LoadingManager
import com.mahalatk.base.managers.MessageManager
import com.mahalatk.domain.entity.CategoryData
import com.mahalatk.domain.entity.SubCategoryData
import com.mahalatk.domain.repository.CommonRepository
import com.mahalatk.domain.usecase.product.AddProductUseCase
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.please_add_image
import mahalatk.shared.generated.resources.please_enter_description
import mahalatk.shared.generated.resources.please_enter_discount_value
import mahalatk.shared.generated.resources.please_enter_price
import mahalatk.shared.generated.resources.please_enter_product_name_ar
import mahalatk.shared.generated.resources.please_enter_product_name_en
import mahalatk.shared.generated.resources.please_enter_quantity
import mahalatk.shared.generated.resources.please_select_category
import mahalatk.shared.generated.resources.please_select_color
import mahalatk.shared.generated.resources.please_select_season
import mahalatk.shared.generated.resources.please_select_size

private const val MAX_IMAGES = 6

class AddProductViewModel(
    private val commonRepository: CommonRepository,
    private val addProductUseCase: AddProductUseCase,
    loadingManager: LoadingManager,
    messageManager: MessageManager,
) : BaseViewModel<AddProductState, Nothing>(
    AddProductState(),
    loadingManager,
    messageManager,
) {

    // TODO: Re-enable API calls when backend is ready
    // init {
    //     loadCategories()
    // }

    // ── Part 1: Product Info ──

    fun selectSeason(season: String) {
        updateState { copy(selectedSeason = season, seasonError = null) }
    }

    fun selectCategory(category: CategoryData) {
        updateState {
            copy(
                selectedCategory = category,
                categoryError = null,
                availableSubCategories = DEFAULT_SUB_CATEGORIES.filter { it.categoryId == category.id }
                    .toImmutableList(),
                selectedSubCategory = null,
                subCategoryError = null,
                // Clear season when switching to shoes (id=4)
                selectedSeason = if (category.id == 4) "" else selectedSeason,
                seasonError = null,
            )
        }
    }

    fun selectSubCategory(subCategory: SubCategoryData) {
        updateState {
            copy(
                selectedSubCategory = subCategory,
                subCategoryError = null,
            )
        }
    }

    // ── Part 2: Piece Attributes ──

    fun selectColor(color: String) {
        updateState { copy(color = color, colorError = null) }
    }

    fun selectSize(size: String) {
        updateState { copy(size = size, sizeError = null) }
    }

    fun addImages(newImages: List<ByteArray>) {
        updateState {
            val combined = images + newImages
            copy(
                images = combined.take(MAX_IMAGES).toImmutableList(),
                imagesError = null,
            )
        }
    }

    fun removeImage(index: Int) {
        updateState {
            copy(images = images.toMutableList().apply { removeAt(index) }.toImmutableList())
        }
    }

    fun setVideo(bytes: ByteArray) {
        updateState { copy(video = bytes) }
    }

    fun removeVideo() {
        updateState { copy(video = null) }
    }

    fun setDiscountType(type: DiscountType) {
        updateState {
            copy(
                discountType = type,
                discountValue = if (type == DiscountType.NONE) "" else discountValue,
                discountError = null,
            )
        }
    }

    fun dismissSuccessSheet() {
        updateState { copy(showSuccessSheet = false) }
    }

    // ── Add Piece: validate Part 2, save piece, reset Part 2 ──

    fun addPiece() {
        val state = uiState.value
        var hasError = false

        // Validate Part 1 on first piece
        if (state.pieces.isEmpty()) {
            if (state.selectedCategory?.id != 4 && state.selectedSeason.isBlank()) {
                updateState { copy(seasonError = Res.string.please_select_season) }
                hasError = true
            }
            if (state.selectedCategory == null) {
                updateState { copy(categoryError = Res.string.please_select_category) }
                hasError = true
            }
            if (state.nameAr.isBlank()) {
                updateState { copy(nameArError = Res.string.please_enter_product_name_ar) }
                hasError = true
            }
            if (state.nameEn.isBlank()) {
                updateState { copy(nameEnError = Res.string.please_enter_product_name_en) }
                hasError = true
            }
            if (state.description.isBlank()) {
                updateState { copy(descriptionError = Res.string.please_enter_description) }
                hasError = true
            }
        }

        // Validate Part 2 fields
        if (state.color.isBlank()) {
            updateState { copy(colorError = Res.string.please_select_color) }
            hasError = true
        }
        if (state.size.isBlank()) {
            updateState { copy(sizeError = Res.string.please_select_size) }
            hasError = true
        }
        if (state.quantity.isBlank()) {
            updateState { copy(quantityError = Res.string.please_enter_quantity) }
            hasError = true
        }
        if (state.price.isBlank()) {
            updateState { copy(priceError = Res.string.please_enter_price) }
            hasError = true
        }
        if (state.images.isEmpty()) {
            updateState { copy(imagesError = Res.string.please_add_image) }
            hasError = true
        }
        if (state.discountType != DiscountType.NONE && state.discountValue.isBlank()) {
            updateState { copy(discountError = Res.string.please_enter_discount_value) }
            hasError = true
        }

        if (hasError) return

        val piece = ProductPiece(
            color = state.color,
            size = state.size,
            quantity = state.quantity,
            price = state.price,
            discountType = state.discountType,
            discountValue = state.discountValue,
            images = state.images,
            video = state.video,
        )

        // Save piece and reset Part 2
        updateState {
            copy(
                pieces = (pieces + piece).toImmutableList(),
                color = "",
                size = "",
                quantity = "",
                price = "",
                discountType = DiscountType.NONE,
                discountValue = "",
                images = persistentListOf(),
                video = null,
                colorError = null,
                sizeError = null,
                quantityError = null,
                priceError = null,
                discountError = null,
                imagesError = null,
            )
        }
    }

    // ── Finish: called when user presses "تم الانتهاء" ──

    fun onFinishClicked() {
        val state = uiState.value

        if (state.pieces.isEmpty()) {
            // No pieces yet — treat as first-time add (validate Part 1 + Part 2)
            addPieceAndFinish()
            return
        }

        // Has pieces already — check if Part 2 has pending unsaved data
        if (state.hasPendingPieceData) {
            updateState { copy(showWarningDialog = true) }
        } else {
            // Part 2 is clean — finish directly
            updateState { copy(showSuccessSheet = true) }
        }
    }

    fun dismissWarningDialog() {
        updateState { copy(showWarningDialog = false) }
    }

    fun confirmFinishAnyway() {
        updateState { copy(showWarningDialog = false, showSuccessSheet = true) }
    }

    /**
     * Used when no pieces exist yet — validates Part 1 + Part 2,
     * adds the piece, then shows success.
     */
    private fun addPieceAndFinish() {
        val state = uiState.value
        var hasError = false

        // Validate Part 1
        if (state.selectedCategory?.id != 4 && state.selectedSeason.isBlank()) {
            updateState { copy(seasonError = Res.string.please_select_season) }
            hasError = true
        }
        if (state.selectedCategory == null) {
            updateState { copy(categoryError = Res.string.please_select_category) }
            hasError = true
        }
        if (state.nameAr.isBlank()) {
            updateState { copy(nameArError = Res.string.please_enter_product_name_ar) }
            hasError = true
        }
        if (state.nameEn.isBlank()) {
            updateState { copy(nameEnError = Res.string.please_enter_product_name_en) }
            hasError = true
        }
        if (state.description.isBlank()) {
            updateState { copy(descriptionError = Res.string.please_enter_description) }
            hasError = true
        }

        // Validate Part 2
        if (state.color.isBlank()) {
            updateState { copy(colorError = Res.string.please_select_color) }
            hasError = true
        }
        if (state.size.isBlank()) {
            updateState { copy(sizeError = Res.string.please_select_size) }
            hasError = true
        }
        if (state.quantity.isBlank()) {
            updateState { copy(quantityError = Res.string.please_enter_quantity) }
            hasError = true
        }
        if (state.price.isBlank()) {
            updateState { copy(priceError = Res.string.please_enter_price) }
            hasError = true
        }
        if (state.images.isEmpty()) {
            updateState { copy(imagesError = Res.string.please_add_image) }
            hasError = true
        }
        if (state.discountType != DiscountType.NONE && state.discountValue.isBlank()) {
            updateState { copy(discountError = Res.string.please_enter_discount_value) }
            hasError = true
        }

        if (hasError) return

        // Save piece then show success (no API call temporarily)
        val piece = ProductPiece(
            color = state.color,
            size = state.size,
            quantity = state.quantity,
            price = state.price,
            discountType = state.discountType,
            discountValue = state.discountValue,
            images = state.images,
            video = state.video,
        )

        updateState {
            copy(
                pieces = (pieces + piece).toImmutableList(),
                showSuccessSheet = true,
            )
        }
    }
}
