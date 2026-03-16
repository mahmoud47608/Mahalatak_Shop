package com.aait.base.common.component.attachment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aait.base.common.component.text.DefaultText
import com.aait.base.common.picker.file.rememberFilePicker
import com.aait.base.common.picker.image.rememberImagePicker
import com.aait.base.ui.theme.ColorLightTokens
import com.aait.base.ui.theme.PaddingDimensions
import com.mahalatak.R

/**
 * File type support mode for attachment upload
 */
enum class AttachmentFileTypeMode {
    IMAGE_ONLY,      // Only images allowed
    IMAGE_AND_PDF    // Both images and PDFs allowed
}

/**
 * Reusable attachment upload section component
 * Displays file picker, selected files, and handles file selection
 *
 * @param modifier Modifier for the container
 * @param selectedFiles List of selected file URLs/paths
 * @param fileTypeMode Whether to support only images or both images and PDFs
 * @param onFileSelected Callback when a file is selected
 * @param onFileRemoved Callback when a file is removed
 * @param errorMessage Optional error message to display
 * @param label Optional custom label (defaults to "Attachments")
 * @param supportedFormatsText Optional custom supported formats text
 */
@Composable
fun AttachmentUploadSection(
    modifier: Modifier = Modifier,
    selectedFiles: List<String>,
    fileTypeMode: AttachmentFileTypeMode = AttachmentFileTypeMode.IMAGE_AND_PDF,
    onFileSelected: (String) -> Unit,
    onFileRemoved: (String) -> Unit,
    errorMessage: String? = null,
    label: String = stringResource(R.string.attachments),
    supportedFormatsText: String = stringResource(R.string.supported_formats)
) {
    var showFilePickerSheet by remember { mutableStateOf(false) }

    val pickImage = rememberImagePicker { filePath ->
        onFileSelected(filePath)
    }

    rememberFilePicker { filePath ->
        onFileSelected(filePath)
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(PaddingDimensions.medium)
    ) {
        // Label
        DefaultText(
            text = label,
            textStyle = MaterialTheme.typography.labelLarge,
            textColor = ColorLightTokens.OnBackground
        )

        // Supported formats text
        DefaultText(
            text = supportedFormatsText,
            textStyle = MaterialTheme.typography.bodySmall,
            textColor = ColorLightTokens.Tertiary
        )

        // Browse Button
        Button(
            onClick = {
                // If image only mode, pick image directly
                // Otherwise show bottom sheet to choose file type
                if (fileTypeMode == AttachmentFileTypeMode.IMAGE_ONLY) {
                    pickImage()
                } else {
                    showFilePickerSheet = true
                }
            },
            modifier = Modifier.height(32.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                text = stringResource(R.string.browse_attachments),
                style = MaterialTheme.typography.bodySmall,
                color = Color.White
            )
        }

        // Selected Files Display
        if (selectedFiles.isNotEmpty()) {
            AttachmentViewer(
                modifier = Modifier.fillMaxWidth(),
                attachmentUrls = selectedFiles,
                editMode = true, // Show delete icons in edit mode
                onDeleteAttachment = onFileRemoved
            )
        }

        // Error message
        errorMessage?.let { error ->
            DefaultText(
                text = error,
                textStyle = MaterialTheme.typography.bodySmall,
                textColor = ColorLightTokens.Error
            )
        }
    }

}
