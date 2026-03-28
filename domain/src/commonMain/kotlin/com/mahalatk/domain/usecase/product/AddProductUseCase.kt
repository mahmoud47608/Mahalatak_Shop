package com.mahalatk.domain.usecase.product

import com.mahalatk.domain.exceptions.ValidationException
import com.mahalatk.domain.repository.CommonRepository
import com.mahalatk.domain.util.DataState
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class AddProductUseCase(private val commonRepository: CommonRepository) {

    operator fun invoke(
        nameAr: String,
        nameEn: String,
        descriptionAr: String,
        descriptionEn: String,
        images: List<ByteArray>,
        video: ByteArray?,
        categoryIds: List<Int>,
        subCategoryIds: List<Int>,
        price: String,
        hasDiscount: Boolean,
        discountType: String?,
        discountValue: String?,
    ) = flow {
        val errors = mutableListOf<ValidationException>()

        // Name validation
        if (nameAr.isBlank()) errors.add(ValidationException.EmptyProductNameArException)
        if (nameEn.isBlank()) errors.add(ValidationException.EmptyProductNameEnException)

        // Description validation
        if (descriptionAr.isBlank()) errors.add(ValidationException.EmptyDescriptionArException)
        if (descriptionEn.isBlank()) errors.add(ValidationException.EmptyDescriptionEnException)

        // Images validation
        if (images.isEmpty()) errors.add(ValidationException.EmptyImagesException)

        // Category validation
        if (categoryIds.isEmpty()) errors.add(ValidationException.EmptyCategoryException)

        // Price validation
        val parsedPrice = price.toDoubleOrNull()
        when {
            price.isBlank() -> errors.add(ValidationException.EmptyPriceException)
            parsedPrice == null || parsedPrice <= 0 -> errors.add(ValidationException.InvalidPriceException)
        }

        // Discount validation
        var parsedDiscountValue: Double? = null
        if (hasDiscount) {
            if (discountType.isNullOrBlank()) {
                errors.add(ValidationException.EmptyDiscountTypeException)
            }
            when {
                discountValue.isNullOrBlank() -> errors.add(ValidationException.EmptyDiscountValueException)
                else -> {
                    parsedDiscountValue = discountValue.toDoubleOrNull()
                    if (parsedDiscountValue == null || parsedDiscountValue <= 0) {
                        errors.add(ValidationException.InvalidDiscountValueException)
                    }
                }
            }
        }

        if (errors.isNotEmpty()) {
            emit(DataState.Error(ValidationException.MultipleValidationException(errors)))
        } else {
            emitAll(
                commonRepository.addProduct(
                    nameAr = nameAr,
                    nameEn = nameEn,
                    descriptionAr = descriptionAr,
                    descriptionEn = descriptionEn,
                    images = images,
                    video = video,
                    categoryIds = categoryIds,
                    subCategoryIds = subCategoryIds,
                    price = parsedPrice!!,
                    discountType = discountType,
                    discountValue = parsedDiscountValue,
                )
            )
        }
    }
}
