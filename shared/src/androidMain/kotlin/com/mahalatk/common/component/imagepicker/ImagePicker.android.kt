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
actual fun rememberImagePickerLauncher(
    onImagePicked: (ByteArray) -> Unit
): () -> Unit {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            context.contentResolver.openInputStream(it)?.use { stream ->
                val originalBytes = stream.readBytes()
                val compressed = compressImage(originalBytes)
                onImagePicked(compressed)
            }
        }
    }

    return { launcher.launch("image/*") }
}

private fun compressImage(imageBytes: ByteArray): ByteArray {
    val original = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        ?: return imageBytes

    // Resize if larger than max dimension
    val resized = resizeBitmap(original, MAX_IMAGE_DIMENSION)

    // Compress to JPEG
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

private fun resizeBitmap(bitmap: Bitmap, maxDimension: Int): Bitmap {
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
