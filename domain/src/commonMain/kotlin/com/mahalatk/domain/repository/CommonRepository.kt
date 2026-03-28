package com.mahalatk.domain.repository

import com.mahalatk.domain.entity.CategoryData
import com.mahalatk.domain.entity.SubCategoryData
import com.mahalatk.domain.entity.base.BaseResponse
import com.mahalatk.domain.util.DataState
import kotlinx.coroutines.flow.Flow

interface CommonRepository {

    suspend fun getCategories(): Flow<DataState<BaseResponse<List<CategoryData>>>>

    suspend fun getSubCategories(categoryId: Int): Flow<DataState<BaseResponse<List<SubCategoryData>>>>

    suspend fun addProduct(
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
    ): Flow<DataState<BaseResponse<Any>>>
}
