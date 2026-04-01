package com.mahalatk.features.offers.add.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
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
import androidx.compose.ui.Modifier
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
import mahalatk.shared.generated.resources.duration_review
import mahalatk.shared.generated.resources.end_date
import mahalatk.shared.generated.resources.offer_summary
import mahalatk.shared.generated.resources.start_date
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
        SectionLabel(text = stringResource(Res.string.duration_review))

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

        Text(
            text = stringResource(Res.string.offer_summary),
            style = MahalatkTheme.titleSmall,
            color = AppColor.TextPrimary,
            fontWeight = FontWeight.SemiBold,
        )

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
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(CornerDimensions.lg),
        colors = CardDefaults.cardColors(
            containerColor = AppColor.Primary.copy(alpha = 0.05f),
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Text(
            text = buildSummaryText(state),
            style = MahalatkTheme.bodyMedium,
            color = AppColor.TextPrimary,
            modifier = Modifier.padding(16.dp),
            lineHeight = MahalatkTheme.bodyMedium.lineHeight,
        )
    }
}

private fun buildSummaryText(state: AddOfferState): String {
    val typeDesc = when (state.offerType) {
        OfferType.DISCOUNT -> {
            val value = when (state.discountMode) {
                DiscountMode.PERCENTAGE -> "${state.discountValue}%"
                DiscountMode.FIXED_AMOUNT -> "${state.discountValue} EGP"
            }
            "\u062E\u0635\u0645 $value"
        }

        OfferType.BUY_X_GET_Y ->
            "\u0627\u0634\u062A\u0631\u064A ${state.buyQuantity} \u0648\u0627\u062D\u0635\u0644 \u0639\u0644\u0649 ${state.getQuantity} \u0645\u062C\u0627\u0646\u0627\u064B"

        OfferType.FREE_SHIPPING -> {
            val minCart = if (state.freeShippingMinCart.isNotBlank()) {
                " \u0639\u0646\u062F \u0627\u0644\u0634\u0631\u0627\u0621 \u0628\u0640 ${state.freeShippingMinCart}"
            } else ""
            "\u0634\u062D\u0646 \u0645\u062C\u0627\u0646\u064A$minCart"
        }

        OfferType.PACKAGE ->
            "\u0628\u0627\u0643\u062F\u062C ${state.packageName} \u0628\u0633\u0639\u0631 ${state.packagePrice} (${state.packageProductIds.size} \u0645\u0646\u062A\u062C\u0627\u062A)"

        null -> ""
    }

    val scopeDesc = if (state.offerType == OfferType.PACKAGE) {
        ""
    } else {
        when (state.scopeType) {
            OfferScopeType.ALL_PRODUCTS -> " \u0639\u0644\u0649 \u0643\u0644 \u0627\u0644\u0645\u0646\u062A\u062C\u0627\u062A"
            OfferScopeType.CATEGORIES -> {
                val cats = state.selectedCategories.joinToString(", ")
                " \u0639\u0644\u0649 \u0623\u0642\u0633\u0627\u0645: $cats"
            }

            OfferScopeType.SPECIFIC_PRODUCTS -> {
                val count = state.selectedProductIds.size
                " \u0639\u0644\u0649 $count \u0645\u0646\u062A\u062C\u0627\u062A \u0645\u062E\u062A\u0627\u0631\u0629"
            }
        }
    }

    val dateRange = if (state.startDate.isNotBlank() && state.endDate.isNotBlank()) {
        "\u060C \u0645\u0646 ${state.startDate} \u0625\u0644\u0649 ${state.endDate}"
    } else {
        ""
    }

    return "\u0639\u0631\u0636\u0643: $typeDesc$scopeDesc$dateRange"
}
