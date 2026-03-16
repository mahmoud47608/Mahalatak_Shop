package com.aait.base.common.component.attachment

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
import com.aait.base.R
import com.aait.base.ui.theme.PaddingDimensions

/**
 * Attachment type enum for distinguishing between images, PDFs, videos, and general files.
 * Also provides icon and title resources for attachment picker components.
 */
enum class AttachmentType(
    @DrawableRes val iconRes: Int,
    @StringRes val defaultTitleRes: Int
) {
    IMAGE(R.drawable.ic_attach_image, R.string.attach_image),
    PDF(R.drawable.ic_attach_file, R.string.attach_file),
    VIDEO(R.drawable.ic_attach_video, R.string.attach_video),
    FILE(R.drawable.ic_attach_file, R.string.attach_file)
}

/**
 * Main attachment viewer component that displays a horizontal scrollable row of attachments
 *
 * @param modifier Modifier for the container
 * @param attachmentUrls List of attachment URLs (images or PDFs)
 * @param editMode Whether to show delete icons on thumbnails
 * @param onDeleteAttachment Callback when delete icon is clicked, receives the URL to delete
 * @param thumbnailSize Size of each thumbnail (default 60.dp)
 */
@Composable
fun AttachmentViewer(
    modifier: Modifier = Modifier,
    attachmentUrls: List<String>,
    editMode: Boolean = false,
    onDeleteAttachment: ((String) -> Unit)? = null,
    thumbnailSize: Dp = 60.dp
) {
    // Don't render if list is empty
    if (attachmentUrls.isEmpty()) return

    // State for preview dialog
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

/**
 * Helper function to determine attachment type based on URL extension
 *
 * @param url The attachment URL
 * @return AttachmentType.PDF for PDFs, AttachmentType.VIDEO for videos, AttachmentType.IMAGE for images or unknown types
 */
private fun getAttachmentType(url: String): AttachmentType {
    return when {
        url.endsWith(".pdf", ignoreCase = true) -> AttachmentType.PDF
        url.matches(Regex(".*\\.(mp4|mov|avi|mkv|webm|m4v)$", RegexOption.IGNORE_CASE))
            -> AttachmentType.VIDEO
        url.matches(Regex(".*\\.(jpg|jpeg|png|webp)$", RegexOption.IGNORE_CASE))
            -> AttachmentType.IMAGE
        else -> AttachmentType.IMAGE // Fallback to image for unknown types
    }
}
