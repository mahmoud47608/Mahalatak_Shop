package com.mahalatk.features.products.add

import androidx.lifecycle.viewModelScope
import com.mahalatk.base.BaseViewModel
import com.mahalatk.base.managers.LoadingManager
import com.mahalatk.base.managers.MessageManager
import com.mahalatk.domain.entity.CategoryData
import com.mahalatk.domain.entity.SubCategoryData
import com.mahalatk.domain.exceptions.ValidationException
import com.mahalatk.domain.repository.CommonRepository
import com.mahalatk.domain.usecase.product.AddProductUseCase
import com.mahalatk.domain.util.DataState
import com.mahalatk.util.applyCommonSideEffects
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.please_add_image
import mahalatk.shared.generated.resources.please_enter_description_ar
import mahalatk.shared.generated.resources.please_enter_description_en
import mahalatk.shared.generated.resources.please_enter_discount_value
import mahalatk.shared.generated.resources.please_enter_price
import mahalatk.shared.generated.resources.please_enter_product_name_ar
import mahalatk.shared.generated.resources.please_enter_product_name_en
import mahalatk.shared.generated.resources.please_select_category
import mahalatk.shared.generated.resources.please_select_discount_type

private const val MAX_IMAGES = 6

class AddProductViewModel(
    private val commonRepository: CommonRepository,
    private val addProductUseCase: AddProductUseCase,
    loadingManager: LoadingManager,
    messageManager: MessageManager,
) : BaseViewModel<AddProductState>(
    AddProductState(),
    loadingManager,
    messageManager,
) {

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            commonRepository.getCategories().collect { state ->
                state.applyCommonSideEffects(this@AddProductViewModel) { response ->
                    response.data?.let { categories ->
                        updateState { copy(availableCategories = categories) }
                    }
                }
            }
        }
    }

    private fun loadSubCategories(categoryIds: List<Int>) {
        viewModelScope.launch {
            val allSubCategories = mutableListOf<SubCategoryData>()
            categoryIds.forEach { categoryId ->
                commonRepository.getSubCategories(categoryId).collect { state ->
                    state.applyCommonSideEffects(
                        this@AddProductViewModel,
                        showLoading = false,
                    ) { response ->
                        response.data?.let { subCategories ->
                            allSubCategories.addAll(subCategories)
                        }
                    }
                }
            }
            updateState {
                copy(
                    availableSubCategories = allSubCategories,
                    selectedSubCategories = selectedSubCategories.filter { sub ->
                        categoryIds.contains(sub.categoryId)
                    },
                )
            }
        }
    }

    fun addImages(newImages: List<ByteArray>) {
        updateState {
            val combined = images + newImages
            copy(
                images = combined.take(MAX_IMAGES),
                imagesError = null,
            )
        }
    }

    fun removeImage(index: Int) {
        updateState {
            copy(images = images.toMutableList().apply { removeAt(index) })
        }
    }

    fun setVideo(bytes: ByteArray) {
        updateState { copy(video = bytes) }
    }

    fun removeVideo() {
        updateState { copy(video = null) }
    }

    fun toggleCategory(category: CategoryData) {
        updateState {
            val updated = if (selectedCategories.any { it.id == category.id }) {
                selectedCategories.filter { it.id != category.id }
            } else {
                selectedCategories + category
            }
            copy(selectedCategories = updated, categoryError = null)
        }
        val categoryIds = uiState.value.selectedCategories.map { it.id }
        if (categoryIds.isNotEmpty()) {
            loadSubCategories(categoryIds)
        } else {
            updateState {
                copy(
                    availableSubCategories = emptyList(),
                    selectedSubCategories = emptyList(),
                )
            }
        }
    }

    fun toggleSubCategory(subCategory: SubCategoryData) {
        updateState {
            val updated = if (selectedSubCategories.any { it.id == subCategory.id }) {
                selectedSubCategories.filter { it.id != subCategory.id }
            } else {
                selectedSubCategories + subCategory
            }
            copy(selectedSubCategories = updated, subCategoryError = null)
        }
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

    fun addProduct() {
        // Clear previous errors
        updateState {
            copy(
                nameArError = null,
                nameEnError = null,
                descriptionArError = null,
                descriptionEnError = null,
                imagesError = null,
                categoryError = null,
                priceError = null,
                discountError = null,
            )
        }

        val state = uiState.value

        viewModelScope.launch {
            val hasDiscount = state.discountType != DiscountType.NONE
            addProductUseCase(
                nameAr = state.nameAr,
                nameEn = state.nameEn,
                descriptionAr = state.descriptionAr,
                descriptionEn = state.descriptionEn,
                images = state.images,
                video = state.video,
                categoryIds = state.selectedCategories.map { it.id },
                subCategoryIds = state.selectedSubCategories.map { it.id },
                price = state.price,
                hasDiscount = hasDiscount,
                discountType = if (hasDiscount) state.discountType.name else null,
                discountValue = state.discountValue,
            ).collectLatest {
                when (it) {
                    is DataState.Error -> {
                        when (val error = it.throwable) {
                            is ValidationException.MultipleValidationException -> {
                                error.errors.forEach { validationError ->
                                    when (validationError) {
                                        is ValidationException.EmptyProductNameArException ->
                                            updateState { copy(nameArError = Res.string.please_enter_product_name_ar) }

                                        is ValidationException.EmptyProductNameEnException ->
                                            updateState { copy(nameEnError = Res.string.please_enter_product_name_en) }

                                        is ValidationException.EmptyDescriptionArException ->
                                            updateState { copy(descriptionArError = Res.string.please_enter_description_ar) }

                                        is ValidationException.EmptyDescriptionEnException ->
                                            updateState { copy(descriptionEnError = Res.string.please_enter_description_en) }

                                        is ValidationException.EmptyImagesException ->
                                            updateState { copy(imagesError = Res.string.please_add_image) }

                                        is ValidationException.EmptyCategoryException ->
                                            updateState { copy(categoryError = Res.string.please_select_category) }

                                        is ValidationException.EmptyPriceException,
                                        is ValidationException.InvalidPriceException ->
                                            updateState { copy(priceError = Res.string.please_enter_price) }

                                        is ValidationException.EmptyDiscountTypeException ->
                                            updateState { copy(discountError = Res.string.please_select_discount_type) }

                                        is ValidationException.EmptyDiscountValueException,
                                        is ValidationException.InvalidDiscountValueException ->
                                            updateState { copy(discountError = Res.string.please_enter_discount_value) }

                                        else -> {}
                                    }
                                }
                            }

                            else -> it.applyCommonSideEffects<Any>(
                                this@AddProductViewModel,
                                showSuccessToast = true,
                            )
                        }
                    }

                    else -> {
                        it.applyCommonSideEffects(
                            this@AddProductViewModel,
                            showLoading = true,
                            showSuccessToast = true,
                        ) { /* Product added successfully */ }
                    }
                }
            }
        }
    }
}
