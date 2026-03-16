package com.aait.data.util.multipart

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class AudioHandler : MultipartHandler() {
    private val supportedExtensions = setOf(
        "mp3", "m4a", "wav", "aac", "ogg", "flac", "amr"
    )

    private val extensionToMimeType = mapOf(
        "mp3" to "audio/mpeg",
        "m4a" to "audio/mp4",
        "wav" to "audio/wav",
        "aac" to "audio/aac",
        "ogg" to "audio/ogg",
        "flac" to "audio/flac",
        "amr" to "audio/amr"
    )

    override fun canHandle(fileExtension: String): Boolean {
        return fileExtension.lowercase() in supportedExtensions
    }

    override fun createPart(partName: String, path: String): MultipartBody.Part {
        val file = File(path)
        val extension = file.extension.lowercase()
        val mimeType = extensionToMimeType[extension] ?: "audio/*"
        val requestFile = file.asRequestBody(mimeType.toMediaTypeOrNull())
        return createFormData(partName, file.name, requestFile)
    }
}
