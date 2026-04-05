package com.mahalatk.features.more

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import com.mahalatk.common.component.animation.AnimatedListItem
import com.mahalatk.common.component.card.GlassCard
import com.mahalatk.common.component.utilis.noRippleClickable
import com.mahalatk.navigation.LocalNavigator
import com.mahalatk.navigation.Route
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.MahalatkTheme
import kotlinx.coroutines.flow.collectLatest
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.about_app
import mahalatk.shared.generated.resources.complaints
import mahalatk.shared.generated.resources.contact_us
import mahalatk.shared.generated.resources.coupons
import mahalatk.shared.generated.resources.employee
import mahalatk.shared.generated.resources.employees
import mahalatk.shared.generated.resources.ic_about
import mahalatk.shared.generated.resources.ic_complaint
import mahalatk.shared.generated.resources.ic_lock
import mahalatk.shared.generated.resources.ic_notification
import mahalatk.shared.generated.resources.ic_orders
import mahalatk.shared.generated.resources.ic_phone
import mahalatk.shared.generated.resources.ic_privacy
import mahalatk.shared.generated.resources.ic_profile
import mahalatk.shared.generated.resources.ic_rating
import mahalatk.shared.generated.resources.ic_settings
import mahalatk.shared.generated.resources.ic_terms
import mahalatk.shared.generated.resources.logout
import mahalatk.shared.generated.resources.more
import mahalatk.shared.generated.resources.my_ratings
import mahalatk.shared.generated.resources.offers
import mahalatk.shared.generated.resources.privacy_policy
import mahalatk.shared.generated.resources.section_account
import mahalatk.shared.generated.resources.section_support_legal
import mahalatk.shared.generated.resources.settings
import mahalatk.shared.generated.resources.shop_owner
import mahalatk.shared.generated.resources.terms_conditions
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel

// ═══════════════════════════════════════════════════════════════
//  MoreScreen
// ═══════════════════════════════════════════════════════════════

