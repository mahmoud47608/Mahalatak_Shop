package com.mahalatk.features.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahalatk.common.component.header.ScreenHeader
import com.mahalatk.common.component.tabs.FilterTabs
import com.mahalatk.common.component.utilis.noRippleClickable
import com.mahalatk.navigation.LocalNavigator
import com.mahalatk.navigation.Route
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.CornerDimensions
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.active_tab
import mahalatk.shared.generated.resources.closed_tab
import mahalatk.shared.generated.resources.ic_nav_chat
import mahalatk.shared.generated.resources.messages
import mahalatk.shared.generated.resources.no_messages
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChatScreen(viewModel: ChatViewModel = koinViewModel()) {
    val state by viewModel.uiState.collectAsState()
    val filteredConversations by remember { derivedStateOf { state.filteredConversations } }
    val navigator = LocalNavigator.current

    Column(
        modifier = Modifier.fillMaxSize().background(AppColor.ScreenBackground),
    ) {
        ScreenHeader(title = stringResource(Res.string.messages))

        Spacer(modifier = Modifier.height(16.dp))

        FilterTabs(
            tabs = listOf(
                ChatTab.Active to stringResource(Res.string.active_tab),
                ChatTab.Closed to stringResource(Res.string.closed_tab),
            ),
            selectedTab = state.selectedTab,
            onTabSelected = viewModel::selectTab,
            modifier = Modifier.padding(horizontal = 20.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (filteredConversations.isEmpty()) {
            EmptyChatsPlaceholder()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(filteredConversations, key = { it.id }) { conversation ->
                    ChatItem(
                        conversation = conversation,
                        onClick = {
                            navigator.push(
                                Route.ChatDetail(conversation.id, conversation.customerName)
                            )
                        },
                    )
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
private fun EmptyChatsPlaceholder() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(Res.drawable.ic_nav_chat),
                contentDescription = null,
                tint = AppColor.TextHint.copy(alpha = 0.4f),
                modifier = Modifier.size(64.dp),
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(Res.string.no_messages),
                style = MahalatkTheme.bodyLarge,
                color = AppColor.TextHint,
            )
        }
    }
}

@Composable
private fun ChatItem(
    conversation: ChatConversation,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth().noRippleClickable { onClick() },
        shape = RoundedCornerShape(CornerDimensions.lg),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Avatar with online indicator
            Box {
                Box(
                    modifier = Modifier.size(50.dp).clip(CircleShape)
                        .background(AppColor.Secondary.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = conversation.customerName.take(1).uppercase(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColor.Primary,
                    )
                }
                if (conversation.isOnline) {
                    Box(
                        modifier = Modifier.size(14.dp).clip(CircleShape)
                            .background(Color.White).padding(2.dp)
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
