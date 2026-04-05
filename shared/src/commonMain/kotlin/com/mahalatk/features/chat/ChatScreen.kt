package com.mahalatk.features.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.animation.AnimatedListItem
import com.mahalatk.common.component.card.GlassCard
import com.mahalatk.common.component.empty.EmptyStatePlaceholder
import com.mahalatk.common.component.header.ScreenHeader
import com.mahalatk.common.component.image.UserAvatar
import com.mahalatk.common.component.loading.ShimmerBox
import com.mahalatk.common.component.loading.ShimmerCircle
import com.mahalatk.common.component.tabs.FilterTabs
import com.mahalatk.common.component.utilis.noRippleClickable
import com.mahalatk.navigation.LocalNavigator
import com.mahalatk.navigation.Route
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.CornerDimensions
import com.mahalatk.theme.MahalatkTheme
import kotlinx.coroutines.launch
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.ic_nav_chat
import mahalatk.shared.generated.resources.inquiries_chat
import mahalatk.shared.generated.resources.messages
import mahalatk.shared.generated.resources.no_messages
import mahalatk.shared.generated.resources.orders_chat
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChatScreen(viewModel: ChatViewModel = koinViewModel()) {
    val state by viewModel.uiState.collectAsState()
    val navigator = LocalNavigator.current
    val tabs = ChatTab.entries
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = tabs.indexOf(state.selectedTab).coerceAtLeast(0),
        pageCount = { tabs.size },
    )

    // Sync pager swipes -> viewModel
    LaunchedEffect(pagerState.currentPage) {
        val tab = tabs[pagerState.currentPage]
        if (state.selectedTab != tab) viewModel.selectTab(tab)
    }

    Column(
        modifier = Modifier.fillMaxSize().background(AppColor.ScreenBackground),
    ) {
        ScreenHeader(title = stringResource(Res.string.messages))

        Spacer(modifier = Modifier.height(16.dp))

        FilterTabs(
            tabs = listOf(
                ChatTab.Orders to stringResource(Res.string.orders_chat),
                ChatTab.Inquiries to stringResource(Res.string.inquiries_chat),
            ),
            selectedTab = tabs[pagerState.currentPage],
            onTabSelected = { tab ->
                coroutineScope.launch { pagerState.animateScrollToPage(tabs.indexOf(tab)) }
            },
            modifier = Modifier.padding(horizontal = 20.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            val tab = tabs[page]
            val pageConversations = state.conversations.filter { conv ->
                when (tab) {
                    ChatTab.Orders -> !conv.isInquiry
                    ChatTab.Inquiries -> conv.isInquiry
                }
            }

            if (state.isLoading) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 20.dp,
                        end = 20.dp,
                        top = 4.dp,
                        bottom = 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(4) { index ->
                        AnimatedListItem(index) { ChatItemSkeleton() }
                    }
                }
            } else if (pageConversations.isEmpty()) {
                EmptyStatePlaceholder(
                    icon = Res.drawable.ic_nav_chat,
                    message = stringResource(Res.string.no_messages),
                    iconTint = AppColor.TextHint.copy(alpha = 0.4f),
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    itemsIndexed(
                        pageConversations,
                        key = { _, c -> c.id },
                        contentType = { _, _ -> "chat" }) { index, conversation ->
                        AnimatedListItem(index) {
                            ChatItem(
                                conversation = conversation,
                                onClick = {
                                    navigator.push(
                                        Route.ChatDetail(conversation.id, conversation.customerName)
                                    )
                                },
                            )
                        }
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }
    }
}

@Composable
private fun ChatItemSkeleton() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(CornerDimensions.lg),
        colors = CardDefaults.cardColors(containerColor = AppColor.Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ShimmerCircle(size = 50.dp)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                ShimmerBox(width = 100.dp, height = 14.dp)
                Spacer(modifier = Modifier.height(6.dp))
                ShimmerBox(width = 160.dp, height = 10.dp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(horizontalAlignment = Alignment.End) {
                ShimmerBox(width = 40.dp, height = 10.dp)
                Spacer(modifier = Modifier.height(6.dp))
                ShimmerCircle(size = 22.dp)
            }
        }
    }
}

@Composable
private fun ChatItem(
    conversation: ChatConversation,
    onClick: () -> Unit,
) {
    GlassCard(
        modifier = Modifier.noRippleClickable { onClick() },
        cornerRadius = CornerDimensions.lg,
        contentPadding = 14.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Avatar with online indicator
            Box {
                UserAvatar(
                    initials = conversation.customerName,
                    imageUrl = conversation.imageUrl,
                    size = 50.dp,
                    backgroundColor = AppColor.Secondary.copy(alpha = 0.3f),
                )
                if (conversation.isOnline) {
                    Box(
                        modifier = Modifier.size(14.dp).clip(CircleShape)
                            .background(AppColor.Surface).padding(2.dp)
                            .clip(CircleShape).background(AppColor.Success)
                            .align(Alignment.BottomEnd),
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = conversation.customerName,
                    style = MahalatkTheme.titleSmall,
                    color = AppColor.TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = conversation.lastMessage,
                    style = MahalatkTheme.bodySmall,
                    color = AppColor.TextHint,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = conversation.time,
                    style = MahalatkTheme.labelSmall,
                    color = AppColor.TextHint,
                )
                if (conversation.unreadCount > 0) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier.size(22.dp).clip(CircleShape)
                            .background(AppColor.Primary),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = conversation.unreadCount.toString(),
                            style = MahalatkTheme.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    }
}
