package com.mahalatk.features.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahalatk.common.component.imagepicker.rememberImagePickerLauncher
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.ic_camera
import mahalatk.shared.generated.resources.ic_send
import mahalatk.shared.generated.resources.online
import mahalatk.shared.generated.resources.type_message
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChatDetailScreen(
    chatId: String,
    customerName: String,
    onBack: () -> Unit,
    viewModel: ChatDetailViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    val pickImage = rememberImagePickerLauncher { bytes ->
        // TODO: send image as message
    }

    LaunchedEffect(customerName) {
        viewModel.setCustomerName(customerName)
    }

    // Auto-scroll to bottom when new message
    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.lastIndex)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.ScreenBackground)
            .windowInsetsPadding(WindowInsets.ime),
    ) {
        val headerGradient = remember {
            Brush.verticalGradient(
                colors = listOf(AppColor.Primary, AppColor.Primary),
            )
        }

        // ── Header ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(brush = headerGradient)
                .padding(top = 40.dp, bottom = 14.dp, start = 16.dp, end = 16.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                // Back button
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.25f),
                            shape = CircleShape,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.size(34.dp),
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(22.dp),
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Avatar
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = customerName.take(1).uppercase(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Name + status
                Column {
                    Text(
                        text = customerName,
                        style = MahalatkTheme.titleSmall,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = stringResource(Res.string.online),
                        style = MahalatkTheme.labelSmall,
                        color = AppColor.Success,
                    )
                }
            }
        }

        // ── Messages ──
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item { Spacer(modifier = Modifier.height(12.dp)) }
            items(state.messages, key = { it.id }) { message ->
                MessageBubble(message = message)
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
        }

        // ── Input Bar ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Camera button
            IconButton(
                onClick = { pickImage() },
                modifier = Modifier.size(40.dp),
            ) {
                Image(
                    painter = painterResource(Res.drawable.ic_camera),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Text input
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = AppColor.ScreenBackground,
                        shape = RoundedCornerShape(24.dp),
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            ) {
                if (state.messageText.isEmpty()) {
                    Text(
                        text = stringResource(Res.string.type_message),
                        style = MahalatkTheme.bodyMedium,
                        color = AppColor.TextHint,
                    )
                }
                BasicTextField(
                    value = state.messageText,
                    onValueChange = viewModel::onMessageTextChanged,
                    textStyle = MahalatkTheme.bodyMedium.copy(color = AppColor.TextPrimary),
                    cursorBrush = SolidColor(AppColor.Primary),
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Send button
            val sendInteractionSource = remember { MutableInteractionSource() }
            val isSendPressed by sendInteractionSource.collectIsPressedAsState()
            val sendScale by animateFloatAsState(
                targetValue = if (isSendPressed) 0.88f else 1f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
            )

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .graphicsLayer {
                        scaleX = sendScale
                        scaleY = sendScale
                    }
                    .clip(CircleShape)
                    .background(AppColor.Primary)
                    .clickable(
                        interactionSource = sendInteractionSource,
                        indication = null,
                    ) { viewModel.sendMessage() },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_send),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}

// ──────────────────────────────────────────────
// Message Bubble
// ──────────────────────────────────────────────
@Composable
private fun MessageBubble(message: ChatMessage) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(300)) +
                slideInVertically(initialOffsetY = { it / 4 }, animationSpec = tween(300)),
    ) {
        val isMe = message.isMe
        val bgColor = if (isMe) AppColor.Primary else Color.White
        val textColor = if (isMe) Color.White else AppColor.TextPrimary
        val timeColor = if (isMe) Color.White.copy(alpha = 0.7f) else AppColor.TextHint
        val shape = if (isMe) {
            RoundedCornerShape(16.dp, 16.dp, 4.dp, 16.dp)
        } else {
            RoundedCornerShape(16.dp, 16.dp, 16.dp, 4.dp)
        }
        val alignment = if (isMe) Alignment.End else Alignment.Start

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = alignment,
        ) {
            Box(
                modifier = Modifier
                    .widthIn(max = 280.dp)
                    .background(color = bgColor, shape = shape)
                    .padding(horizontal = 14.dp, vertical = 10.dp),
            ) {
                Column {
                    Text(
                        text = message.text,
                        style = MahalatkTheme.bodyMedium,
                        color = textColor,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = message.time,
                        style = MahalatkTheme.labelSmall,
                        color = timeColor,
                        modifier = Modifier.align(Alignment.End),
                    )
                }
            }
        }
    }
}
