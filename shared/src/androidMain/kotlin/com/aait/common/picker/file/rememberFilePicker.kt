package com.aait.common.picker.file

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.aait.ui.util.files.FileUtils
import kotlinx.coroutines.launch

/**
 * Composable function that returns a PDF picker launcher.
 * Opens PDF picker and returns the file path via the callback.
 *
 * @param onFileSelected Callback invoked when a PDF is selected with its local file path.
 * @return Function to trigger the PDF picker.
 */
@Composable
fun rememberFilePicker(
    onFileSelected: (String) -> Unit
): () -> Unit {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // PDF picker launcher
    val pdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            scope.launch {
                val file = FileUtils(context).createTmpFileFromUri(uri)
                if (file != null && file.exists()) {
                    onFileSelected(file.absolutePath)
                }
            }
        }
    }

    return { pdfLauncher.launch("application/pdf") }
}
