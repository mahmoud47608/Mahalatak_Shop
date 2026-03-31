package com.mahalatk.features.complaints

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.mahalatk.common.component.animation.AnimatedListItem
import com.mahalatk.common.component.header.ScreenHeader
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.complaints
import mahalatk.shared.generated.resources.ic_complaint
import mahalatk.shared.generated.resources.no_complaints
import org.jetbrains.compose.resources.painterResource
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
            EmptyComplaintsPlaceholder()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item { Spacer(modifier = Modifier.height(4.dp)) }

                itemsIndexed(state.complaints, key = { _, c -> c.id }) { index, complaint ->
                    AnimatedListItem(index) {
                        ComplaintCard(complaint = complaint)
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

// ─── Complaint Card ──────────────────────────────────────

@Composable
private fun ComplaintCard(complaint: Complaint) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            // ── User info row ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(AppColor.Primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center,
                ) {
                    if (complaint.userImage.isNotEmpty()) {
                        AsyncImage(
                            model = complaint.userImage,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize().clip(CircleShape),
                            contentScale = ContentScale.Crop,
                        )
                    } else {
                        Text(
                            text = complaint.userName.take(1),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = AppColor.Primary,
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Name
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = complaint.userName,
                        style = MahalatkTheme.bodyMedium,
                        color = AppColor.TextPrimary,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = complaint.date,
                        style = MahalatkTheme.labelSmall,
                        color = AppColor.TextHint,
                        fontSize = 11.sp,
                    )
                }

                // Status badge
                Box(
                    modifier = Modifier
                        .background(
                            color = AppColor.Error.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp),
                        )
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_complaint),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = AppColor.Error,
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Description ──
            Text(
                text = complaint.description,
                style = MahalatkTheme.bodySmall,
                color = AppColor.TextSecondary,
                lineHeight = 20.sp,
            )

            // ── Attached images ──
            if (complaint.images.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(0.dp),
                ) {
                    items(complaint.images) { imageUrl ->
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop,
                        )
                    }
                }
            }
        }
    }
}

// ─── Empty State ─────────────────────────────────────────

@Composable
private fun EmptyComplaintsPlaceholder() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(AppColor.Error.copy(alpha = 0.08f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(Res.drawable.ic_complaint),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(Res.string.no_complaints),
                style = MahalatkTheme.bodyLarge,
                color = AppColor.TextHint,
                textAlign = TextAlign.Center,
            )
        }
    }
}
