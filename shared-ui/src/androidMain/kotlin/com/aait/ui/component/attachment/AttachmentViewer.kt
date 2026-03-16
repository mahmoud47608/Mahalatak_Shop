package com.aait.ui.component.attachment

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.aait.ui.theme.PaddingDimensions
import com.mahalatak.shared.ui.R

enum class AttachmentType(
    @DrawableRes val iconRes: Int,
    @StringRes val defaultTitleRes: Int
) {
    IMAGE(R.drawable.ic_attach_image, R.string.attach_image),
    PDF(R.drawable.ic_attach_file, R.string.attach_file),
    VIDEO(R.drawable.ic_attach_video, R.string.attach_video),
    FILE(R.drawable.ic_attach_file, R.string.attach_file)
}

@Composable
fun AttachmentViewer(
    modifier: Modifier = Modifier,
    attachmentUrls: List<String>,
    editMode: Boolean = false,
    onDeleteAttachment: ((String) -> Unit)? = null,
    thumbnailSize: Dp = 60.dp
) {
    if (attachmentUrls.isEmpty()) return

    var selectedAttachment by remember { mutableStateOf<String?>(null) }
    var showPreview by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(PaddingDimensions.medium)
    ) {
        attachmentUrls.forEach { url ->
            AttachmentThumbnail(
                url = url,
                size = thumbnailSize,
                attachmentType = getAttachmentType(url),
                editMode = editMode,
                onClick = {
                    selectedAttachment = url
                    showPreview = true
                },
                onDelete = onDeleteAttachment?.let { callback -> { callback(url) } }
            )
        }
    }
}

private fun getAttachmentType(url: String): AttachmentType {
    return when {
        url.endsWith(".pdf", ignoreCase = true) -> AttachmentType.PDF
        url.matches(Regex(".*\\.(mp4|mov|avi|mkv|webm|m4v)$", RegexOption.IGNORE_CASE))
            -> AttachmentType.VIDEO

        url.matches(Regex(".*\\.(jpg|jpeg|png|webp)$", RegexOption.IGNORE_CASE))
            -> AttachmentType.IMAGE

        else -> AttachmentType.IMAGE
    }
}
