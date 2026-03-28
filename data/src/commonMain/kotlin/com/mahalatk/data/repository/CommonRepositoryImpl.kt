package com.mahalatk.data.repository

import com.mahalatk.data.remote.CommonEndPoint
import com.mahalatk.data.util.safeApiCall
import com.mahalatk.domain.repository.CommonRepository

class CommonRepositoryImpl(private val endPoint: CommonEndPoint) : CommonRepository {

    override suspend fun getCategories() = safeApiCall {
        endPoint.getCategories()
    }

    override suspend fun getSubCategories(categoryId: Int) = safeApiCall {
        endPoint.getSubCategories(categoryId)
    }

    override suspend fun addProduct(
        nameAr: String,
        nameEn: String,
        descriptionAr: String,
        descriptionEn: String,
        images: List<ByteArray>,
        video: ByteArray?,
        categoryIds: List<Int>,
        subCategoryIds: List<Int>,
        price: Double,
        discountType: String?,
        discountValue: Double?,
    ) = safeApiCall {
        endPoint.addProduct(
            nameAr = nameAr,
            nameEn = nameEn,
            descriptionAr = descriptionAr,
            descriptionEn = descriptionEn,
            images = images,
            video = video,
            categoryIds = categoryIds,
            subCategoryIds = subCategoryIds,
            price = price,
            discountType = discountType,
            discountValue = discountValue,
        )
    }
}
