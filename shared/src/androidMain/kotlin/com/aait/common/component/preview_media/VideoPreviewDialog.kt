package com.aait.common.component.preview_media

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
import com.aait.domain.entity.general.MediaItemModel
import com.aait.domain.entity.general.MediaType
import com.aait.ui.theme.colorWhite

/* -------------------------------------------------------------------
   TEST DATA
------------------------------------------------------------------- */

private const val TEST_VIDEO_1 =
    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"

private const val TEST_VIDEO_2 =
    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"

private const val TEST_THUMBNAIL =
    "https://picsum.photos/600/400"

/* -------------------------------------------------------------------
   SINGLE VIDEO PREVIEW DIALOG
------------------------------------------------------------------- */

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

            // Your existing video player composable
            VideoPlayerContentView(
                player = player,
                modifier = Modifier.fillMaxSize(),
                onToggleControls = {},
                onShowControls = {},
                onHideControls = {}
            )

            // Close button
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

/* -------------------------------------------------------------------
   MULTI VIDEO PREVIEW (TEST VERSION)
------------------------------------------------------------------- */

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


@Composable
fun SingleVideoPreview(
    url: String,
    onDismiss: () -> Unit
) {
    VideoPreviewDialog(
        videoUrl = TEST_VIDEO_1,
        onDismiss = onDismiss
    )
}

@Composable
fun VideoPreviewTestScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        SingleVideoPreview(TEST_VIDEO_1) {}
    }
}
