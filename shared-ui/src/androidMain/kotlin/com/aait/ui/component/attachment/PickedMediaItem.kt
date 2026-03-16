package com.aait.ui.component.attachment

import java.io.File

sealed class PickedMediaItem {
    abstract val file: File
    abstract val uri: String?

    data class Image(
        override val file: File,
        override val uri: String? = null
    ) : PickedMediaItem()

    data class Video(
        override val file: File,
        override val uri: String? = null,
        val thumbnailPath: String? = null
    ) : PickedMediaItem()
}
