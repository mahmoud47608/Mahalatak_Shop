package com.mahalatk.features.offers.add.steps

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Summarize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.inputs.DefaultTextField
import com.mahalatk.features.offers.add.AddOfferState
import com.mahalatk.features.offers.add.AddOfferViewModel
import com.mahalatk.features.offers.add.DiscountMode
import com.mahalatk.features.offers.add.OfferScopeType
import com.mahalatk.features.offers.add.OfferType
import com.mahalatk.features.offers.add.components.SectionLabel
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.CornerDimensions
import com.mahalatk.theme.MahalatkTheme
import com.mahalatk.util.DateUtils
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.cancel
import mahalatk.shared.generated.resources.confirm
import mahalatk.shared.generated.resources.currency
import mahalatk.shared.generated.resources.duration_review
import mahalatk.shared.generated.resources.end_date
import mahalatk.shared.generated.resources.offer_step4_subtitle
import mahalatk.shared.generated.resources.offer_summary
import mahalatk.shared.generated.resources.start_date
import mahalatk.shared.generated.resources.summary_buy_x_get_y
import mahalatk.shared.generated.resources.summary_date_range
import mahalatk.shared.generated.resources.summary_discount
import mahalatk.shared.generated.resources.summary_free_shipping
import mahalatk.shared.generated.resources.summary_free_shipping_min
import mahalatk.shared.generated.resources.summary_package
import mahalatk.shared.generated.resources.summary_scope_all
import mahalatk.shared.generated.resources.summary_scope_categories
import mahalatk.shared.generated.resources.summary_scope_products
import mahalatk.shared.generated.resources.summary_your_offer
import org.jetbrains.compose.resources.stringResource

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun Step4DurationReview(state: AddOfferState, viewModel: AddOfferViewModel) {
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SectionLabel(
            text = stringResource(Res.string.duration_review),
            subtitle = stringResource(Res.string.offer_step4_subtitle),
        )

        Spacer(modifier = Modifier.height(4.dp))

        DateField(
            value = state.startDate,
            placeholder = stringResource(Res.string.start_date),
            onClick = { showStartDatePicker = true },
        )

        DateField(
            value = state.endDate,
            placeholder = stringResource(Res.string.end_date),
            onClick = { showEndDatePicker = true },
        )

        Spacer(modifier = Modifier.height(8.dp))

        SummaryCard(state = state)
    }

    if (showStartDatePicker) {
        OfferDatePickerDialog(
            onDateSelected = { viewModel.updateStartDate(it) },
            onDismiss = { showStartDatePicker = false },
        )
    }

    if (showEndDatePicker) {
        OfferDatePickerDialog(
            onDateSelected = { viewModel.updateEndDate(it) },
            onDismiss = { showEndDatePicker = false },
        )
    }
}

@Composable
private fun DateField(
    value: String,
    placeholder: String,
    onClick: () -> Unit,
) {
    DefaultTextField(
        value = value,
        onValueChanged = {},
        placeholderText = placeholder,
        isEnabled = false,
        onClick = onClick,
        leadingIcon = {
            Icon(
                Icons.Rounded.CalendarMonth,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MahalatkTheme.primary,
            )
        },
    )
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
private fun OfferDatePickerDialog(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val datePickerState = rememberDatePickerState()
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let { millis ->
                    onDateSelected(DateUtils.formatDateMillis(millis))
                }
                onDismiss()
            }) { Text(stringResource(Res.string.confirm)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.cancel))
            }
        },
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
private fun SummaryCard(state: AddOfferState) {
    val summaryText = buildSummaryText(state)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(CornerDimensions.lg),
        colors = CardDefaults.cardColors(containerColor = AppColor.PrimaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(AppColor.Primary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.Rounded.Summarize,
                        contentDescription = null,
                        tint = AppColor.OnPrimaryContainer,
                        modifier = Modifier.size(16.dp),
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(Res.string.offer_summary),
                    style = MahalatkTheme.titleSmall,
                    color = AppColor.OnPrimaryContainer,
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = summaryText,
                style = MahalatkTheme.bodyMedium,
                color = AppColor.TextPrimary,
                lineHeight = MahalatkTheme.bodyMedium.lineHeight,
            )
        }
    }
}

@Composable
private fun buildSummaryText(state: AddOfferState): String {
    val type = buildTypeText(state)
    val scope = buildScopeText(state)
    val dates = buildDateText(state)
    return stringResource(Res.string.summary_your_offer, type, scope, dates)
}

@Composable
private fun buildTypeText(state: AddOfferState): String {
    val currencyLabel = stringResource(Res.string.currency)
    return when (state.offerType) {
        OfferType.DISCOUNT -> {
            val v = when (state.discountMode) {
                DiscountMode.PERCENTAGE -> "${state.discountValue}%"
                DiscountMode.FIXED_AMOUNT -> "${state.discountValue} $currencyLabel"
            }
            stringResource(Res.string.summary_discount, v)
        }

        OfferType.BUY_X_GET_Y -> stringResource(
            Res.string.summary_buy_x_get_y,
            state.buyQuantity,
            state.getQuantity,
        )

        OfferType.FREE_SHIPPING -> {
            if (state.freeShippingMinCart.isNotBlank()) {
                stringResource(Res.string.summary_free_shipping_min, state.freeShippingMinCart)
            } else {
                stringResource(Res.string.summary_free_shipping)
            }
        }

        OfferType.PACKAGE -> stringResource(
            Res.string.summary_package,
            state.packageName,
            state.packagePrice,
            state.packageProductIds.size.toString(),
        )

        null -> ""
    }
}

@Composable
private fun buildScopeText(state: AddOfferState): String {
    if (state.offerType == OfferType.PACKAGE) return ""
    return when (state.scopeType) {
        OfferScopeType.ALL_PRODUCTS -> stringResource(Res.string.summary_scope_all)
        OfferScopeType.CATEGORIES -> stringResource(
            Res.string.summary_scope_categories,
            state.selectedCategories.joinToString(", "),
        )

        OfferScopeType.SPECIFIC_PRODUCTS -> stringResource(
            Res.string.summary_scope_products,
            state.selectedProductIds.size.toString(),
        )
    }
}

@Composable
private fun buildDateText(state: AddOfferState): String {
    if (state.startDate.isBlank() || state.endDate.isBlank()) return ""
    return stringResource(Res.string.summary_date_range, state.startDate, state.endDate)
}
