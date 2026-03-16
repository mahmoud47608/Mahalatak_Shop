package com.aait.data.util.multipart

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class DocumentHandler : MultipartHandler() {
    private val supportedExtensions = setOf(
        "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "rtf", "csv", "odt", "ods", "odp"
    )

    private val extensionToMimeType = mapOf(
        "pdf" to "application/pdf",
        "doc" to "application/msword",
        "docx" to "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "xls" to "application/vnd.ms-excel",
        "xlsx" to "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        "ppt" to "application/vnd.ms-powerpoint",
        "pptx" to "application/vnd.openxmlformats-officedocument.presentationml.presentation",
        "txt" to "text/plain",
        "rtf" to "application/rtf",
        "csv" to "text/csv",
        "odt" to "application/vnd.oasis.opendocument.text",
        "ods" to "application/vnd.oasis.opendocument.spreadsheet",
        "odp" to "application/vnd.oasis.opendocument.presentation"
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
