package com.aait.domain.entity.general

import kotlinx.serialization.Serializable

@Serializable
data class MediaItemModel(
    val url: String,
    val type: MediaType
) {
    val isVideo: Boolean
        get() = type == MediaType.VIDEO

    val isImage: Boolean
        get() = type == MediaType.IMAGE

    companion object {
        fun fromUrl(url: String): MediaItemModel {
            val type = when {
                url.isVideoUrl() -> MediaType.VIDEO
                else -> MediaType.IMAGE
            }
            return MediaItemModel(url, type)
        }

        fun fromUrls(urls: List<String>): List<MediaItemModel> {
            return urls.map { fromUrl(it) }
        }
    }
}

@Serializable
enum class MediaType {
    IMAGE,
    VIDEO
}

private fun String.isVideoUrl(): Boolean {
    val videoExtensions = listOf(".mp4", ".mov", ".avi", ".mkv", ".3gp", ".webm", ".flv", ".wmv")
    return videoExtensions.any { this.lowercase().endsWith(it) }
}
