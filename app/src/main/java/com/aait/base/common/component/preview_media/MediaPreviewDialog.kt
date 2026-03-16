package com.aait.base.common.component.preview_media

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.aait.base.common.component.banner.PagerIndicator
import com.aait.base.common.component.image.DefaultImage
import com.aait.base.common.component.image.ZoomableImageView
import com.aait.base.common.component.utilis.noRippleClickable
import com.aait.base.ui.theme.colorPrimary
import com.aait.base.ui.theme.colorText
import com.aait.base.ui.theme.colorTextHint
import com.aait.base.ui.theme.colorWhite
import com.aait.domain.entity.general.MediaItemModel
import com.aait.domain.entity.general.MediaType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MediaPreviewDialog(
    mediaItems: List<MediaItemModel>,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    initialPage: Int = 0
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = initialPage.coerceIn(0, mediaItems.size - 1)
    ) { mediaItems.size }

    var isControlsVisible by remember { mutableStateOf(true) }

    // Swipe hint animation state
    val swipeOffset = remember { Animatable(0f) }

    // Create ExoPlayer instances for videos
    val players = remember(mediaItems) {
        mediaItems.map { mediaItem ->
            if (mediaItem.type == MediaType.VIDEO) {
                ExoPlayer.Builder(context).build().apply {
                    setMediaItem(androidx.media3.common.MediaItem.fromUri(mediaItem.url))
                    prepare()
                    repeatMode = Player.REPEAT_MODE_OFF
                }
            } else {
                null
            }
        }
    }

    // Trigger swipe hint animation on enter if there are multiple items
    LaunchedEffect(Unit) {
        if (mediaItems.size > 1) {
            delay(500)
            // Nudge left to hint next item
            swipeOffset.animateTo(
                targetValue = -80f,
                animationSpec = tween(250)
            )
            // Return to center
            swipeOffset.animateTo(
                targetValue = 0f,
                animationSpec = tween(200)
            )
            // Small delay between animations
            delay(100)
            // Nudge right to hint previous item
            swipeOffset.animateTo(
                targetValue = 80f,
                animationSpec = tween(250)
            )
            // Bounce back to center
            swipeOffset.animateTo(
                targetValue = 0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
    }

    // Control video playback based on current page
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            players.forEachIndexed { index, player ->
                player?.let {
                    if (index == page) {
                        it.playWhenReady = true
                    } else {
                        it.pause()
                    }
                }
            }
        }
    }

    // Release all video players when composable leaves composition
    DisposableEffect(Unit) {
        onDispose {
            players.forEach { it?.release() }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // 1. Top Bar - Always visible
            AnimatedVisibility(
                visible = isControlsVisible,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Page counter
                    if (mediaItems.size > 1) {
                        Box(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "${pagerState.currentPage + 1} / ${mediaItems.size}",
                                color = colorText(),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    // Close button
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = colorText(),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = isControlsVisible,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                HorizontalDivider(color = colorTextHint().copy(alpha = 0.5f), thickness = 1.dp)
            }

            // 2. Main Pager Area - Fills remaining space
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { translationX = swipeOffset.value }, // Apply hint offset
                    pageSpacing = 16.dp // Add some spacing between items
                ) { page ->
                    val mediaItem = mediaItems[page]
                    when (mediaItem.type) {
                        MediaType.IMAGE -> {
                            ZoomableImageView(
                                imageUrl = mediaItem.url,
                                modifier = Modifier.fillMaxSize(),
                                onToggleControls = { isControlsVisible = !isControlsVisible },
                                onShowControls = { isControlsVisible = true },
                                onHideControls = { isControlsVisible = false }
                            )
                        }
                        MediaType.VIDEO -> {
                            players[page]?.let { player ->
                                VideoPlayerContentView(
                                    player = player,
                                    modifier = Modifier.fillMaxSize(),
                                    onToggleControls = { isControlsVisible = !isControlsVisible },
                                    onShowControls = { isControlsVisible = true },
                                    onHideControls = { isControlsVisible = false }
                                )
                            }
                        }
                    }
                }
            }

            // 3. Bottom Grouped Section (Thumbnails + Indicators)
            AnimatedVisibility(
                visible = isControlsVisible,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                HorizontalDivider(color = colorTextHint().copy(alpha = 0.5f), thickness = 1.dp)
            }

            AnimatedVisibility(
                visible = isControlsVisible,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                if (mediaItems.size > 1) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Thumbnail Strip
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        ) {
                            items(mediaItems.size) { index ->
                                MediaThumbnailItem(
                                    mediaItem = mediaItems[index],
                                    isSelected = pagerState.currentPage == index,
                                    onClick = {
                                        scope.launch {
                                            pagerState.animateScrollToPage(index)
                                        }
                                    }
                                )
                            }
                        }

                        // Page Indicator Dots
                        PagerIndicator(
                            pageCount = mediaItems.size,
                            currentPage = pagerState.currentPage,
                            activeColor = colorPrimary(),
                            inactiveColor = colorTextHint().copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(UnstableApi::class)
@Composable
fun VideoPlayerContentView(
    player: ExoPlayer,
    onToggleControls: () -> Unit,
    onShowControls: () -> Unit,
    onHideControls: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Auto show/hide controls based on video playback
    DisposableEffect(player) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isPlaying) {
                    onHideControls()
                } else {
                    onShowControls()
                }
            }
        }
        player.addListener(listener)
        onDispose {
            player.removeListener(listener)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onToggleControls() }
                )
            }
    ) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    this.player = player
                    useController = true
                    controllerShowTimeoutMs = 3000
                    controllerHideOnTouch = true
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

/**
 * Thumbnail item for the media thumbnail strip
 */
@Composable
private fun MediaThumbnailItem(
    mediaItem: MediaItemModel,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(60.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(colorTextHint().copy(alpha = 0.1f))
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) colorPrimary() else colorTextHint().copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp)
            )
            .noRippleClickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        // Thumbnail image
        DefaultImage(
            url = mediaItem.url,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Play icon overlay for videos
        if (mediaItem.type == MediaType.VIDEO) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "Video",
                    tint = colorWhite(),
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

/**
 * Backward compatibility alias
 */
@Composable
fun MixedMediaPreviewDialog(
    mediaItems: List<MediaItemModel>,
    initialPage: Int = 0,
    onDismiss: () -> Unit
) {
    MediaPreviewDialog(
        mediaItems = mediaItems,
        initialPage = initialPage,
        onDismiss = onDismiss
    )
}

/**
 * Preview for MediaPreviewDialog with multiple items
 */
@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
private fun MediaPreviewDialogPreview() {
    val items = listOf(
        MediaItemModel(url = "https://images.unsplash.com/photo-1519167758481-83f550bb49b3", type = MediaType.IMAGE),
        MediaItemModel(url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4", type = MediaType.VIDEO),
        MediaItemModel(url = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d", type = MediaType.IMAGE)
    )
    MaterialTheme {
        MediaPreviewDialog(
            mediaItems = items,
            onDismiss = {}
        )
    }
}
