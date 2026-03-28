package com.mahalatk.common.component.videopicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import platform.Foundation.NSData
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UIKit.UIApplication
import platform.darwin.NSObject
import platform.posix.memcpy

@Composable
actual fun rememberVideoPickerLauncher(
    onVideoPicked: (ByteArray) -> Unit
): () -> Unit {
    val delegate = remember {
        VideoPickerDelegate(onVideoPicked)
    }

    return {
        val configuration = PHPickerConfiguration()
        configuration.filter = PHPickerFilter.videosFilter
        configuration.selectionLimit = 1

        val picker = PHPickerViewController(configuration = configuration)
        picker.delegate = delegate

        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootViewController?.presentViewController(picker, animated = true, completion = null)
    }
}

private class VideoPickerDelegate(
    private val onVideoPicked: (ByteArray) -> Unit
) : NSObject(), PHPickerViewControllerDelegateProtocol {

    override fun picker(picker: PHPickerViewController, didFinishPicking: List<*>) {
        picker.dismissViewControllerAnimated(true, completion = null)

        val result = didFinishPicking.firstOrNull() as? PHPickerResult ?: return
        val provider = result.itemProvider ?: return

        if (provider.hasItemConformingToTypeIdentifier("public.movie")) {
            provider.loadDataRepresentationForTypeIdentifier("public.movie") { data, error ->
                if (error == null && data != null) {
                    val bytes = data.toVideoByteArray()
                    onVideoPicked(bytes)
                }
            }
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun NSData.toVideoByteArray(): ByteArray {
    val size = length.toInt()
    val byteArray = ByteArray(size)
    if (size > 0) {
        memcpy(byteArray.refTo(0), bytes, length)
    }
    return byteArray
}
