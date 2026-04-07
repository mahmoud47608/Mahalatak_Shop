package com.mahalatk.features.packages

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahalatk.common.component.bottomsheet.SuccessBottomSheet
import com.mahalatk.common.component.button.DefaultButton
import com.mahalatk.common.component.card.GlassCard
import com.mahalatk.common.component.header.ScreenHeader
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.CornerDimensions
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.package_details
import mahalatk.shared.generated.resources.package_price_label
import mahalatk.shared.generated.resources.sar
import mahalatk.shared.generated.resources.subscribe_now
import mahalatk.shared.generated.resources.subscribe_success_message
import mahalatk.shared.generated.resources.subscription_active
import mahalatk.shared.generated.resources.subscription_pending
import mahalatk.shared.generated.resources.use_package
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PackageDetailScreen(
    packageItem: PackageItem,
    onBack: () -> Unit,
    onSubscribed: () -> Unit = {},
    onUsePackage: (String) -> Unit = {},
    viewModel: PackageDetailViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    // Load package on first composition
    androidx.compose.runtime.LaunchedEffect(packageItem.id) {
        viewModel.loadPackage(packageItem)
    }

    val pkg = state.packageItem ?: packageItem
    val visuals = getPackageVisuals(pkg.type)

    Column(
        modifier = Modifier.fillMaxSize().background(AppColor.ScreenBackground),
    ) {
        ScreenHeader(
            title = stringResource(Res.string.package_details),
            onBackClick = onBack,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Large icon
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        brush = Brush.linearGradient(
                            listOf(
                                visuals.color.copy(alpha = 0.06f),
                                visuals.color.copy(alpha = 0.14f),
                            )
                        )
                    )
                    .border(1.dp, visuals.color.copy(alpha = 0.2f), RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(visuals.icon),
                    contentDescription = null,
                    tint = visuals.color,
                    modifier = Modifier.size(40.dp),
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = pkg.name,
                style = MahalatkTheme.titleMedium,
                color = AppColor.TextPrimary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Description card
            GlassCard(cornerRadius = CornerDimensions.lg) {
                Text(
                    text = pkg.description,
                    style = MahalatkTheme.bodyMedium,
                    color = AppColor.TextSecondary,
                    lineHeight = 24.sp,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Price card
            GlassCard(
                accentColor = visuals.color,
                cornerRadius = CornerDimensions.lg,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(Res.string.package_price_label),
                        style = MahalatkTheme.titleSmall,
                        color = AppColor.TextPrimary,
                        fontWeight = FontWeight.Medium,
                    )
                    Text(
                        text = "${pkg.price} ${stringResource(Res.string.sar)}",
                        style = MahalatkTheme.titleMedium,
                        color = visuals.color,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Status indicator for non-available
            if (pkg.status != PackageStatus.AVAILABLE) {
                val bgColor: Color
                val textColor: Color
                val labelRes: org.jetbrains.compose.resources.StringResource
                when (pkg.status) {
                    PackageStatus.PENDING -> {
                        bgColor = Color(0xFFFFC107).copy(alpha = 0.1f)
                        textColor = Color(0xFFE6A700)
                        labelRes = Res.string.subscription_pending
                    }

                    PackageStatus.SUBSCRIBED -> {
                        bgColor = AppColor.Success.copy(alpha = 0.1f)
                        textColor = AppColor.Success
                        labelRes = Res.string.subscription_active
                    }

                    else -> return@Column
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(bgColor)
                        .border(0.5.dp, textColor.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(labelRes),
                        style = MahalatkTheme.titleSmall,
                        color = textColor,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Subscribe button
            if (pkg.status == PackageStatus.AVAILABLE) {
                DefaultButton(
                    text = stringResource(Res.string.subscribe_now),
                    onClick = { viewModel.subscribe() },
                    enabled = !state.isSubscribing,
                )
            }

            // Use package button (when subscribed)
            if (pkg.status == PackageStatus.SUBSCRIBED && pkg.type != "most_popular") {
                DefaultButton(
                    text = stringResource(Res.string.use_package),
                    onClick = { onUsePackage(pkg.type) },
                )
            }
        }
    }

    SuccessBottomSheet(
        visible = state.showSubscribeSuccess,
        message = stringResource(Res.string.subscribe_success_message),
        onDismiss = {
            viewModel.dismissSuccess()
            onSubscribed()
        },
    )
}
