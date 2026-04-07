package com.mahalatk.features.packages

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.animation.AnimatedListItem
import com.mahalatk.common.component.card.GlassCard
import com.mahalatk.common.component.empty.EmptyStatePlaceholder
import com.mahalatk.common.component.header.ScreenHeader
import com.mahalatk.common.component.loading.ShimmerBox
import com.mahalatk.common.component.loading.ShimmerCircle
import com.mahalatk.common.component.utilis.noRippleClickable
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.AppShapes
import com.mahalatk.theme.CornerDimensions
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.ic_orders
import mahalatk.shared.generated.resources.my_packages
import mahalatk.shared.generated.resources.no_packages
import mahalatk.shared.generated.resources.sar
import mahalatk.shared.generated.resources.subscription_active
import mahalatk.shared.generated.resources.subscription_available
import mahalatk.shared.generated.resources.subscription_pending
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PackagesScreen(
    viewModel: PackagesViewModel = koinViewModel(),
    onBack: () -> Unit = {},
    onPackageClick: (PackageItem) -> Unit = {},
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().background(AppColor.ScreenBackground),
    ) {
        ScreenHeader(
            title = stringResource(Res.string.my_packages),
            onBackClick = onBack,
        )

        when {
            state.isLoading -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(4) { index ->
                        AnimatedListItem(index) { PackageCardSkeleton() }
                    }
                }
            }

            state.packages.isEmpty() -> {
                EmptyStatePlaceholder(
                    icon = Res.drawable.ic_orders,
                    message = stringResource(Res.string.no_packages),
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    itemsIndexed(
                        state.packages,
                        key = { _, p -> p.id },
                    ) { index, pkg ->
                        AnimatedListItem(index) {
                            PackageCard(
                                item = pkg,
                                onClick = { onPackageClick(pkg) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PackageCard(item: PackageItem, onClick: () -> Unit) {
    val visuals = getPackageVisuals(item.type)

    GlassCard(
        modifier = Modifier.noRippleClickable { onClick() },
        accentColor = visuals.color,
        cornerRadius = CornerDimensions.lg,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        brush = Brush.linearGradient(
                            listOf(
                                visuals.color.copy(alpha = 0.06f),
                                visuals.color.copy(alpha = 0.14f),
                            )
                        )
                    )
                    .border(0.5.dp, visuals.color.copy(alpha = 0.15f), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(visuals.icon),
                    contentDescription = null,
                    tint = visuals.color,
                    modifier = Modifier.size(24.dp),
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Content
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MahalatkTheme.titleSmall,
                    color = AppColor.TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.description,
                    style = MahalatkTheme.bodySmall,
                    color = AppColor.TextHint,
                    maxLines = 1,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${item.price} ${stringResource(Res.string.sar)}",
                    style = MahalatkTheme.labelSmall,
                    color = visuals.color,
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Status badge
            PackageStatusBadge(status = item.status)
        }
    }
}

@Composable
private fun PackageStatusBadge(status: PackageStatus) {
    val (bgColor, textColor, labelRes) = when (status) {
        PackageStatus.AVAILABLE -> Triple(
            AppColor.Primary.copy(alpha = 0.1f),
            AppColor.Primary,
            Res.string.subscription_available,
        )

        PackageStatus.PENDING -> Triple(
            Color(0xFFFFC107).copy(alpha = 0.15f),
            Color(0xFFE6A700),
            Res.string.subscription_pending,
        )

        PackageStatus.SUBSCRIBED -> Triple(
            AppColor.Success.copy(alpha = 0.1f),
            AppColor.Success,
            Res.string.subscription_active,
        )
    }

    Box(
        modifier = Modifier
            .background(color = bgColor, shape = AppShapes.Small)
            .border(0.5.dp, textColor.copy(alpha = 0.15f), AppShapes.Small)
            .padding(horizontal = 10.dp, vertical = 4.dp),
    ) {
        Text(
            text = stringResource(labelRes),
            style = MahalatkTheme.labelSmall,
            color = textColor,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun PackageCardSkeleton() {
    GlassCard(cornerRadius = CornerDimensions.lg) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ShimmerCircle(size = 48.dp)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                ShimmerBox(width = 120.dp, height = 14.dp)
                Spacer(modifier = Modifier.height(8.dp))
                ShimmerBox(width = 180.dp, height = 10.dp)
                Spacer(modifier = Modifier.height(6.dp))
                ShimmerBox(width = 60.dp, height = 10.dp)
            }
            ShimmerBox(width = 50.dp, height = 22.dp, shape = RoundedCornerShape(8.dp))
        }
    }
}
