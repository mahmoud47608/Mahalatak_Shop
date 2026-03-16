package com.aait.ui.component.attachment

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
import com.aait.ui.component.text.DefaultText
import com.aait.ui.picker.file.rememberFilePicker
import com.aait.ui.picker.image.rememberImagePicker
import com.aait.ui.theme.ColorLightTokens
import com.aait.ui.theme.PaddingDimensions
import com.mahalatak.shared.ui.R

enum class AttachmentFileTypeMode {
    IMAGE_ONLY,
    IMAGE_AND_PDF
}

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
        DefaultText(
            text = label,
            textStyle = MaterialTheme.typography.labelLarge,
            textColor = ColorLightTokens.OnBackground
        )

        DefaultText(
            text = supportedFormatsText,
            textStyle = MaterialTheme.typography.bodySmall,
            textColor = ColorLightTokens.Tertiary
        )

        Button(
            onClick = {
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

        if (selectedFiles.isNotEmpty()) {
            AttachmentViewer(
                modifier = Modifier.fillMaxWidth(),
                attachmentUrls = selectedFiles,
                editMode = true,
                onDeleteAttachment = onFileRemoved
            )
        }

        errorMessage?.let { error ->
            DefaultText(
                text = error,
                textStyle = MaterialTheme.typography.bodySmall,
                textColor = ColorLightTokens.Error
            )
        }
    }

}
