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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.aait.ui.theme.colorWhite
import com.aait.domain.entity.general.MediaItemModel
import com.aait.domain.entity.general.MediaType

@Composable
fun VideoPreviewDialog(
    videoUrl: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    val player = remember(videoUrl) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            player.release()
        }
    }

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
            VideoPlayerContentView(
                player = player,
                modifier = Modifier.fillMaxSize(),
                onToggleControls = {},
                onShowControls = {},
                onHideControls = {}
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
fun MultiVideoPreviewDialog(
    videos: List<String>,
    initialPage: Int = 0,
    onDismiss: () -> Unit
) {
    MediaPreviewDialog(
        mediaItems = videos.map { MediaItemModel(url = it, type = MediaType.VIDEO) },
        initialPage = initialPage,
        onDismiss = onDismiss
    )
}
