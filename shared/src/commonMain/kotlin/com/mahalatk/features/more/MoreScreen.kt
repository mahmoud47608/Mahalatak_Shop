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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.mahalatk.common.component.utilis.noRippleClickable
import com.mahalatk.theme.MahalatkTheme
import com.mahalatk.theme.PaddingDimensions
import com.mahalatk.theme.SpacingDimensions
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.complaints
import mahalatk.shared.generated.resources.ic_complaint
import mahalatk.shared.generated.resources.ic_notification
import mahalatk.shared.generated.resources.ic_profile
import mahalatk.shared.generated.resources.ic_rating
import mahalatk.shared.generated.resources.ic_settings
import mahalatk.shared.generated.resources.more
import mahalatk.shared.generated.resources.my_ratings
import mahalatk.shared.generated.resources.profile
import mahalatk.shared.generated.resources.settings
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MoreScreen(viewModel: MoreViewModel = koinViewModel()) {
    val state by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(MahalatkTheme.white)) {

        // ── Gradient Header Banner ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MahalatkTheme.primary,
                            MahalatkTheme.primary.copy(alpha = 0.8f),
                        ),
                    ),
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
                ),
        ) {
            // Page title + Notification icon
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp, start = PaddingDimensions.high, end = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(Res.string.more),
                    style = MahalatkTheme.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                )

                IconButton(onClick = { }) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 140.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // ── Floating Profile Card ──
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = PaddingDimensions.high),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MahalatkTheme.white),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // Profile image
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(MahalatkTheme.primary.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (state.userImage.isNotEmpty()) {
                            AsyncImage(
                                model = state.userImage,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop,
                            )
                        } else {
                            Image(
                                painter = painterResource(Res.drawable.ic_profile),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(50.dp)
                                    .padding(top = 6.dp),
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // User name
                    Text(
                        text = state.userName.ifEmpty { "Ahmed Mohamed" },
                        style = MahalatkTheme.titleMedium,
                        color = MahalatkTheme.black,
                        fontWeight = FontWeight.SemiBold,
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Rating - 3 filled stars + 2 empty
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        repeat(3) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFC107),
                                modifier = Modifier.size(18.dp),
                            )
                        }
                        repeat(2) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = null,
                                tint = MahalatkTheme.hint.copy(alpha = 0.3f),
                                modifier = Modifier.size(18.dp),
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Menu Items Card ──
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = PaddingDimensions.high),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MahalatkTheme.white),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Column(
                    modifier = Modifier.padding(vertical = SpacingDimensions.sp1),
                ) {
                    MoreMenuItem(
                        icon = Res.drawable.ic_profile,
                        title = stringResource(Res.string.profile),
                        onClick = { },
                    )

                    MoreMenuItem(
                        icon = Res.drawable.ic_settings,
                        title = stringResource(Res.string.settings),
                        onClick = { },
                    )

                    MoreMenuItem(
                        icon = Res.drawable.ic_rating,
                        title = stringResource(Res.string.my_ratings),
                        onClick = { },
                    )

                    MoreMenuItem(
                        icon = Res.drawable.ic_complaint,
                        title = stringResource(Res.string.complaints),
                        onClick = { },
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun MoreMenuItem(
    icon: DrawableResource,
    title: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable(onClick = onClick)
            .padding(horizontal = PaddingDimensions.extraHigh, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Icon with tinted circle background
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = MahalatkTheme.primary.copy(alpha = 0.1f),
                    shape = CircleShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = MahalatkTheme.primary,
                modifier = Modifier.size(20.dp),
            )
        }

        Spacer(modifier = Modifier.width(SpacingDimensions.sp4))

        Text(
            text = title,
            style = MahalatkTheme.bodyLarge,
            color = MahalatkTheme.black,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f),
        )

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MahalatkTheme.hint,
            modifier = Modifier.size(22.dp),
        )
    }
}
