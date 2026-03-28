package com.mahalatk.data.remote

import com.mahalatk.domain.entity.CategoryData
import com.mahalatk.domain.entity.SubCategoryData
import com.mahalatk.domain.entity.base.BaseResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

class CommonEndPoint(private val client: HttpClient) {

    suspend fun getCategories(): BaseResponse<List<CategoryData>> =
        client.get("categories").body()

    suspend fun getSubCategories(categoryId: Int): BaseResponse<List<SubCategoryData>> =
        client.get("categories/$categoryId/sub-categories").body()

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
    ): BaseResponse<Any> = client.submitFormWithBinaryData(
        url = "products",
        formData = formData {
            append("name_ar", nameAr)
            append("name_en", nameEn)
            append("description_ar", descriptionAr)
            append("description_en", descriptionEn)
            append("price", price.toString())

            images.forEachIndexed { index, bytes ->
                append("images[$index]", bytes, Headers.build {
                    append(HttpHeaders.ContentType, "image/jpeg")
                    append(HttpHeaders.ContentDisposition, "filename=\"image_$index.jpg\"")
                })
            }

            video?.let {
                append("video", it, Headers.build {
                    append(HttpHeaders.ContentType, "video/mp4")
                    append(HttpHeaders.ContentDisposition, "filename=\"video.mp4\"")
                })
            }

            categoryIds.forEachIndexed { index, id ->
                append("category_ids[$index]", id.toString())
            }

            subCategoryIds.forEachIndexed { index, id ->
                append("sub_category_ids[$index]", id.toString())
            }

            discountType?.let { append("discount_type", it) }
            discountValue?.let { append("discount_value", it.toString()) }
        }
    ).body()
}
