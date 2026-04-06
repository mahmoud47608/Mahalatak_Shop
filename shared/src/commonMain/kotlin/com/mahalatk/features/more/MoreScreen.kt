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
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import mahalatk.shared.generated.resources.section_quick_actions
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
        viewModel.events.collectLatest { event ->
            when (event) {
                MoreEvent.LoggedOut -> navigator.replaceAll(Route.Login)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.ScreenBackground)
            .drawBehind {
                // Floating decorative orbs for depth
                drawCircle(
                    color = AppColor.Primary.copy(alpha = if (AppColor.isDark) 0.04f else 0.06f),
                    radius = 120.dp.toPx(),
                    center = Offset(size.width * 0.85f, size.height * 0.12f),
                )
                drawCircle(
                    color = AppColor.Primary.copy(alpha = if (AppColor.isDark) 0.03f else 0.05f),
                    radius = 90.dp.toPx(),
                    center = Offset(-20.dp.toPx(), size.height * 0.45f),
                )
                drawCircle(
                    color = AppColor.Primary.copy(alpha = if (AppColor.isDark) 0.025f else 0.04f),
                    radius = 70.dp.toPx(),
                    center = Offset(size.width * 0.7f, size.height * 0.82f),
                )
                drawCircle(
                    color = Color(0xFFFFC107).copy(alpha = if (AppColor.isDark) 0.02f else 0.03f),
                    radius = 45.dp.toPx(),
                    center = Offset(size.width * 0.3f, size.height * 0.65f),
                )
            }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Header — enhanced glassmorphism
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .drawBehind {
                    val glassColors = if (AppColor.isDark) listOf(
                        Color(0xFF14444A), Color(0xFF1F6268), Color(0xFF276E74)
                    ) else listOf(Color(0xFF3D9098), Color(0xFF5AA6AC), Color(0xFF6DBABF))
                    drawRect(Brush.verticalGradient(glassColors))
                    // Glass orbs
                    drawCircle(
                        Color.White.copy(alpha = 0.07f),
                        80.dp.toPx(),
                        Offset(-20.dp.toPx(), -10.dp.toPx())
                    )
                    drawCircle(
                        Color.White.copy(alpha = 0.05f),
                        55.dp.toPx(),
                        Offset(size.width + 10.dp.toPx(), size.height * 0.4f)
                    )
                    drawCircle(
                        Color.White.copy(alpha = 0.03f),
                        35.dp.toPx(),
                        Offset(size.width * 0.5f, -15.dp.toPx())
                    )
                    drawCircle(
                        Color.White.copy(alpha = 0.04f),
                        45.dp.toPx(),
                        Offset(size.width * 0.3f, size.height * 0.8f)
                    )
                    drawCircle(
                        Color.White.copy(alpha = 0.06f),
                        25.dp.toPx(),
                        Offset(size.width * 0.75f, size.height * 0.2f)
                    )
                    drawCircle(
                        Color.White.copy(alpha = 0.03f),
                        60.dp.toPx(),
                        Offset(size.width * 0.15f, size.height * 0.6f)
                    )
                    // Subtle white accent line at bottom
                    drawLine(
                        color = Color.White.copy(alpha = 0.12f),
                        start = Offset(size.width * 0.1f, size.height - 1.dp.toPx()),
                        end = Offset(size.width * 0.9f, size.height - 1.dp.toPx()),
                        strokeWidth = 0.5.dp.toPx(),
                    )
                },
            contentAlignment = Alignment.BottomCenter,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 14.dp, start = 20.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(Res.string.more),
                    style = MahalatkTheme.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.weight(1f))
                Box {
                    IconButton(
                        onClick = { navigator.push(Route.Notifications) },
                        modifier = Modifier
                            .size(38.dp)
                            .background(Color.White.copy(alpha = 0.15f), CircleShape),
                    ) {
                        Icon(
                            vectorResource(Res.drawable.ic_notification),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(22.dp),
                        )
                    }
                    Box(
                        Modifier.size(7.dp).align(Alignment.TopEnd).offset((-4).dp, 4.dp)
                            .background(Color(0xFFFF5252), CircleShape)
                            .border(1.dp, Color.White.copy(alpha = 0.8f), CircleShape),
                    )
                }
            }
        }

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item { Spacer(Modifier.height(16.dp)) }

            // User profile card
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

            item { Spacer(Modifier.height(14.dp)) }

            // Quick actions
            item { AnimatedListItem(1) { SectionLabel(stringResource(Res.string.section_quick_actions)) } }
            item {
                AnimatedListItem(2) {
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
            item { AnimatedListItem(3) { SectionLabel(stringResource(Res.string.section_account)) } }
            item {
                AnimatedListItem(4) {
                    MenuCard {
                        MenuRow(
                            Res.drawable.ic_settings,
                            Res.string.settings,
                        ) { navigator.push(Route.Settings) }
                        MenuRow(
                            Res.drawable.ic_profile,
                            Res.string.employees,
                            showTopDivider = true,
                        ) { navigator.push(Route.Employees) }
                    }
                }
            }

            item { Spacer(Modifier.height(14.dp)) }

            // Support section
            item { AnimatedListItem(5) { SectionLabel(stringResource(Res.string.section_support_legal)) } }
            item {
                AnimatedListItem(6) {
                    MenuCard {
                        MenuRow(
                            Res.drawable.ic_phone,
                            Res.string.contact_us,
                        ) { uriHandler.openUri("https://wa.me/2001017156197") }
                        MenuRow(
                            Res.drawable.ic_about,
                            Res.string.about_app,
                            showTopDivider = true,
                        ) { navigator.push(Route.About) }
                        MenuRow(
                            Res.drawable.ic_terms,
                            Res.string.terms_conditions,
                            showTopDivider = true,
                        ) { navigator.push(Route.Terms) }
                        MenuRow(
                            Res.drawable.ic_privacy,
                            Res.string.privacy_policy,
                            showTopDivider = true,
                        ) { navigator.push(Route.PrivacyPolicy) }
                    }
                }
            }

            item { Spacer(Modifier.height(18.dp)) }

            // Logout
            item {
                AnimatedListItem(7) {
                    GlassCard(
                        modifier = Modifier.padding(horizontal = 16.dp)
                            .noRippleClickable { viewModel.logout() },
                        accentColor = AppColor.Error,
                        cornerRadius = 14.dp,
                        contentPadding = 0.dp,
                    ) {
                        Box(Modifier.fillMaxWidth()) {
                            // Red gradient accent strip at top
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .height(2.dp)
                                    .align(Alignment.TopCenter)
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            listOf(
                                                AppColor.Error.copy(alpha = 0.0f),
                                                AppColor.Error.copy(alpha = 0.4f),
                                                AppColor.Error.copy(alpha = 0.0f),
                                            )
                                        )
                                    )
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Box(
                                    Modifier
                                        .size(28.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            brush = Brush.linearGradient(
                                                listOf(
                                                    AppColor.Error.copy(alpha = 0.08f),
                                                    AppColor.Error.copy(alpha = 0.14f),
                                                )
                                            )
                                        ),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.ExitToApp,
                                        null,
                                        tint = AppColor.Error,
                                        modifier = Modifier.size(14.dp),
                                    )
                                }
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    stringResource(Res.string.logout),
                                    style = MahalatkTheme.bodySmall,
                                    color = AppColor.Error,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp,
                                )
                            }
                        }
                    }
                }
            }

            // Footer
            item {
                AnimatedListItem(8) {
                    Column(
                        Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Subtle gradient divider
                        Box(
                            Modifier
                                .width(40.dp)
                                .height(0.5.dp)
                                .background(
                                    brush = Brush.horizontalGradient(
                                        listOf(
                                            AppColor.TextHint.copy(alpha = 0.0f),
                                            AppColor.TextHint.copy(alpha = 0.2f),
                                            AppColor.TextHint.copy(alpha = 0.0f),
                                        )
                                    )
                                )
                        )
                        Spacer(Modifier.height(10.dp))
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
    } // end outer Box
}

// ═══════════════════════════════════════════════════════════════
//  Profile Card
// ═══════════════════════════════════════════════════════════════

@Composable
private fun ProfileCard(
    userName: String,
    userImage: String,
    isShopOwner: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    GlassCard(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .noRippleClickable(onClick = onClick)
            .drawBehind {
                // Diagonal shimmer overlay for multi-layer glass
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.0f),
                            Color.White.copy(alpha = if (AppColor.isDark) 0.02f else 0.04f),
                            Color.White.copy(alpha = 0.0f),
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(size.width, size.height),
                    )
                )
            },
        cornerRadius = 18.dp,
        contentPadding = 14.dp,
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar with gradient border + glow + verified badge
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.drawBehind {
                    // Subtle glow behind avatar
                    drawCircle(
                        color = AppColor.Primary.copy(alpha = if (AppColor.isDark) 0.12f else 0.15f),
                        radius = 38.dp.toPx(),
                        center = center,
                    )
                }
            ) {
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
                // Enhanced verified badge
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.BottomEnd)
                        .offset(1.dp, 1.dp)
                        .shadow(2.dp, CircleShape)
                        .background(AppColor.Surface, CircleShape)
                        .border(
                            width = 1.dp,
                            brush = Brush.linearGradient(
                                listOf(AppColor.Primary, Color(0xFF8ED1D6))
                            ),
                            shape = CircleShape,
                        )
                        .padding(2.dp)
                        .background(AppColor.Primary, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "\u2713",
                        color = Color.White,
                        fontSize = 9.sp,
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
                val badgeBg = if (isShopOwner) AppColor.Primary else Color(0xFFFFC107)
                val badgeText =
                    if (isShopOwner) stringResource(Res.string.shop_owner) else stringResource(Res.string.employee)
                Box(
                    Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(badgeBg.copy(alpha = 0.08f), badgeBg.copy(alpha = 0.14f))
                            )
                        )
                        .border(0.5.dp, badgeBg.copy(alpha = 0.12f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 7.dp, vertical = 2.dp)
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

            // Glass-styled edit button
            Box(
                Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(11.dp))
                    .background(
                        brush = Brush.linearGradient(
                            listOf(
                                AppColor.Primary.copy(alpha = 0.08f),
                                AppColor.Primary.copy(alpha = 0.15f),
                            )
                        )
                    )
                    .border(
                        width = 0.5.dp,
                        brush = Brush.linearGradient(
                            listOf(
                                AppColor.Primary.copy(alpha = 0.25f),
                                AppColor.Primary.copy(alpha = 0.08f),
                            )
                        ),
                        shape = RoundedCornerShape(11.dp),
                    ),
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
    val couponAccent = if (AppColor.isDark) Color(0xFFE5AC00) else Color(0xFFFFC107)
    val ratingAccent = if (AppColor.isDark) Color(0xFFE68A00) else Color(0xFFFF9800)

    Row(
        Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        QuickActionItem(
            Modifier.weight(1f),
            Res.drawable.ic_orders,
            Res.string.offers,
            AppColor.Primary,
            onOffers
        )
        QuickActionItem(
            Modifier.weight(1f),
            Res.drawable.ic_lock,
            Res.string.coupons,
            couponAccent,
            onCoupons
        )
        QuickActionItem(
            Modifier.weight(1f),
            Res.drawable.ic_rating,
            Res.string.my_ratings,
            ratingAccent,
            onRatings
        )
        QuickActionItem(
            Modifier.weight(1f),
            Res.drawable.ic_complaint,
            Res.string.complaints,
            AppColor.Error,
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
        Box(modifier = Modifier.fillMaxWidth()) {
            // Gradient accent strip at top
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .align(Alignment.TopCenter)
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(
                                tint.copy(alpha = 0.0f),
                                tint.copy(alpha = 0.4f),
                                tint.copy(alpha = 0.0f),
                            )
                        )
                    )
            )
            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(11.dp))
                        .background(
                            brush = Brush.linearGradient(
                                listOf(tint.copy(alpha = 0.06f), tint.copy(alpha = 0.14f))
                            )
                        )
                        .border(0.5.dp, tint.copy(alpha = 0.15f), RoundedCornerShape(11.dp)),
                    contentAlignment = Alignment.Center
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
}

