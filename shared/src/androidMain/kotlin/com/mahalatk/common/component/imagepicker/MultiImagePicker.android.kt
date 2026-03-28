package com.mahalatk.common.component.imagepicker

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.io.ByteArrayOutputStream

@Composable
actual fun rememberMultiImagePickerLauncher(
    onImagesPicked: (List<ByteArray>) -> Unit
): () -> Unit {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            val compressed = uris.mapNotNull { uri ->
                context.contentResolver.openInputStream(uri)?.use { stream ->
                    val originalBytes = stream.readBytes()
                    compressImageBytes(originalBytes)
                }
            }
            onImagesPicked(compressed)
        }
    }

    return { launcher.launch("image/*") }
}

private fun compressImageBytes(imageBytes: ByteArray): ByteArray {
    val original = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        ?: return imageBytes

    val resized = resizeMultiBitmap(original, MAX_IMAGE_DIMENSION)

    val outputStream = ByteArrayOutputStream()
    resized.compress(
        Bitmap.CompressFormat.JPEG,
        (COMPRESSION_QUALITY * 100).toInt(),
        outputStream
    )

    if (resized != original) resized.recycle()
    original.recycle()

    return outputStream.toByteArray()
}

private fun resizeMultiBitmap(bitmap: Bitmap, maxDimension: Int): Bitmap {
    val width = bitmap.width
    val height = bitmap.height

    if (width <= maxDimension && height <= maxDimension) return bitmap

    val ratio = width.toFloat() / height.toFloat()
    val newWidth: Int
    val newHeight: Int

    if (width > height) {
        newWidth = maxDimension
        newHeight = (maxDimension / ratio).toInt()
    } else {
        newHeight = maxDimension
        newWidth = (maxDimension * ratio).toInt()
    }

    return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
}
