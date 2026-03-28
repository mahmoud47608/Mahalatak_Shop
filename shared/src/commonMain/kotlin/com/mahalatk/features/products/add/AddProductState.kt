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

/**
 * Represents a single piece/variant added to the product.
 */
@Immutable
data class ProductPiece(
    val color: String = "",
    val size: String = "",
    val quantity: String = "",
    val price: String = "",
    val discountType: DiscountType = DiscountType.NONE,
    val discountValue: String = "",
    val images: List<ByteArray> = emptyList(),
    val video: ByteArray? = null,
)

@Immutable
data class AddProductState(
    // ── Part 1: Product Info (persists across "Add Piece") ──
    val selectedSeason: String = "",
    val selectedCategory: CategoryData? = null,
    val selectedSubCategory: SubCategoryData? = null,
    val nameAr: String = "",
    val nameEn: String = "",
    val description: String = "",

    // ── Part 2: Piece Attributes (resets on "Add Piece") ──
    val color: String = "",
    val size: String = "",
    val quantity: String = "",
    val price: String = "",
    val discountType: DiscountType = DiscountType.NONE,
    val discountValue: String = "",
    val images: List<ByteArray> = emptyList(),
    val video: ByteArray? = null,

    // ── Accumulated pieces ──
    val pieces: List<ProductPiece> = emptyList(),

    // ── Data from repository ──
    val availableCategories: List<CategoryData> = DEFAULT_CATEGORIES,
    val availableSubCategories: List<SubCategoryData> = emptyList(),
    val availableSeasons: List<String> = listOf("صيفي", "شتوي"),
    val availableColors: List<String> = listOf(
        "أحمر", "أزرق", "أسود", "أبيض", "أخضر", "أصفر", "بني", "رمادي", "برتقالي", "وردي",
    ),
    val availableSizes: List<String> = listOf(
        "XS", "S", "M", "L", "XL", "XXL", "XXXL",
    ),

    // ── UI flags ──
    val showSuccessSheet: Boolean = false,
    val showWarningDialog: Boolean = false,

    // ── Error fields ──
    val seasonError: StringResource? = null,
    val categoryError: StringResource? = null,
    val subCategoryError: StringResource? = null,
    val nameArError: StringResource? = null,
    val nameEnError: StringResource? = null,
    val descriptionError: StringResource? = null,
    val colorError: StringResource? = null,
    val sizeError: StringResource? = null,
    val quantityError: StringResource? = null,
    val priceError: StringResource? = null,
    val discountError: StringResource? = null,
    val imagesError: StringResource? = null,
) {
    /** True after at least one piece has been added — Part 1 becomes locked. */
    val isPart1Locked: Boolean get() = pieces.isNotEmpty()

    /** True if the user started filling Part 2 but hasn't clicked "Add Piece" yet. */
    val hasPendingPieceData: Boolean
        get() = color.isNotBlank() || size.isNotBlank() || quantity.isNotBlank() ||
                price.isNotBlank() || images.isNotEmpty() || video != null ||
                (discountType != DiscountType.NONE)
}

// ── Default dummy data ──

private val DEFAULT_CATEGORIES = listOf(
    CategoryData(id = 1, name = "رجالي"),
    CategoryData(id = 2, name = "حريمي"),
    CategoryData(id = 3, name = "أطفالي"),
    CategoryData(id = 4, name = "أحذية"),
)

val DEFAULT_SUB_CATEGORIES = listOf(
    // رجالي
    SubCategoryData(id = 1, name = "بنطلون", categoryId = 1),
    SubCategoryData(id = 2, name = "تي شيرت", categoryId = 1),
    SubCategoryData(id = 3, name = "قميص", categoryId = 1),
    // حريمي
    SubCategoryData(id = 4, name = "بلوزة", categoryId = 2),
    SubCategoryData(id = 5, name = "فستان", categoryId = 2),
    SubCategoryData(id = 6, name = "تي شيرت", categoryId = 2),
    // أطفالي
    SubCategoryData(id = 7, name = "بنطلون", categoryId = 3),
    SubCategoryData(id = 8, name = "تي شيرت", categoryId = 3),
    SubCategoryData(id = 9, name = "بيجامة", categoryId = 3),
    // أحذية
    SubCategoryData(id = 10, name = "رياضي", categoryId = 4),
    SubCategoryData(id = 11, name = "كلاسيك", categoryId = 4),
    SubCategoryData(id = 12, name = "شبشب", categoryId = 4),
)
