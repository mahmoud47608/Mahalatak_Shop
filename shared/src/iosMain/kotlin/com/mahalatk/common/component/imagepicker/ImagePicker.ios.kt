package com.mahalatk.common.component.imagepicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSData
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UIKit.UIApplication
import platform.UIKit.UIGraphicsBeginImageContextWithOptions
import platform.UIKit.UIGraphicsEndImageContext
import platform.UIKit.UIGraphicsGetImageFromCurrentImageContext
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.darwin.NSObject
import platform.posix.memcpy

@Composable
actual fun rememberImagePickerLauncher(
    onImagePicked: (ByteArray) -> Unit
): () -> Unit {
    val delegate = remember {
        ImagePickerDelegate(onImagePicked)
    }

    return {
        val configuration = PHPickerConfiguration()
        configuration.filter = PHPickerFilter.imagesFilter
        configuration.selectionLimit = 1

        val picker = PHPickerViewController(configuration = configuration)
        picker.delegate = delegate

        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootViewController?.presentViewController(picker, animated = true, completion = null)
    }
}

private class ImagePickerDelegate(
    private val onImagePicked: (ByteArray) -> Unit
) : NSObject(), PHPickerViewControllerDelegateProtocol {

    override fun picker(picker: PHPickerViewController, didFinishPicking: List<*>) {
        picker.dismissViewControllerAnimated(true, completion = null)

        val result = didFinishPicking.firstOrNull() as? PHPickerResult ?: return
        val provider = result.itemProvider ?: return

        if (provider.hasItemConformingToTypeIdentifier("public.image")) {
            provider.loadDataRepresentationForTypeIdentifier("public.image") { data, error ->
                if (error != null || data == null) return@loadDataRepresentationForTypeIdentifier
                val image = UIImage(data = data) ?: return@loadDataRepresentationForTypeIdentifier

                // Resize then compress
                val resized = resizeImage(image, MAX_IMAGE_DIMENSION)
                val compressedData = UIImageJPEGRepresentation(resized, COMPRESSION_QUALITY)
                    ?: return@loadDataRepresentationForTypeIdentifier
                val bytes = compressedData.toByteArray()
                onImagePicked(bytes)
            }
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun resizeImage(image: UIImage, maxDimension: Int): UIImage {
    val imageSize = image.size.useContents { this }
    val width = imageSize.width
    val height = imageSize.height

    if (width <= maxDimension && height <= maxDimension) return image

    val ratio = width / height
    val newWidth: Double
    val newHeight: Double

    if (width > height) {
        newWidth = maxDimension.toDouble()
        newHeight = maxDimension.toDouble() / ratio
    } else {
        newHeight = maxDimension.toDouble()
        newWidth = maxDimension.toDouble() * ratio
    }

    val newSize = CGSizeMake(newWidth, newHeight)
    UIGraphicsBeginImageContextWithOptions(newSize, false, 1.0)
    image.drawInRect(CGRectMake(0.0, 0.0, newWidth, newHeight))
    val resizedImage = UIGraphicsGetImageFromCurrentImageContext()
    UIGraphicsEndImageContext()

    return resizedImage ?: image
}

@OptIn(ExperimentalForeignApi::class)
private fun NSData.toByteArray(): ByteArray {
    val size = length.toInt()
    val byteArray = ByteArray(size)
    if (size > 0) {
        memcpy(byteArray.refTo(0), bytes, length)
    }
    return byteArray
}
