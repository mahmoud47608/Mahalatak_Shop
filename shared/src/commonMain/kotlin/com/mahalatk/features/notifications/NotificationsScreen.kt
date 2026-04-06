package com.mahalatk.features.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.animation.AnimatedListItem
import com.mahalatk.common.component.card.GlassCard
import com.mahalatk.common.component.header.ScreenHeader
import com.mahalatk.common.component.utilis.noRippleClickable
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.CornerDimensions
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.clear_all_notifications
import mahalatk.shared.generated.resources.ic_delete
import mahalatk.shared.generated.resources.ic_notification
import mahalatk.shared.generated.resources.no_notifications
import mahalatk.shared.generated.resources.notifications
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NotificationsScreen(
    onBack: () -> Unit,
    viewModel: NotificationsViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().background(AppColor.ScreenBackground),
    ) {
        ScreenHeader(
            title = stringResource(Res.string.notifications),
            onBackClick = onBack,
            actions = if (state.notifications.isNotEmpty()) {
                {
                    Text(
                        text = stringResource(Res.string.clear_all_notifications),
                        style = MahalatkTheme.labelMedium,
                        color = Color.White.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .background(
                                color = Color.White.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp),
                            )
                            .noRippleClickable { viewModel.clearAll() }
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                    )
                }
            } else null,
        )

        // ── Content ──
        if (state.notifications.isEmpty()) {
            EmptyNotificationsPlaceholder()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item { Spacer(modifier = Modifier.height(12.dp)) }
                itemsIndexed(state.notifications, key = { _, n -> n.id }) { index, notification ->
                    AnimatedListItem(index) {
                        NotificationCard(
                            notification = notification,
                            onDelete = { viewModel.deleteNotification(notification.id) },
                            onClick = { viewModel.markAsRead(notification.id) },
                        )
                    }
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
private fun EmptyNotificationsPlaceholder() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = vectorResource(Res.drawable.ic_notification),
                contentDescription = null,
                tint = AppColor.TextHint.copy(alpha = 0.3f),
                modifier = Modifier.size(72.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(Res.string.no_notifications),
                style = MahalatkTheme.bodyLarge,
                color = AppColor.TextHint,
            )
        }
    }
}

@Composable
private fun NotificationCard(
    notification: NotificationItem,
    onDelete: () -> Unit,
    onClick: () -> Unit,
) {
    GlassCard(
        modifier = Modifier.noRippleClickable { onClick() },
        accentColor = if (!notification.isRead) AppColor.Primary else AppColor.Gray,
        cornerRadius = CornerDimensions.lg,
        contentPadding = 16.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
        ) {
            // Notification icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        brush = Brush.linearGradient(
                            listOf(
                                AppColor.Primary.copy(alpha = 0.06f),
                                AppColor.Primary.copy(alpha = 0.14f),
                            )
                        )
                    )
                    .border(
                        0.5.dp,
                        AppColor.Primary.copy(alpha = 0.1f),
                        RoundedCornerShape(14.dp),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_notification),
                    contentDescription = null,
                    tint = AppColor.Primary,
                    modifier = Modifier.size(22.dp),
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Content
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Unread dot
                    if (!notification.isRead) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(AppColor.Primary),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = notification.title,
                        style = MahalatkTheme.titleSmall,
                        color = AppColor.TextPrimary,
                        fontWeight = if (!notification.isRead) FontWeight.Bold else FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = notification.body,
                    style = MahalatkTheme.bodySmall,
                    color = AppColor.TextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = notification.time,
                    style = MahalatkTheme.labelSmall,
                    color = AppColor.TextHint,
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Delete button
            Icon(
                painter = painterResource(Res.drawable.ic_delete),
                contentDescription = null,
                tint = AppColor.Error.copy(alpha = 0.6f),
                modifier = Modifier
                    .size(20.dp)
                    .noRippleClickable { onDelete() },
            )
        }
    }
}
