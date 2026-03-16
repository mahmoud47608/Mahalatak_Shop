package com.aait.base.util.files

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream

class FileUtils(private val context: Context) {

    /** Save Uri content to a file in external Downloads dir */
    fun saveFileToDownloads(uri: Uri): File? {
        return try {
            val fileName = context.contentResolver.getFileName(uri) ?: return null
            val downloadsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                ?: return null

            val file = File(downloadsDir, fileName)
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /** Create a temporary file in cache dir from Uri */
    fun createTmpFileFromUri(uri: Uri): File? {
        return try {
            val fileName = context.contentResolver.getFileName(uri) ?: "temp_file"

            // Extract extension from filename
            val extension = fileName.substringAfterLast('.', "")
            val nameWithoutExt = fileName.substringBeforeLast('.', fileName)

            // Create temp file with proper extension as suffix (File.createTempFile requires at least 3 chars for prefix)
            val prefix = if (nameWithoutExt.length >= 3) nameWithoutExt else "tmp_$nameWithoutExt"
            val suffix = if (extension.isNotEmpty()) ".$extension" else ""

            val file = File.createTempFile(prefix, suffix, context.cacheDir)

            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /** Extension: get display name of Uri */
    @SuppressLint("Range")
    fun ContentResolver.getFileName(uri: Uri): String? {
        var name: String? = null
        query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        }
        return name
    }


}