package com.mahalatk.features.complaints

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.mahalatk.common.component.animation.AnimatedListItem
import com.mahalatk.common.component.card.GlassCard
import com.mahalatk.common.component.empty.EmptyStatePlaceholder
import com.mahalatk.common.component.header.ScreenHeader
import com.mahalatk.common.component.image.UserAvatar
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.CornerDimensions
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.complaints
import mahalatk.shared.generated.resources.ic_complaint
import mahalatk.shared.generated.resources.no_complaints
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ComplaintsScreen(
    viewModel: ComplaintsViewModel = koinViewModel(),
    onBack: () -> Unit = {},
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.ScreenBackground),
    ) {
        ScreenHeader(
            title = stringResource(Res.string.complaints),
            onBackClick = onBack,
        )

        if (state.complaints.isEmpty()) {
            EmptyStatePlaceholder(
                icon = Res.drawable.ic_complaint,
                message = stringResource(Res.string.no_complaints),
                iconBackgroundColor = AppColor.Error.copy(alpha = 0.08f),
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                itemsIndexed(
                    state.complaints,
                    key = { _, c -> c.id },
                    contentType = { _, _ -> "complaint" }) { index, complaint ->
                    AnimatedListItem(index) {
                        ComplaintCard(complaint = complaint)
                    }
                }
            }
        }
    }
}

// ─── Complaint Card ──────────────────────────────────────

@Composable
private fun ComplaintCard(complaint: Complaint) {
    GlassCard(
        cornerRadius = CornerDimensions.lg,
        contentPadding = 20.dp,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // ── User info row ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Avatar with accent ring
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .border(2.dp, AppColor.Primary.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    UserAvatar(
                        imageUrl = complaint.userImage,
                        initials = complaint.userName,
                        size = 44.dp,
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Name + date
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = complaint.userName,
                        style = MahalatkTheme.bodyMedium,
                        color = AppColor.TextPrimary,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = complaint.date,
                        style = MahalatkTheme.labelSmall,
                        color = AppColor.TextHint,
                        fontSize = 12.sp,
                    )
                }

            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Separator ──
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(
                        color = Color.LightGray.copy(alpha = if (AppColor.isDark) 0.5f else 1f)
                    )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Description ──
            Text(
                text = complaint.description,
                style = MahalatkTheme.bodySmall,
                color = AppColor.TextSecondary,
                lineHeight = 22.sp,
            )

            // ── Attached images ──
            if (complaint.images.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(0.dp),
                ) {
                    itemsIndexed(complaint.images) { _, imageUrl ->
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .size(90.dp)
                                .shadow(4.dp, RoundedCornerShape(14.dp))
                                .clip(RoundedCornerShape(14.dp)),
                            contentScale = ContentScale.Crop,
                        )
                    }
                }
            }
        }
    }
}
