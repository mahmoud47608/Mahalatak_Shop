package com.aait.data.util.multipart

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class VideoHandler : MultipartHandler() {
    private val supportedExtensions = setOf(
        "mp4", "avi", "mov", "mkv", "webm", "3gp", "wmv", "flv"
    )

    private val extensionToMimeType = mapOf(
        "mp4" to "video/mp4",
        "avi" to "video/x-msvideo",
        "mov" to "video/quicktime",
        "mkv" to "video/x-matroska",
        "webm" to "video/webm",
        "3gp" to "video/3gpp",
        "wmv" to "video/x-ms-wmv",
        "flv" to "video/x-flv"
    )

    override fun canHandle(fileExtension: String): Boolean {
        return fileExtension.lowercase() in supportedExtensions
    }

    override fun createPart(partName: String, path: String): MultipartBody.Part {
        val file = File(path)
        val extension = file.extension.lowercase()
        val mimeType = extensionToMimeType[extension] ?: "video/*"
        
        val requestFile = file.asRequestBody(mimeType.toMediaTypeOrNull())
        return createFormData(partName, file.name, requestFile)
    }
}
