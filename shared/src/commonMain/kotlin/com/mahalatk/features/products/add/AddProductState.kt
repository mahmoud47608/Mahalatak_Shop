package com.mahalatk.features.products.add

import androidx.compose.runtime.Immutable
import com.mahalatk.domain.entity.CategoryData
import com.mahalatk.domain.entity.SubCategoryData
import org.jetbrains.compose.resources.StringResource

enum class DiscountType {
    NONE,
    FIXED_PRICE,
    PERCENTAGE,
}

@Immutable
data class AddProductState(
    // Form fields
    val nameAr: String = "",
    val nameEn: String = "",
    val descriptionAr: String = "",
    val descriptionEn: String = "",
    val images: List<ByteArray> = emptyList(),
    val video: ByteArray? = null,
    val selectedCategories: List<CategoryData> = emptyList(),
    val selectedSubCategories: List<SubCategoryData> = emptyList(),
    val price: String = "",
    val discountType: DiscountType = DiscountType.NONE,
    val discountValue: String = "",

    // Data from repository
    val availableCategories: List<CategoryData> = emptyList(),
    val availableSubCategories: List<SubCategoryData> = emptyList(),

    // Error fields
    val nameArError: StringResource? = null,
    val nameEnError: StringResource? = null,
    val descriptionArError: StringResource? = null,
    val descriptionEnError: StringResource? = null,
    val imagesError: StringResource? = null,
    val categoryError: StringResource? = null,
    val subCategoryError: StringResource? = null,
    val priceError: StringResource? = null,
    val discountError: StringResource? = null,
)
