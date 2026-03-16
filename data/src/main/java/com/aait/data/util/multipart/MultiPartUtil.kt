package com.aait.data.util.multipart

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object MultiPartUtil {
    private val chain: MultipartHandler

    init {
        val docHandler = DocumentHandler()
        val imageHandler = ImageHandler()
        val audioHandler = AudioHandler()
        val videoHandler = VideoHandler()
        val compressedHandler = CompressedHandler()

        imageHandler
            .setNext(videoHandler)
            .setNext(docHandler)
            .setNext(audioHandler)
            .setNext(compressedHandler)
        chain = imageHandler
    }

    fun String.toMultiPart(partName: String ): MultipartBody.Part {
        return chain.handle(partName, this)
    }

    fun List<String>.toMultiPart(partName: String): List<MultipartBody.Part> {
        return this.map { path -> path.toMultiPart(partName = partName) }
    }

    fun String.stringToPart(): RequestBody {
        return this.toRequestBody("text/plain".toMediaTypeOrNull())
    }

}
