package com.aait.base.util.files

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
//noinspection ExifInterface
import android.media.ExifInterface
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object CompressUtil {
    suspend fun compressImageToWebPInCache(context: Context, inputFilePath: String?): String? {
        if (inputFilePath == null) return null

        return withContext(Dispatchers.IO) {
            try {
                val inputFile = File(inputFilePath)
                val originalSize = inputFile.length() / (1024.0 * 1024.0)
                Log.d("Compress", "Compression Log Original file size: %.2f MB $originalSize")

                var bitmap = decodeScaledBitmap(inputFile) ?: return@withContext null
                bitmap = rotateIfNeeded(bitmap, inputFile.absolutePath)

                val cacheDir = context.cacheDir
                val outputFile = File(cacheDir, "compressed_${System.currentTimeMillis()}.webp")

                val compressFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    Bitmap.CompressFormat.WEBP_LOSSY
                } else {
                    Bitmap.CompressFormat.WEBP
                }

                FileOutputStream(outputFile).use { outStream ->
                    val result = bitmap.compress(compressFormat, 97, outStream)

                    if (result) {
                        val compressedSize = outputFile.length() / (1024.0 * 1024.0)
                        Log.d(
                            "Compress",
                            "Compression Log Compressed file size: %.2f MB $compressedSize"
                        )

                        return@withContext outputFile.absolutePath
                    } else {
                        Log.e("Compress", "Compression Log Compression failed")
                        return@withContext inputFilePath
                    }
                }
            } catch (e: IOException) {
                Log.e("Compress", "Compression Log Error during image compression $e")
                return@withContext inputFilePath
            }
        }
    }

    private fun decodeScaledBitmap(file: File, maxDimension: Int = 3000): Bitmap? {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeFile(file.absolutePath, options)

        val (height, width) = options.outHeight to options.outWidth
        var inSampleSize = 1

        while (height / inSampleSize > maxDimension || width / inSampleSize > maxDimension) {
            inSampleSize *= 2
        }

        return BitmapFactory.decodeFile(file.absolutePath, BitmapFactory.Options().apply {
            this.inSampleSize = inSampleSize
        })
    }


    private fun rotateIfNeeded(bitmap: Bitmap, path: String): Bitmap {
        val ei = ExifInterface(path)
        val orientation: Int = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        val rotatedBitmap: Bitmap?
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> {
                rotatedBitmap = rotateImage(bitmap, 90f)
            }

            ExifInterface.ORIENTATION_ROTATE_180 -> {
                rotatedBitmap = rotateImage(bitmap, 180f)
            }

            ExifInterface.ORIENTATION_ROTATE_270 -> {
                rotatedBitmap = rotateImage(bitmap, 270f)
            }

            ExifInterface.ORIENTATION_NORMAL -> {
                rotatedBitmap = bitmap
            }

            else -> {
                rotatedBitmap = bitmap
            }
        }

        return rotatedBitmap
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }


    suspend fun Uri?.compressToString(context: Context): String? {
        if (this == null) return null

        // Detect MIME type to determine if it's a video or image
        val mimeType = context.contentResolver.getType(this)
        val file = FileUtils(context).createTmpFileFromUri(this) ?: return null

        return if (mimeType?.startsWith("video/") == true) {
            // Handle video compression
            compressVideoToCache(context, file.path)
        } else {
            // Handle image compression (default)
            compressImageToWebPInCache(context, file.path)
        }
    }


    suspend fun compressVideoToCache(context: Context, inputFilePath: String?): String? {
        if (inputFilePath == null) return null

        return withContext(Dispatchers.IO) {
            try {
                val inputFile = File(inputFilePath)
                if (!inputFile.exists()) return@withContext null

                val originalSize = inputFile.length() / (1024.0 * 1024.0)
                Log.d("Compress", "Video Compression - Original file size: %.2f MB".format(originalSize))

                // Check video duration and size
                val retriever = MediaMetadataRetriever()
                retriever.setDataSource(inputFilePath)
                val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: 0
                retriever.release()

                val durationInSeconds = duration / 1000
                Log.d("Compress", "Video Compression - Duration: $durationInSeconds seconds")

                // Create output file in cache
                val cacheDir = context.cacheDir
                val outputFile = File(cacheDir, "compressed_video_${System.currentTimeMillis()}.mp4")

                // For now, copy the video to cache directory
                // TODO: Implement actual video compression using MediaCodec or FFmpeg
                inputFile.copyTo(outputFile, overwrite = true)

                val compressedSize = outputFile.length() / (1024.0 * 1024.0)
                Log.d("Compress", "Video Compression - Compressed file size: %.2f MB".format(compressedSize))

                return@withContext outputFile.absolutePath
            } catch (e: Exception) {
                Log.e("Compress", "Video Compression - Error during video compression", e)
                return@withContext inputFilePath
            }
        }
    }

}