// ═══════════════════════════════════════════════════════════════
//  Reusable building blocks
// ═══════════════════════════════════════════════════════════════

@Composable
private fun SectionLabel(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .width(3.dp)
                .height(14.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(
                    brush = Brush.verticalGradient(
                        listOf(AppColor.Primary, AppColor.Primary.copy(alpha = 0.3f))
                    )
                )
        )
        Spacer(Modifier.width(8.dp))
        Text(
            title,
            style = MahalatkTheme.labelMedium,
            color = AppColor.TextSecondary,
            fontWeight = FontWeight.SemiBold,
            fontSize = 11.sp,
            letterSpacing = 0.3.sp,
        )
    }
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
    showTopDivider: Boolean = false,
    onClick: () -> Unit
) {
    Column {
        if (showTopDivider) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
                    .height(0.5.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(
                                AppColor.Primary.copy(alpha = 0.0f),
                                AppColor.Primary.copy(
                                    alpha = if (AppColor.isDark) 0.08f else 0.12f
                                ),
                                AppColor.Primary.copy(alpha = 0.0f),
                            )
                        )
                    )
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().noRippleClickable(onClick = onClick)
                .padding(horizontal = 12.dp, vertical = 11.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        brush = Brush.linearGradient(
                            listOf(tint.copy(alpha = 0.06f), tint.copy(alpha = 0.14f))
                        )
                    )
                    .border(0.5.dp, tint.copy(alpha = 0.1f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(painterResource(icon), null, tint = tint, modifier = Modifier.size(16.dp))
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

