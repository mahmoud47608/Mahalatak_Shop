package com.mahalatk.features.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.mahalatk.common.component.animation.AnimatedListItem
import com.mahalatk.common.component.card.GlassCard
import com.mahalatk.common.component.image.UserAvatar
import com.mahalatk.common.component.utilis.noRippleClickable
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.AppShapes
import com.mahalatk.theme.CornerDimensions
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.available
import mahalatk.shared.generated.resources.cancelled_orders
import mahalatk.shared.generated.resources.completed_orders
import mahalatk.shared.generated.resources.hello
import mahalatk.shared.generated.resources.ic_notification
import mahalatk.shared.generated.resources.ic_orders
import mahalatk.shared.generated.resources.new_orders
import mahalatk.shared.generated.resources.receive_new_orders
import mahalatk.shared.generated.resources.statistics
import mahalatk.shared.generated.resources.today
import mahalatk.shared.generated.resources.unavailable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onOrderClick: (String) -> Unit = {},
) {
    val state by viewModel.uiState.collectAsState()
    val navigator = com.mahalatk.navigation.LocalNavigator.current

    val glassColors = remember(AppColor.isDark) {
        if (AppColor.isDark) listOf(Color(0xFF14444A), Color(0xFF1F6268), Color(0xFF276E74))
        else listOf(Color(0xFF3D9098), Color(0xFF5AA6AC), Color(0xFF6DBABF))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.ScreenBackground),
    ) {
        // ── Top Header with glass morphism ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(AppShapes.Header)
                .drawBehind {
                    drawRect(Brush.verticalGradient(glassColors))
                    drawCircle(
                        Color.White.copy(alpha = 0.07f),
                        80.dp.toPx(),
                        Offset(-20.dp.toPx(), -10.dp.toPx()),
                    )
                    drawCircle(
                        Color.White.copy(alpha = 0.05f),
                        55.dp.toPx(),
                        Offset(size.width + 10.dp.toPx(), size.height * 0.4f),
                    )
                    drawCircle(
                        Color.White.copy(alpha = 0.03f),
                        35.dp.toPx(),
                        Offset(size.width * 0.5f, -15.dp.toPx()),
                    )
                }
                .padding(
                    top = 50.dp,
                    bottom = 24.dp,
                    start = 20.dp,
                    end = 20.dp,
                ),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Profile avatar
                UserAvatar(
                    imageUrl = state.userImage,
                    size = 48.dp,
                    backgroundColor = Color.White.copy(alpha = 0.2f),
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Greeting + Name
                Column(
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = stringResource(Res.string.hello),
                        style = MahalatkTheme.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f),
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = state.userName,
                        style = MahalatkTheme.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Notification bell
                IconButton(
                    onClick = { navigator.push(com.mahalatk.navigation.Route.Notifications) },
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.15f),
                            shape = CircleShape,
                        ),
                ) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_notification),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp),
                    )
                }
            }
        }

        // ── Scrollable Content ──
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            // ── Toggle Cards ──
            item {
                ToggleCard(
                    icon = Res.drawable.ic_orders,
                    title = stringResource(Res.string.receive_new_orders),
                    subtitle = if (state.receiveNewOrders) stringResource(Res.string.available)
                    else stringResource(Res.string.unavailable),
                    isChecked = state.receiveNewOrders,
                    onToggle = viewModel::toggleReceiveOrders,
                )
            }

            // ── Statistics Section ──
            item {
                Text(
                    text = stringResource(Res.string.statistics),
                    style = MahalatkTheme.titleMedium,
                    color = MahalatkTheme.black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        count = state.completedOrders,
                        label = stringResource(Res.string.completed_orders),
                        accentColor = AppColor.Primary,
                        progress = state.completedProgress,
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        count = state.cancelledOrders,
                        label = stringResource(Res.string.cancelled_orders),
                        accentColor = AppColor.Error,
                        progress = state.cancelledProgress,
                    )
                }
            }

            // ── New Orders Section ──
            item {
                Text(
                    text = stringResource(Res.string.new_orders),
                    style = MahalatkTheme.titleMedium,
                    color = MahalatkTheme.black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }

            itemsIndexed(state.newOrders, key = { _, o -> o.id }) { index, order ->
                AnimatedListItem(index) {
                    OrderCard(order = order, onClick = { onOrderClick(order.id) })
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

// ──────────────────────────────────────────────
// Toggle Card Component
// ──────────────────────────────────────────────
@Composable
private fun ToggleCard(
    icon: org.jetbrains.compose.resources.DrawableResource,
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onToggle: (Boolean) -> Unit,
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = CornerDimensions.lg,
        contentPadding = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Icon container
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = AppColor.Primary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = AppColor.Primary,
                    modifier = Modifier.size(22.dp),
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MahalatkTheme.titleSmall,
                    color = AppColor.TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MahalatkTheme.bodySmall,
                    color = if (isChecked) AppColor.Success else AppColor.TextHint,
                )
            }

            Switch(
                checked = isChecked,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = AppColor.Primary,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = AppColor.Gray,
                    uncheckedBorderColor = Color.Transparent,
                    checkedBorderColor = Color.Transparent,
                ),
            )
        }
    }
}

// ──────────────────────────────────────────────
// Stat Card Component with Circular Progress
// ──────────────────────────────────────────────
@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    count: Int,
    label: String,
    accentColor: Color,
    progress: Float,
) {
    val animatedProgress = remember { Animatable(0f) }
    LaunchedEffect(progress) {
        animatedProgress.animateTo(
            targetValue = progress,
            animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
        )
    }
    // derivedStateOf prevents recomposition on every frame — only recomposes when the integer % actually changes
    val percentage by remember { derivedStateOf { (animatedProgress.value * 100).toInt() } }

    GlassCard(
        modifier = modifier,
        cornerRadius = CornerDimensions.lg,
        contentPadding = 0.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Circular Progress with count in center
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(72.dp),
            ) {
                Canvas(modifier = Modifier.size(72.dp)) {
                    // Track (background circle)
                    drawArc(
                        color = accentColor.copy(alpha = 0.15f),
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round),
                    )
                    // Progress arc
                    drawArc(
                        color = accentColor,
                        startAngle = -90f,
                        sweepAngle = 360f * animatedProgress.value,
                        useCenter = false,
                        style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round),
                    )
                }
                Text(
                    text = count.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColor.TextPrimary,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = label,
                style = MahalatkTheme.bodySmall,
                color = AppColor.TextSecondary,
                maxLines = 1,
            )

            Text(
                text = "$percentage%",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = accentColor,
            )
        }
    }
}

// ──────────────────────────────────────────────
// Order Card Component
// ──────────────────────────────────────────────
@Composable
private fun OrderCard(order: OrderItem, onClick: () -> Unit = {}) {
    GlassCard(
        modifier = Modifier.fillMaxWidth().noRippleClickable { onClick() },
        cornerRadius = CornerDimensions.lg,
        contentPadding = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Customer avatar
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(AppColor.Secondary.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center,
            ) {
                if (order.imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = order.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Text(
                        text = order.customerName.take(1).uppercase(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColor.Primary,
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = order.customerName,
                    style = MahalatkTheme.titleSmall,
                    color = AppColor.TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "#${order.orderNumber}",
                    style = MahalatkTheme.bodySmall,
                    color = AppColor.TextHint,
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = order.time,
                    style = MahalatkTheme.bodySmall,
                    color = AppColor.TextSecondary,
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = stringResource(Res.string.today),
                    style = MahalatkTheme.labelSmall,
                    color = AppColor.Primary,
                )
            }
        }
    }
}
