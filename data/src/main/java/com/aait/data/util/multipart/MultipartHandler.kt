package com.aait.data.util.multipart

import okhttp3.MultipartBody
import java.io.File

abstract class MultipartHandler(protected var nextHandler: MultipartHandler? = null) {
    fun setNext(handler: MultipartHandler): MultipartHandler {
        this.nextHandler = handler
        return handler
    }
    abstract fun canHandle(fileExtension: String): Boolean
    abstract fun createPart(partName: String, path: String): MultipartBody.Part

    fun handle(partName: String, path: String): MultipartBody.Part {
        val file = File(path)
        val extension = file.extension.lowercase()

        return if (canHandle(extension)) {
            createPart(partName, path)
        } else {
            nextHandler?.handle(partName, path)
                ?: throw IllegalArgumentException("Unsupported file type: $extension")
        }
    }
}
