package com.mahalatk.common.component.imagepicker

import androidx.compose.ui.graphics.ImageBitmap

/**
 * Converts a ByteArray (image data) to an ImageBitmap for display in Compose.
 */
expect fun ByteArray.toImageBitmap(): ImageBitmap?
