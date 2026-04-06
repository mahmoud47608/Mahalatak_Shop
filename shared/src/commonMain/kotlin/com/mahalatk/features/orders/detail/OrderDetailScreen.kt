package com.mahalatk.features.orders.detail

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahalatk.common.component.animation.AnimatedListItem
import com.mahalatk.common.component.button.DefaultButton
import com.mahalatk.common.component.card.GlassCard
import com.mahalatk.common.component.header.ScreenHeader
import com.mahalatk.common.component.loading.LoadingOverlay
import com.mahalatk.common.component.utilis.noRippleClickable
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.CornerDimensions
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.coupon_code
import mahalatk.shared.generated.resources.customer_rating
import mahalatk.shared.generated.resources.delivery_address
import mahalatk.shared.generated.resources.delivery_fee
import mahalatk.shared.generated.resources.delivery_info
import mahalatk.shared.generated.resources.driver_name
import mahalatk.shared.generated.resources.driver_phone
import mahalatk.shared.generated.resources.driver_rating
import mahalatk.shared.generated.resources.mark_delivered_driver
import mahalatk.shared.generated.resources.mark_prepared
import mahalatk.shared.generated.resources.normal_order
import mahalatk.shared.generated.resources.order_details
import mahalatk.shared.generated.resources.order_info
import mahalatk.shared.generated.resources.order_type
import mahalatk.shared.generated.resources.payment_info
import mahalatk.shared.generated.resources.payment_method
import mahalatk.shared.generated.resources.products_section
import mahalatk.shared.generated.resources.products_total
import mahalatk.shared.generated.resources.recommend_dealing
import mahalatk.shared.generated.resources.sar
import mahalatk.shared.generated.resources.submit_rating
import mahalatk.shared.generated.resources.total_amount
import mahalatk.shared.generated.resources.vat_amount
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OrderDetailScreen(
    orderId: String,
    onBack: () -> Unit,
    viewModel: OrderDetailViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(orderId) { viewModel.loadOrder(orderId) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColor.ScreenBackground),
        ) {
            ScreenHeader(
                title = stringResource(Res.string.order_details),
                onBackClick = onBack,
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                // ── Customer Info ──
                item { Spacer(modifier = Modifier.height(4.dp)) }
                item {
                    AnimatedListItem(0) {
                        CustomerInfoCard(state)
                    }
                }

                // ── Status Stepper ──
                item {
                    AnimatedListItem(1) {
                        StatusStepperCard(currentStep = state.currentStep)
                    }
                }

                // ── Products ──
                item {
                    AnimatedListItem(2) {
                        SectionTitle(stringResource(Res.string.products_section))
                    }
                }

                itemsIndexed(state.products) { index, product ->
                    AnimatedListItem(3 + index) {
                        ProductCard(product)
                    }
                }

                // ── Order Info ──
                item {
                    AnimatedListItem(5) {
                        OrderInfoCard(state)
                    }
                }

                // ── Payment Info ──
                item {
                    AnimatedListItem(6) {
                        PaymentInfoCard(state)
                    }
                }

                // ── Delivery Info (if past preparing) ──
                if (state.currentStep.index >= DetailOrderStep.DeliveredToDriver.index) {
                    item {
                        AnimatedListItem(7) {
                            DeliveryInfoCard(state)
                        }
                    }
                }

                // ── Rating (if completed) ──
                if (state.currentStep == DetailOrderStep.Completed) {
                    item {
                        AnimatedListItem(8) {
                            RatingCard(
                                customerRating = state.customerRating,
                                driverRating = state.driverRating,
                                onCustomerRate = viewModel::rateCustomer,
                                onDriverRate = viewModel::rateDriver,
                            )
                        }
                    }
                }

                // ── Action Button ──
                val actionText = when (state.currentStep) {
                    DetailOrderStep.Preparing -> Res.string.mark_prepared
                    DetailOrderStep.Ready -> Res.string.mark_delivered_driver
                    DetailOrderStep.Completed -> Res.string.submit_rating
                    else -> null
                }
                if (actionText != null) {
                    item {
                        AnimatedListItem(9) {
                            DefaultButton(
                                text = stringResource(actionText),
                                onClick = viewModel::onActionClick,
                                modifier = Modifier.padding(vertical = 8.dp),
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }

        if (state.isLoading) {
            LoadingOverlay()
        }
    }
}

// ── Customer Info ──────────────────────────────────────────

@Composable
private fun CustomerInfoCard(state: OrderDetailState) {
    DetailCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(AppColor.Secondary.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = state.customerName.take(1).uppercase(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColor.Primary,
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = state.customerName,
                    style = MahalatkTheme.titleSmall,
                    color = AppColor.TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = "#${state.orderNumber}",
                    style = MahalatkTheme.bodySmall,
                    color = AppColor.TextHint,
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = state.customerPhone,
                style = MahalatkTheme.bodySmall,
                color = AppColor.Primary,
            )
            Text(
                text = "${state.date}, ${state.time}",
                style = MahalatkTheme.bodySmall,
                color = AppColor.TextHint,
            )
        }
    }
}

// ── Status Stepper ────────────────────────────────────────

@Composable
private fun StatusStepperCard(currentStep: DetailOrderStep) {
    val steps = listOf(
        DetailOrderStep.WaitingPayment,
        DetailOrderStep.Preparing,
        DetailOrderStep.Ready,
        DetailOrderStep.DeliveredToDriver,
    )

    val statusText = when (currentStep) {
        DetailOrderStep.WaitingPayment -> "Waiting for customer payment"
        DetailOrderStep.Preparing -> "Preparing"
        DetailOrderStep.Ready -> "Ready for delivery"
        DetailOrderStep.DeliveredToDriver -> "Delivered to driver"
        DetailOrderStep.Completed -> "Completed"
    }

    val statusDesc = when (currentStep) {
        DetailOrderStep.WaitingPayment -> "The order is waiting for the customer to complete payment"
        DetailOrderStep.Preparing -> "The order is being prepared and will be ready soon"
        DetailOrderStep.Ready -> "The order is ready and waiting for the driver"
        DetailOrderStep.DeliveredToDriver -> "The order has been handed to the driver"
        DetailOrderStep.Completed -> "The order has been delivered successfully"
    }

    DetailCard {
        // Stepper row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            steps.forEachIndexed { index, step ->
                val isCompleted = currentStep.index > step.index
                val isActive = currentStep.index == step.index

                // Step circle
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            color = when {
                                isCompleted -> AppColor.Primary
                                isActive -> AppColor.Primary
                                else -> AppColor.Outline
                            },
                            shape = CircleShape,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "${index + 1}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isCompleted || isActive) Color.White else AppColor.TextHint,
                    )
                }

                // Connecting line (not after last)
                if (index < steps.lastIndex) {
                    val lineColor =
                        if (currentStep.index > step.index) AppColor.Primary else AppColor.Outline
                    Canvas(
                        modifier = Modifier
                            .weight(1f)
                            .height(2.dp)
                            .padding(horizontal = 4.dp),
                    ) {
                        drawLine(
                            color = lineColor,
                            start = Offset(0f, size.height / 2),
                            end = Offset(size.width, size.height / 2),
                            strokeWidth = 2.dp.toPx(),
                            pathEffect = if (currentStep.index <= step.index)
                                PathEffect.dashPathEffect(floatArrayOf(8f, 6f)) else null,
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Status text
        Text(
            text = statusText,
            style = MahalatkTheme.titleSmall,
            color = AppColor.Primary,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = statusDesc,
            style = MahalatkTheme.bodySmall,
            color = AppColor.TextSecondary,
        )
    }
}

// ── Product Card ──────────────────────────────────────────

@Composable
private fun ProductCard(product: OrderProduct) {
    DetailCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Product image placeholder
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(AppColor.PrimaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Text("🌹", fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    style = MahalatkTheme.titleSmall,
                    color = AppColor.TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = product.description,
                    style = MahalatkTheme.bodySmall,
                    color = AppColor.TextHint,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${product.price.toInt()} ${stringResource(Res.string.sar)}",
                    style = MahalatkTheme.titleSmall,
                    color = AppColor.Primary,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

// ── Order Info ────────────────────────────────────────────

@Composable
private fun OrderInfoCard(state: OrderDetailState) {
    DetailCard {
        SectionTitle(stringResource(Res.string.order_info))
        Spacer(modifier = Modifier.height(10.dp))
        InfoRow(stringResource(Res.string.delivery_address), state.deliveryAddress)
        InfoRow(stringResource(Res.string.payment_method), state.paymentMethod)
        InfoRow(stringResource(Res.string.coupon_code), state.couponCode)
        InfoRow(stringResource(Res.string.order_type), stringResource(Res.string.normal_order))
    }
}

// ── Payment Info ──────────────────────────────────────────

@Composable
private fun PaymentInfoCard(state: OrderDetailState) {
    DetailCard {
        SectionTitle(stringResource(Res.string.payment_info))
        Spacer(modifier = Modifier.height(10.dp))
        InfoRow(
            stringResource(Res.string.products_total),
            "${state.productsTotal.toInt()} ${stringResource(Res.string.sar)}"
        )
        InfoRow(
            stringResource(Res.string.delivery_fee),
            "${state.deliveryFee.toInt()} ${stringResource(Res.string.sar)}"
        )
        InfoRow(
            stringResource(Res.string.vat_amount),
            "${state.vatAmount.toInt()} ${stringResource(Res.string.sar)}"
        )

        Spacer(modifier = Modifier.height(6.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            AppColor.Primary.copy(alpha = 0.0f),
                            AppColor.Primary.copy(alpha = if (AppColor.isDark) 0.08f else 0.12f),
                            AppColor.Primary.copy(alpha = 0.0f),
                        )
                    )
                )
        )
        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(Res.string.total_amount),
                style = MahalatkTheme.titleSmall,
                color = AppColor.TextPrimary,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "${state.totalAmount.toInt()} ${stringResource(Res.string.sar)}",
                style = MahalatkTheme.titleSmall,
                color = AppColor.Primary,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

// ── Delivery Info ─────────────────────────────────────────

@Composable
private fun DeliveryInfoCard(state: OrderDetailState) {
    DetailCard {
        SectionTitle(stringResource(Res.string.delivery_info))
        Spacer(modifier = Modifier.height(10.dp))
        InfoRow(stringResource(Res.string.driver_name), state.driverName)
        InfoRow(stringResource(Res.string.driver_phone), state.driverPhone)
    }
}

// ── Rating Card ───────────────────────────────────────────

@Composable
private fun RatingCard(
    customerRating: Int,
    driverRating: Int,
    onCustomerRate: (Int) -> Unit,
    onDriverRate: (Int) -> Unit,
) {
    DetailCard {
        // Customer rating
        Text(
            text = stringResource(Res.string.customer_rating),
            style = MahalatkTheme.titleSmall,
            color = AppColor.TextPrimary,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.height(8.dp))
        StarRatingRow(rating = customerRating, onRate = onCustomerRate)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(Res.string.recommend_dealing),
            style = MahalatkTheme.bodySmall,
            color = AppColor.TextHint,
        )

        Spacer(modifier = Modifier.height(16.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            AppColor.Primary.copy(alpha = 0.0f),
                            AppColor.Primary.copy(alpha = if (AppColor.isDark) 0.08f else 0.12f),
                            AppColor.Primary.copy(alpha = 0.0f),
                        )
                    )
                )
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Driver rating
        Text(
            text = stringResource(Res.string.driver_rating),
            style = MahalatkTheme.titleSmall,
            color = AppColor.TextPrimary,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.height(8.dp))
        StarRatingRow(rating = driverRating, onRate = onDriverRate)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(Res.string.recommend_dealing),
            style = MahalatkTheme.bodySmall,
            color = AppColor.TextHint,
        )
    }
}

@Composable
private fun StarRatingRow(rating: Int, onRate: (Int) -> Unit) {
    Row {
        repeat(5) { index ->
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = if (index < rating) Color(0xFFFFC107) else AppColor.Outline,
                modifier = Modifier
                    .size(28.dp)
                    .noRippleClickable { onRate(index + 1) },
            )
            if (index < 4) Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

// ── Shared components ─────────────────────────────────────

@Composable
private fun DetailCard(content: @Composable () -> Unit) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = CornerDimensions.lg,
    ) {
        Column {
            content()
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MahalatkTheme.titleSmall,
        color = AppColor.TextPrimary,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MahalatkTheme.bodySmall,
            color = AppColor.TextHint,
        )
        Text(
            text = value,
            style = MahalatkTheme.bodySmall,
            color = AppColor.TextPrimary,
            fontWeight = FontWeight.Medium,
        )
    }
}