@Composable
fun MoreScreen(viewModel: MoreViewModel = koinViewModel()) {
    val state by viewModel.uiState.collectAsState()
    val navigator = LocalNavigator.current
    val uriHandler = LocalUriHandler.current
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.loggedOut.collectLatest { navigator.replaceAll(Route.Login) }
    }

    val collapseProgress by derivedStateOf {
        if (listState.firstVisibleItemIndex > 0) 1f
        else (listState.firstVisibleItemScrollOffset / 250f).coerceIn(0f, 1f)
    }

    Box(modifier = Modifier.fillMaxSize().background(AppColor.ScreenBackground)) {

        // Gradient header background (static — no animation overhead)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp))
                .drawBehind {
                    val colors = if (AppColor.isDark) listOf(
                        Color(0xFF14444A),
                        Color(0xFF1F6268),
                        Color(0xFF276E74)
                    )
                    else listOf(Color(0xFF3D9098), Color(0xFF5AA6AC), Color(0xFF6DBABF))
                    drawRect(Brush.verticalGradient(colors))
                    drawCircle(
                        Color.White.copy(alpha = 0.05f),
                        70.dp.toPx(),
                        Offset(-15.dp.toPx(), -8.dp.toPx())
                    )
                    drawCircle(
                        Color.White.copy(alpha = 0.04f),
                        50.dp.toPx(),
                        Offset(size.width + 8.dp.toPx(), size.height * 0.6f)
                    )
                },
        )

        // Header content (title ↔ compact profile crossfade)
        UnifiedHeader(
            userName = state.userName,
            userImage = state.userImage,
            collapseProgress = collapseProgress,
            onNotificationClick = { navigator.push(Route.Notifications) },
            onProfileClick = { navigator.push(Route.ShopOwnerProfile) },
        )

        // Scrollable content
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize().padding(top = 85.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                AnimatedListItem(0) {
                    ProfileCard(
                        userName = state.userName,
                        userImage = state.userImage,
                        isShopOwner = state.isShopOwner,
                        onClick = { navigator.push(Route.EditShopOwnerProfile) },
                    )
                }
            }

            item { Spacer(Modifier.height(24.dp)) }

            // Quick actions
            item {
                AnimatedListItem(1) {
                    QuickActionsRow(
                        onOffers = { navigator.push(Route.Offers) },
                        onCoupons = { navigator.push(Route.Coupons) },
                        onRatings = { navigator.push(Route.MyRatings) },
                        onComplaints = { navigator.push(Route.Complaints) },
                    )
                }
            }

            item { Spacer(Modifier.height(18.dp)) }

            // Account section
            item { AnimatedListItem(2) { SectionLabel(stringResource(Res.string.section_account)) } }
            item {
                AnimatedListItem(3) {
                    MenuCard {
                        MenuRow(
                            Res.drawable.ic_settings,
                            Res.string.settings,
                            Color(0xFF546E7A)
                        ) { navigator.push(Route.Settings) }
                        MenuRow(
                            Res.drawable.ic_profile,
                            Res.string.employees,
                            Color(0xFF5C6BC0)
                        ) { navigator.push(Route.Employees) }
                    }
                }
            }

            item { Spacer(Modifier.height(14.dp)) }

            // Support section
            item { AnimatedListItem(4) { SectionLabel(stringResource(Res.string.section_support_legal)) } }
            item {
                AnimatedListItem(5) {
                    MenuCard {
                        MenuRow(
                            Res.drawable.ic_phone,
                            Res.string.contact_us,
                            Color(0xFF25D366)
                        ) { uriHandler.openUri("https://wa.me/2001017156197") }
                        MenuRow(
                            Res.drawable.ic_about,
                            Res.string.about_app,
                            Color(0xFF42A5F5)
                        ) { navigator.push(Route.About) }
                        MenuRow(
                            Res.drawable.ic_terms,
                            Res.string.terms_conditions,
                            Color(0xFF7E57C2)
                        ) { navigator.push(Route.Terms) }
                        MenuRow(
                            Res.drawable.ic_privacy,
                            Res.string.privacy_policy,
                            Color(0xFF78909C)
                        ) { navigator.push(Route.PrivacyPolicy) }
                    }
                }
            }

            item { Spacer(Modifier.height(18.dp)) }

            // Logout
            item {
                AnimatedListItem(6) {
                    GlassCard(
                        modifier = Modifier.padding(horizontal = 16.dp)
                            .noRippleClickable { viewModel.logout() },
                        accentColor = AppColor.Error,
                        cornerRadius = 14.dp,
                        contentPadding = 12.dp,
                    ) {
                        Text(
                            stringResource(Res.string.logout),
                            style = MahalatkTheme.bodySmall,
                            color = AppColor.Error,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }

            // Footer
            item {
                AnimatedListItem(7) {
                    Column(
                        Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Mahalatak",
                            style = MahalatkTheme.labelSmall,
                            color = AppColor.TextHint.copy(alpha = 0.45f),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp,
                            letterSpacing = 0.8.sp
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            "v1.0.0",
                            style = MahalatkTheme.labelSmall,
                            color = AppColor.TextHint.copy(alpha = 0.3f),
                            fontSize = 10.sp
                        )
                    }
                }
            }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

// ═══════════════════════════════════════════════════════════════
//  Header (crossfades between title and compact profile)
// ═══════════════════════════════════════════════════════════════

@Composable
private fun UnifiedHeader(
    userName: String,
    userImage: String,
    collapseProgress: Float,
    onNotificationClick: () -> Unit,
    onProfileClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().zIndex(1f)
            .padding(top = 40.dp, start = 20.dp, end = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.weight(1f)) {
            // Title
            Text(
                stringResource(Res.string.more), style = MahalatkTheme.titleLarge,
                color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp,
                modifier = Modifier.graphicsLayer { alpha = 1f - collapseProgress },
            )
            // Compact profile
            Row(
                Modifier.graphicsLayer { alpha = collapseProgress },
                verticalAlignment = Alignment.CenterVertically
            ) {
                SmallAvatar(userImage, 32.dp, onProfileClick)
                Spacer(Modifier.width(8.dp))
                Column {
                    Text(
                        userName.ifEmpty { "Ahmed Mohamed" },
                        style = MahalatkTheme.titleSmall,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(2.dp))
                    StarRow(size = 10.dp, color = Color.White.copy(alpha = 0.25f))
                }
            }
        }

        // Notification bell (single instance, never duplicated)
        Box {
            IconButton(onClick = onNotificationClick) {
                Icon(
                    vectorResource(Res.drawable.ic_notification),
                    null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
            Box(
                Modifier.size(7.dp).align(Alignment.TopEnd).offset((-9).dp, 9.dp)
                    .background(Color(0xFFFF5252), CircleShape)
                    .border(1.dp, Color.White.copy(alpha = 0.8f), CircleShape)
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════════
//  Profile Card
// ═══════════════════════════════════════════════════════════════

@Composable
private fun ProfileCard(
    userName: String,
    userImage: String,
    isShopOwner: Boolean,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier.padding(horizontal = 16.dp).noRippleClickable(onClick = onClick),
        cornerRadius = 20.dp,
        contentPadding = 14.dp,
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar with gradient border + verified badge
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier.size(56.dp)
                        .border(
                            2.dp,
                            Brush.sweepGradient(
                                listOf(
                                    AppColor.Primary,
                                    Color(0xFF6DB8BD),
                                    Color(0xFF8ED1D6),
                                    AppColor.Primary
                                )
                            ),
                            CircleShape
                        )
                        .padding(3.dp).clip(CircleShape).background(AppColor.PrimaryContainer),
                    contentAlignment = Alignment.Center,
                ) {
                    if (userImage.isNotEmpty()) {
                        AsyncImage(
                            userImage,
                            null,
                            Modifier.fillMaxSize().clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painterResource(Res.drawable.ic_profile),
                            null,
                            Modifier.size(30.dp).padding(top = 3.dp)
                        )
                    }
                }
                // Verified badge
                Box(
                    modifier = Modifier.size(18.dp).align(Alignment.BottomEnd).offset(1.dp, 1.dp)
                        .shadow(1.dp, CircleShape).background(AppColor.Surface, CircleShape)
                        .padding(1.5.dp).background(AppColor.Primary, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "\u2713",
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    userName.ifEmpty { "Ahmed Mohamed" },
                    style = MahalatkTheme.titleSmall,
                    color = AppColor.TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(Modifier.height(3.dp))
                // Tier badge
                val badgeBg = if (isShopOwner) AppColor.Primary else Color(0xFF7C4DFF)
                val badgeText =
                    if (isShopOwner) stringResource(Res.string.shop_owner) else stringResource(Res.string.employee)
                Box(
                    Modifier.clip(RoundedCornerShape(5.dp)).background(badgeBg.copy(alpha = 0.1f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        badgeText,
                        style = MahalatkTheme.labelSmall,
                        color = badgeBg,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 9.sp
                    )
                }
                Spacer(Modifier.height(5.dp))
                StarRow(size = 12.dp, color = AppColor.TextHint.copy(alpha = 0.18f))
            }

            // Edit button
            Box(
                Modifier.size(34.dp).clip(RoundedCornerShape(10.dp))
                    .background(AppColor.Primary.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.Edit,
                    null,
                    tint = AppColor.Primary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════
//  Quick Actions
// ═══════════════════════════════════════════════════════════════

@Composable
private fun QuickActionsRow(
    onOffers: () -> Unit,
    onCoupons: () -> Unit,
    onRatings: () -> Unit,
    onComplaints: () -> Unit
) {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        QuickActionItem(
            Modifier.weight(1f),
            Res.drawable.ic_orders,
            Res.string.offers,
            Color(0xFFFF6B6B),
            onOffers
        )
        QuickActionItem(
            Modifier.weight(1f),
            Res.drawable.ic_lock,
            Res.string.coupons,
            Color(0xFF7C4DFF),
            onCoupons
        )
        QuickActionItem(
            Modifier.weight(1f),
            Res.drawable.ic_rating,
            Res.string.my_ratings,
            Color(0xFFFFC107),
            onRatings
        )
        QuickActionItem(
            Modifier.weight(1f),
            Res.drawable.ic_complaint,
            Res.string.complaints,
            Color(0xFFFF7043),
            onComplaints
        )
    }
}

@Composable
private fun QuickActionItem(
    modifier: Modifier,
    icon: DrawableResource,
    label: StringResource,
    tint: Color,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = modifier.noRippleClickable(onClick = onClick),
        accentColor = tint,
        cornerRadius = 14.dp,
        contentPadding = 0.dp,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
        Box(
            Modifier.size(36.dp).clip(RoundedCornerShape(10.dp))
                .background(tint.copy(alpha = 0.08f)), contentAlignment = Alignment.Center
        ) {
            Icon(painterResource(icon), null, tint = tint, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.height(6.dp))
        Text(
            stringResource(label),
            style = MahalatkTheme.labelSmall,
            color = AppColor.TextPrimary,
            fontWeight = FontWeight.Medium,
            fontSize = 10.sp,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
        }
    }
}

// ═══════════════════════════════════════════════════════════════
//  Reusable building blocks
// ═══════════════════════════════════════════════════════════════

@Composable
private fun SectionLabel(title: String) {
    Text(
        title,
        style = MahalatkTheme.labelMedium,
        color = AppColor.TextSecondary,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        letterSpacing = 0.3.sp,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 3.dp)
    )
}

@Composable
private fun MenuCard(content: @Composable () -> Unit) {
    GlassCard(
        modifier = Modifier.padding(horizontal = 16.dp),
        cornerRadius = 16.dp,
        contentPadding = 0.dp,
    ) { Column { content() } }
}

@Composable
private fun MenuRow(
    icon: DrawableResource,
    titleRes: StringResource,
    tint: Color = AppColor.Primary,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().noRippleClickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier.size(32.dp).clip(RoundedCornerShape(10.dp))
                .background(tint.copy(alpha = 0.1f)), contentAlignment = Alignment.Center
        ) {
            Icon(painterResource(icon), null, tint = tint, modifier = Modifier.size(15.dp))
        }
        Spacer(Modifier.width(10.dp))
        Text(
            stringResource(titleRes),
            style = MahalatkTheme.bodySmall,
            color = AppColor.TextPrimary,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            modifier = Modifier.weight(1f)
        )
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight,
            null,
            tint = AppColor.TextHint.copy(alpha = 0.4f),
            modifier = Modifier.size(16.dp)
        )
    }
}

/** Reusable 5-star rating row. */
@Composable
private fun StarRow(size: androidx.compose.ui.unit.Dp, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        repeat(5) { i ->
            Icon(
                Icons.Filled.Star,
                null,
                tint = if (i < 3) Color(0xFFFFC107) else color,
                modifier = Modifier.size(size)
            )
        }
        Spacer(Modifier.width(4.dp))
        Text("3.0", color = AppColor.TextSecondary, fontSize = (size.value - 1).sp)
    }
}

/** Small circular avatar used in the compact header bar. */
@Composable
private fun SmallAvatar(imageUrl: String, size: androidx.compose.ui.unit.Dp, onClick: () -> Unit) {
    Box(
        modifier = Modifier.size(size).clip(CircleShape)
            .border(1.5.dp, Color.White.copy(alpha = 0.5f), CircleShape)
            .noRippleClickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        if (imageUrl.isNotEmpty()) {
            AsyncImage(
                imageUrl,
                null,
                Modifier.fillMaxSize().clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                Modifier.fillMaxSize().background(AppColor.PrimaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painterResource(Res.drawable.ic_profile),
                    null,
                    Modifier.size(size * 0.55f).padding(top = 1.dp)
                )
            }
        }
    }
}
