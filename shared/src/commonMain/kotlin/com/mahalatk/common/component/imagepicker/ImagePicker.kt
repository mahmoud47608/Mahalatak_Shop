package com.mahalatk.common.component.imagepicker

import androidx.compose.runtime.Composable

/**
 * Maximum dimension (width or height) for the compressed image.
 */
const val MAX_IMAGE_DIMENSION = 800

/**
 * JPEG compression quality (0.0 - 1.0).
 */
const val COMPRESSION_QUALITY = 0.7

/**
 * Remembers a single-image picker launcher that works on both Android and iOS.
 * The picked image is automatically compressed before being returned.
 *
 * @param onImagePicked callback with the compressed image as ByteArray
 * @return a lambda that launches the image picker when invoked
 */
@Composable
expect fun rememberImagePickerLauncher(
    onImagePicked: (ByteArray) -> Unit
): () -> Unit
