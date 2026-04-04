package com.mahalatk.features.ratings

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.StarRate
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahalatk.common.component.animation.AnimatedListItem
import com.mahalatk.common.component.empty.EmptyStatePlaceholder
import com.mahalatk.common.component.header.ScreenHeader
import com.mahalatk.common.component.image.UserAvatar
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.ic_rating
import mahalatk.shared.generated.resources.my_ratings
import mahalatk.shared.generated.resources.no_ratings
import mahalatk.shared.generated.resources.ratings_count
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

private val StarGold = Color(0xFFFFC107)
private val StarEmpty = Color(0xFFE0E0E0)

@Composable
fun MyRatingsScreen(
    viewModel: MyRatingsViewModel = koinViewModel(),
    onBack: () -> Unit = {},
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.ScreenBackground),
    ) {
        ScreenHeader(
            title = stringResource(Res.string.my_ratings),
            onBackClick = onBack,
        )

        if (state.ratings.isEmpty()) {
            EmptyStatePlaceholder(
                icon = Res.drawable.ic_rating,
                message = stringResource(Res.string.no_ratings),
                iconBackgroundColor = StarGold.copy(alpha = 0.1f),
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 4.dp,
                    bottom = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                // ── Summary Card ──
                item {
                    AnimatedListItem(0) {
                        RatingSummaryCard(
                            averageRating = state.averageRating,
                            totalRatings = state.totalRatings,
                        )
                    }
                }

                // ── Rating Cards ──
                itemsIndexed(
                    state.ratings,
                    key = { _, r -> r.id },
                    contentType = { _, _ -> "rating" }) { index, rating ->
                    AnimatedListItem(index + 1) {
                        RatingCard(rating = rating)
                    }
                }
            }
        }
    }
}

// ─── Summary Card ────────────────────────────────────────

@Composable
private fun RatingSummaryCard(
    averageRating: Float,
    totalRatings: Int,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = AppColor.Primary),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Rating number
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = ((averageRating * 10).toInt() / 10f).toString(),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Stars row
                Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    for (i in 1..5) {
                        Icon(
                            Icons.Filled.Star, null,
                            modifier = Modifier.size(16.dp),
                            tint = if (i <= averageRating.toInt()) StarGold
                            else Color.White.copy(alpha = 0.35f),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(20.dp))

            // Divider
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(50.dp)
                    .background(Color.White.copy(alpha = 0.25f)),
            )

            Spacer(modifier = Modifier.width(20.dp))

            // Total count
            Column {
                Text(
                    text = "$totalRatings",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
                Text(
                    text = stringResource(Res.string.ratings_count),
                    style = MahalatkTheme.bodySmall,
                    color = Color.White.copy(alpha = 0.7f),
                )
            }
        }
    }
}

// ─── Rating Card ─────────────────────────────────────────

@Composable
private fun RatingCard(rating: Rating) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppColor.Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Avatar
                UserAvatar(
                    imageUrl = rating.customerImage,
                    initials = rating.customerName,
                    size = 44.dp,
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Name + Date
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = rating.customerName,
                        style = MahalatkTheme.bodyMedium,
                        color = AppColor.TextPrimary,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = rating.date,
                        style = MahalatkTheme.labelSmall,
                        color = AppColor.TextHint,
                        fontSize = 11.sp,
                    )
                }

                // Rating badge
                Box(
                    modifier = Modifier
                        .background(
                            color = StarGold.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(8.dp),
                        )
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Rounded.StarRate, null,
                            modifier = Modifier.size(16.dp),
                            tint = StarGold,
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = ((rating.rating * 10).toInt() / 10f).toString(),
                            style = MahalatkTheme.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF8A6D00),
                            fontSize = 13.sp,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Comment
            Text(
                text = rating.comment,
                style = MahalatkTheme.bodySmall,
                color = AppColor.TextHint,
                lineHeight = 20.sp,
            )
        }
    }
}

