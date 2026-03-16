package com.aait.data.util.multipart

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ImageHandler : MultipartHandler() {
    private val supportedExtensions = setOf(
        "png", "jpg", "jpeg", "gif", "bmp", "webp", "svg", "tiff", "ico"
    )

    private val extensionToMimeType = mapOf(
        "jpg" to "image/jpeg",
        "jpeg" to "image/jpeg",
        "png" to "image/png",
        "gif" to "image/gif",
        "bmp" to "image/bmp",
        "webp" to "image/webp",
        "svg" to "image/svg+xml",
        "tiff" to "image/tiff",
        "ico" to "image/x-icon"
    )

    override fun canHandle(fileExtension: String): Boolean {
        return fileExtension.lowercase() in supportedExtensions
    }

    override fun createPart(partName: String, path: String): MultipartBody.Part {
        val file = File(path)
        val extension = file.extension.lowercase()
        val mimeType = extensionToMimeType[extension] ?: "image/webp"
        val requestFile = file.asRequestBody(mimeType.toMediaTypeOrNull())
        return createFormData(partName, file.name, requestFile)
    }
}
