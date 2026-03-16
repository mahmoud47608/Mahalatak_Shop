package com.aait.ui.component.preview_media

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.aait.ui.component.image.ZoomableImageView
import com.aait.ui.theme.colorWhite
import com.aait.domain.entity.general.MediaItemModel
import com.aait.domain.entity.general.MediaType

@Composable
fun SingleImagePreviewDialog(
    imageUrl: String,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            ZoomableImageView(
                imageUrl = imageUrl,
                modifier = Modifier.fillMaxSize(),
                onToggleControls = { },
                onShowControls = { },
                onHideControls = { }
            )

            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close",
                    tint = colorWhite(),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun MultiImagePreviewDialog(
    images: List<String>,
    initialPage: Int = 0,
    onDismiss: () -> Unit
) {
    MediaPreviewDialog(
        mediaItems = images.map { MediaItemModel(url = it, type = MediaType.IMAGE) },
        initialPage = initialPage,
        onDismiss = onDismiss
    )
}
