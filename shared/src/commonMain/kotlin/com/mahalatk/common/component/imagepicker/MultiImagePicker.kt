package com.mahalatk.common.component.imagepicker

import androidx.compose.runtime.Composable

/**
 * Remembers a multi-image picker launcher that works on both Android and iOS.
 * Each picked image is automatically compressed before being returned.
 *
 * @param onImagesPicked callback with the list of compressed images as ByteArray
 * @return a lambda that launches the multi-image picker when invoked
 */
@Composable
expect fun rememberMultiImagePickerLauncher(
    onImagesPicked: (List<ByteArray>) -> Unit
): () -> Unit
