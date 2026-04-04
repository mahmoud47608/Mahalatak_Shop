package com.mahalatk.features.offers

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.LocalShipping
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material.icons.rounded.Storefront
import androidx.compose.material.icons.rounded.ViewInAr
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.animation.AnimatedListItem
import com.mahalatk.common.component.empty.EmptyStatePlaceholder
import com.mahalatk.common.component.header.ScreenHeader
import com.mahalatk.common.component.loading.ShimmerBox
import com.mahalatk.common.component.loading.ShimmerCircle
import com.mahalatk.common.component.utilis.noRippleClickable
import com.mahalatk.features.offers.add.Offer
import com.mahalatk.features.offers.add.OfferType
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.CornerDimensions
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.active_status
import mahalatk.shared.generated.resources.ic_check_circle
import mahalatk.shared.generated.resources.inactive_status
import mahalatk.shared.generated.resources.no_offers
import mahalatk.shared.generated.resources.offers
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OffersScreen(
    viewModel: OffersViewModel = koinViewModel(),
    onBack: () -> Unit = {},
    onAddOffer: () -> Unit = {},
) {
    val state by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    val isFabVisible by remember {
        derivedStateOf { listState.firstVisibleItemIndex < 2 }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.ScreenBackground),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            ScreenHeader(
                title = stringResource(Res.string.offers),
                onBackClick = onBack,
            )

            Crossfade(targetState = state.isLoading) { loading ->
                when {
                    loading -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                start = 20.dp, end = 20.dp,
                                top = 16.dp, bottom = 16.dp,
                            ),
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                        ) {
                            items(4) { index ->
                                AnimatedListItem(index) { OfferCardSkeleton() }
                            }
                        }
                    }

                    state.offers.isEmpty() -> {
                        EmptyStatePlaceholder(
                            icon = Res.drawable.ic_check_circle,
                            message = stringResource(Res.string.no_offers),
                            iconTint = AppColor.TextHint.copy(alpha = 0.4f),
                        )
                    }

                    else -> {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                start = 20.dp, end = 20.dp,
                                top = 12.dp, bottom = 80.dp,
                            ),
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                        ) {
                            // ── Offer cards ──
                            itemsIndexed(
                                items = state.offers,
                                key = { _, offer -> offer.id },
                                contentType = { _, _ -> "offer" },
                            ) { index, offer ->
                                AnimatedListItem(index) {
                                    OfferCard(
                                        offer = offer,
                                        onToggleActive = { viewModel.toggleOfferActive(offer.id) },
                                        onDelete = { viewModel.deleteOffer(offer.id) },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // FAB
        AnimatedVisibility(
            visible = !state.isLoading && isFabVisible,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut(),
        ) {
            FloatingActionButton(
                onClick = onAddOffer,
                containerColor = AppColor.Primary,
                contentColor = Color.White,
                shape = CircleShape,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                )
            }
        }
    }
}

// ── Offer Card ──────────────────────────────────────────────────────────────

@Composable
private fun OfferCard(
    offer: Offer,
    onToggleActive: () -> Unit,
    onDelete: () -> Unit,
) {
    val (icon, iconBg) = remember(offer.type, AppColor.isDark) {
        when (offer.type) {
            OfferType.DISCOUNT -> Icons.Rounded.Storefront to AppColor.Primary
            OfferType.BUY_X_GET_Y -> Icons.Rounded.ShoppingCart to AppColor.Success
            OfferType.FREE_SHIPPING -> Icons.Rounded.LocalShipping to AppColor.Info
            OfferType.PACKAGE -> Icons.Rounded.ViewInAr to AppColor.Warning
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(CornerDimensions.lg),
        colors = CardDefaults.cardColors(containerColor = AppColor.Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Icon in colored circle
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(iconBg.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconBg,
                        modifier = Modifier.size(22.dp),
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = offer.description,
                        style = MahalatkTheme.titleSmall,
                        color = AppColor.TextPrimary,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${offer.startDate} - ${offer.endDate}",
                        style = MahalatkTheme.bodySmall,
                        color = AppColor.TextHint,
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Active/Inactive badge
                ActiveBadge(isActive = offer.isActive, onClick = onToggleActive)

                Spacer(modifier = Modifier.width(6.dp))

                // Delete
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(AppColor.Error.copy(alpha = 0.08f))
                        .noRippleClickable { onDelete() },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.DeleteOutline,
                        contentDescription = null,
                        tint = AppColor.Error,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
        }
    }
}

// ── Active Badge ────────────────────────────────────────────────────────────

@Composable
private fun ActiveBadge(isActive: Boolean, onClick: () -> Unit) {
    val bgColor by animateColorAsState(
        targetValue = if (isActive) AppColor.Success.copy(alpha = 0.1f)
        else AppColor.Error.copy(alpha = 0.1f),
        animationSpec = tween(300),
    )
    val textColor by animateColorAsState(
        targetValue = if (isActive) AppColor.Success else AppColor.Error,
        animationSpec = tween(300),
    )
    val label = if (isActive) stringResource(Res.string.active_status)
    else stringResource(Res.string.inactive_status)

    Box(
        modifier = Modifier
            .noRippleClickable { onClick() }
            .background(color = bgColor, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
    ) {
        Text(
            text = label,
            style = MahalatkTheme.labelSmall,
            color = textColor,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

// ── Skeleton ────────────────────────────────────────────────────────────────

@Composable
private fun OfferCardSkeleton() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(CornerDimensions.lg),
        colors = CardDefaults.cardColors(containerColor = AppColor.Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ShimmerCircle(size = 44.dp)
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    ShimmerBox(width = 180.dp, height = 14.dp)
                    Spacer(modifier = Modifier.height(6.dp))
                    ShimmerBox(width = 120.dp, height = 10.dp)
                }
                ShimmerBox(width = 60.dp, height = 24.dp, shape = RoundedCornerShape(8.dp))
            }
        }
    }
}
