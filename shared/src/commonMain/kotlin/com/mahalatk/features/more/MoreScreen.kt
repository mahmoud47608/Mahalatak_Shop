package com.mahalatk.features.more

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.mahalatk.common.component.animation.AnimatedListItem
import com.mahalatk.common.component.utilis.noRippleClickable
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.MahalatkTheme
import com.mahalatk.theme.PaddingDimensions
import com.mahalatk.theme.SpacingDimensions
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.about_app
import mahalatk.shared.generated.resources.complaints
import mahalatk.shared.generated.resources.employees
import mahalatk.shared.generated.resources.ic_about
import mahalatk.shared.generated.resources.ic_complaint
import mahalatk.shared.generated.resources.ic_notification
import mahalatk.shared.generated.resources.ic_privacy
import mahalatk.shared.generated.resources.ic_profile
import mahalatk.shared.generated.resources.ic_rating
import mahalatk.shared.generated.resources.ic_settings
import mahalatk.shared.generated.resources.ic_terms
import mahalatk.shared.generated.resources.more
import mahalatk.shared.generated.resources.my_ratings
import mahalatk.shared.generated.resources.privacy_policy
import mahalatk.shared.generated.resources.profile
import mahalatk.shared.generated.resources.settings
import mahalatk.shared.generated.resources.terms_conditions
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel

private data class MenuItem(
    val icon: DrawableResource,
    val titleRes: org.jetbrains.compose.resources.StringResource,
)

@Composable
fun MoreScreen(viewModel: MoreViewModel = koinViewModel()) {
    val state by viewModel.uiState.collectAsState()
    val navigator = com.mahalatk.navigation.LocalNavigator.current

    androidx.compose.runtime.LaunchedEffect(Unit) {
        viewModel.loggedOut.collect {
            navigator.replaceAll(com.mahalatk.navigation.Route.Login)
        }
    }

    val headerGradient = remember {
        Brush.linearGradient(
            colors = listOf(AppColor.Primary, AppColor.Primary.copy(alpha = 0.8f)),
        )
    }

    val mainMenuItems = remember {
        listOf(
            MenuItem(Res.drawable.ic_profile, Res.string.profile),
            MenuItem(Res.drawable.ic_settings, Res.string.settings),
            MenuItem(Res.drawable.ic_profile, Res.string.employees),
            MenuItem(Res.drawable.ic_rating, Res.string.my_ratings),
            MenuItem(Res.drawable.ic_complaint, Res.string.complaints),
        )
    }

    val infoMenuItems = remember {
        listOf(
            MenuItem(Res.drawable.ic_about, Res.string.about_app),
            MenuItem(Res.drawable.ic_terms, Res.string.terms_conditions),
            MenuItem(Res.drawable.ic_privacy, Res.string.privacy_policy),
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(AppColor.ScreenBackground)) {

        // ── Gradient Header Banner ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(
                    brush = headerGradient,
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
                ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 36.dp, start = PaddingDimensions.high, end = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(Res.string.more),
                    style = MahalatkTheme.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                )
                IconButton(onClick = { navigator.push(com.mahalatk.navigation.Route.Notifications) }) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_notification),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
        }

        // ── Scrollable Content ──
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 85.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // ── Profile Card ──
            item {
                AnimatedListItem(0) {
                    ProfileCard(
                        userName = state.userName,
                        userImage = state.userImage,
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // ── Main Menu Group ──
            item {
                AnimatedListItem(1) {
                    MenuGroup(items = mainMenuItems, onItemClick = { index ->
                        when (index) {
                            // TODO: switch based on user type later
                            0 -> navigator.push(com.mahalatk.navigation.Route.ShopOwnerProfile)
                            1 -> navigator.push(com.mahalatk.navigation.Route.Settings)
                            2 -> navigator.push(com.mahalatk.navigation.Route.Employees)
                        }
                    })
                }
            }

            item { Spacer(modifier = Modifier.height(12.dp)) }

            // ── Info Menu Group ──
            item {
                AnimatedListItem(2) {
                    MenuGroup(items = infoMenuItems, onItemClick = { index ->
                        when (index) {
                            0 -> navigator.push(com.mahalatk.navigation.Route.About)
                            1 -> navigator.push(com.mahalatk.navigation.Route.Terms)
                            2 -> navigator.push(com.mahalatk.navigation.Route.PrivacyPolicy)
                        }
                    })
                }
            }

            item { Spacer(modifier = Modifier.height(12.dp)) }

            // ── Logout ──
            item {
                AnimatedListItem(3) {
                    LogoutButton(onClick = viewModel::logout)
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

// ── Profile Card ──────────────────────────────────────────

@Composable
private fun ProfileCard(userName: String, userImage: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = PaddingDimensions.high),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(AppColor.Primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center,
            ) {
                if (userImage.isNotEmpty()) {
                    AsyncImage(
                        model = userImage,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Image(
                        painter = painterResource(Res.drawable.ic_profile),
                        contentDescription = null,
                        modifier = Modifier.size(36.dp).padding(top = 4.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Name + Rating
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = userName.ifEmpty { "Ahmed Mohamed" },
                    style = MahalatkTheme.titleSmall,
                    color = AppColor.TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(3) {
                        Icon(
                            Icons.Filled.Star, null,
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(14.dp),
                        )
                    }
                    repeat(2) {
                        Icon(
                            Icons.Filled.Star, null,
                            tint = AppColor.TextHint.copy(alpha = 0.3f),
                            modifier = Modifier.size(14.dp),
                        )
                    }
                }
            }

            // Arrow
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight, null,
                tint = AppColor.TextHint,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

// ── Menu Group (single card with dividers) ────────────────

@Composable
private fun MenuGroup(items: List<MenuItem>, onItemClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = PaddingDimensions.high),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column {
            items.forEachIndexed { index, item ->
                MenuRow(
                    icon = item.icon,
                    title = stringResource(item.titleRes),
                    onClick = { onItemClick(index) },
                )
            }
        }
    }
}

@Composable
private fun MenuRow(icon: DrawableResource, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(AppColor.Primary.copy(alpha = 0.08f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painterResource(icon), null,
                tint = AppColor.Primary,
                modifier = Modifier.size(16.dp),
            )
        }
        Spacer(modifier = Modifier.width(SpacingDimensions.sp2))
        Text(
            text = title,
            style = MahalatkTheme.bodyMedium,
            color = AppColor.TextPrimary,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f),
        )
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight, null,
            tint = AppColor.TextHint,
            modifier = Modifier.size(18.dp),
        )
    }
}

// ── Logout Button ─────────────────────────────────────────

@Composable
private fun LogoutButton(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = PaddingDimensions.high)
            .noRippleClickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
        ) {
            Text(
                text = "Logout",
                style = MahalatkTheme.bodyMedium,
                color = AppColor.Error,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
            )
        }
    }
}
