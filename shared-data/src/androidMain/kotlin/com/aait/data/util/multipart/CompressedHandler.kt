package com.aait.data.util.multipart

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class CompressedHandler : MultipartHandler() {
    private val supportedExtensions = setOf(
        "zip", "rar", "7z", "tar", "gz", "bz2"
    )

    private val extensionToMimeType = mapOf(
        "zip" to "application/zip",
        "rar" to "application/x-rar-compressed",
        "7z" to "application/x-7z-compressed",
        "tar" to "application/x-tar",
        "gz" to "application/gzip",
        "bz2" to "application/x-bzip2"
    )

    override fun canHandle(fileExtension: String): Boolean {
        return fileExtension.lowercase() in supportedExtensions
    }

    override fun createPart(partName: String, path: String): MultipartBody.Part {
        val file = File(path)
        val extension = file.extension.lowercase()
        val mimeType = extensionToMimeType[extension] ?: "application/octet-stream"
        val requestFile = file.asRequestBody(mimeType.toMediaTypeOrNull())
        return createFormData(partName, file.name, requestFile)
    }
}
