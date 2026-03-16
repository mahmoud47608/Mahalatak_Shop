package com.aait.ui.component.preview_media

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
import com.aait.ui.component.banner.PagerIndicator
import com.aait.ui.component.image.DefaultImage
import com.aait.ui.component.image.ZoomableImageView
import com.aait.ui.component.utilis.noRippleClickable
import com.aait.ui.theme.colorPrimary
import com.aait.ui.theme.colorText
import com.aait.ui.theme.colorTextHint
import com.aait.ui.theme.colorWhite
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

    val swipeOffset = remember { Animatable(0f) }

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

    LaunchedEffect(Unit) {
        if (mediaItems.size > 1) {
            delay(500)
            swipeOffset.animateTo(
                targetValue = -80f,
                animationSpec = tween(250)
            )
            swipeOffset.animateTo(
                targetValue = 0f,
                animationSpec = tween(200)
            )
            delay(100)
            swipeOffset.animateTo(
                targetValue = 80f,
                animationSpec = tween(250)
            )
            swipeOffset.animateTo(
                targetValue = 0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
    }

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

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { translationX = swipeOffset.value },
                    pageSpacing = 16.dp
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
        DefaultImage(
            url = mediaItem.url,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

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
