package com.aait.ui.picker.file

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.aait.ui.util.files.FileUtils
import kotlinx.coroutines.launch

@Composable
fun rememberFilePicker(
    onFileSelected: (String) -> Unit
): () -> Unit {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

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
