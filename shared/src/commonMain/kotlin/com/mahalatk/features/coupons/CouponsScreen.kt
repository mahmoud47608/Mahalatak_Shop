package com.mahalatk.features.coupons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahalatk.common.component.animation.AnimatedListItem
import com.mahalatk.common.component.animation.PressableCard
import com.mahalatk.common.component.card.GlassCard
import com.mahalatk.common.component.empty.EmptyStatePlaceholder
import com.mahalatk.common.component.header.ScreenHeader
import com.mahalatk.common.component.loading.ShimmerBox
import com.mahalatk.common.component.utilis.noRippleClickable
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.CornerDimensions
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.active_status
import mahalatk.shared.generated.resources.coupon_min_cart
import mahalatk.shared.generated.resources.coupon_usage
import mahalatk.shared.generated.resources.coupons
import mahalatk.shared.generated.resources.currency
import mahalatk.shared.generated.resources.ic_delivery
import mahalatk.shared.generated.resources.inactive_status
import mahalatk.shared.generated.resources.no_coupons
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CouponsScreen(
    viewModel: CouponsViewModel = koinViewModel(),
    onBack: () -> Unit = {},
    onAddCoupon: () -> Unit = {},
) {
    val state by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val isFabVisible by remember { derivedStateOf { !listState.isScrollInProgress } }

    Box(modifier = Modifier.fillMaxSize().background(AppColor.ScreenBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            ScreenHeader(
                title = stringResource(Res.string.coupons),
                onBackClick = onBack,
            )

            Crossfade(targetState = state.isLoading, animationSpec = tween(300)) { loading ->
                when {
                    loading -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                top = 16.dp,
                                bottom = 16.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            items(3) { index -> AnimatedListItem(index) { CouponCardSkeleton() } }
                        }
                    }

                    state.coupons.isEmpty() -> {
                        EmptyStatePlaceholder(
                            icon = Res.drawable.ic_delivery,
                            message = stringResource(Res.string.no_coupons),
                        )
                    }

                    else -> {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                top = 16.dp,
                                bottom = 80.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            itemsIndexed(
                                state.coupons,
                                key = { _, c -> c.id },
                                contentType = { _, _ -> "coupon" }) { index, coupon ->
                                AnimatedListItem(index) {
                                    PressableCard(onClick = {}) {
                                        CouponCard(
                                            coupon = coupon,
                                            onToggleActive = { viewModel.toggleActive(coupon.id) },
                                        )
                                    }
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
            modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp),
            enter = scaleIn(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)) + fadeIn(),
            exit = scaleOut() + fadeOut(),
        ) {
            FloatingActionButton(
                onClick = onAddCoupon,
                containerColor = AppColor.Primary,
                contentColor = Color.White,
                shape = CircleShape,
            ) {
                Icon(Icons.Rounded.Add, null, modifier = Modifier.size(28.dp))
            }
        }
    }
}

@Composable
private fun CouponCard(coupon: Coupon, onToggleActive: () -> Unit) {
    GlassCard(
        cornerRadius = CornerDimensions.lg,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Coupon code in dashed box
            Box(
                modifier = Modifier.fillMaxWidth().border(
                    width = 1.5.dp,
                    color = AppColor.Primary.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(10.dp),
                ).background(
                    color = AppColor.Primary.copy(alpha = 0.04f),
                    shape = RoundedCornerShape(10.dp),
                ).padding(vertical = 12.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = coupon.code,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColor.Primary,
                    letterSpacing = 3.sp,
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Discount info
            Row(verticalAlignment = Alignment.CenterVertically) {
                val discountText = when (coupon.discountType) {
                    CouponDiscountType.PERCENTAGE -> "${coupon.discountValue}%"
                    CouponDiscountType.FIXED_AMOUNT -> "${coupon.discountValue} ${stringResource(Res.string.currency)}"
                }
                Text(
                    text = discountText,
                    style = MahalatkTheme.titleSmall,
                    color = AppColor.TextPrimary,
                    fontWeight = FontWeight.Bold,
                )

                if (coupon.minCartValue.isNotBlank()) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(Res.string.coupon_min_cart, coupon.minCartValue),
                        style = MahalatkTheme.labelSmall,
                        color = AppColor.TextHint,
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Active badge
                val bgColor by animateColorAsState(
                    targetValue = if (coupon.isActive) AppColor.Success.copy(alpha = 0.1f) else AppColor.Error.copy(
                        alpha = 0.1f
                    ),
                    animationSpec = tween(300),
                )
                val textColor by animateColorAsState(
                    targetValue = if (coupon.isActive) AppColor.Success else AppColor.Error,
                    animationSpec = tween(300),
                )

                Box(
                    modifier = Modifier.noRippleClickable { onToggleActive() }
                        .background(bgColor, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                ) {
                    Text(
                        text = if (coupon.isActive) stringResource(Res.string.active_status) else stringResource(
                            Res.string.inactive_status
                        ),
                        style = MahalatkTheme.labelSmall,
                        color = textColor,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Date + usage
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${coupon.startDate} - ${coupon.endDate}",
                    style = MahalatkTheme.labelSmall,
                    color = AppColor.TextHint,
                )

                Spacer(modifier = Modifier.weight(1f))

                val usageText = if (coupon.maxUses.isNotBlank()) {
                    "${coupon.usedCount}/${coupon.maxUses}"
                } else {
                    "${coupon.usedCount}"
                }
                Text(
                    text = stringResource(Res.string.coupon_usage, usageText),
                    style = MahalatkTheme.labelSmall,
                    color = AppColor.TextHint,
                )
            }
        }
    }
}

@Composable
private fun CouponCardSkeleton() {
    GlassCard(
        cornerRadius = CornerDimensions.lg,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            ShimmerBox(height = 44.dp, shape = RoundedCornerShape(10.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Row {
                ShimmerBox(width = 60.dp, height = 14.dp)
                Spacer(modifier = Modifier.weight(1f))
                ShimmerBox(width = 50.dp, height = 20.dp, shape = RoundedCornerShape(8.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            ShimmerBox(width = 180.dp, height = 10.dp)
        }
    }
}
