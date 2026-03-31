package com.mahalatk.features.coupons

import androidx.compose.foundation.background
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.bottomsheet.SuccessBottomSheet
import com.mahalatk.common.component.button.DefaultButton
import com.mahalatk.common.component.chips.ChipCloud
import com.mahalatk.common.component.header.ScreenHeader
import com.mahalatk.common.component.inputs.DefaultTextField
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.CornerDimensions
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.add_coupon
import mahalatk.shared.generated.resources.cancel
import mahalatk.shared.generated.resources.confirm
import mahalatk.shared.generated.resources.coupon_added_success
import mahalatk.shared.generated.resources.coupon_code
import mahalatk.shared.generated.resources.coupon_duration
import mahalatk.shared.generated.resources.discount_fixed
import mahalatk.shared.generated.resources.discount_percentage
import mahalatk.shared.generated.resources.discount_type
import mahalatk.shared.generated.resources.end_date
import mahalatk.shared.generated.resources.max_uses
import mahalatk.shared.generated.resources.min_cart_value
import mahalatk.shared.generated.resources.save
import mahalatk.shared.generated.resources.start_date
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCouponScreen(
    viewModel: AddCouponViewModel = koinViewModel(),
    onBack: () -> Unit = {},
) {
    val state by viewModel.uiState.collectAsState()
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(AppColor.ScreenBackground)) {
        ScreenHeader(
            title = stringResource(Res.string.add_coupon),
            onBackClick = onBack,
        )

        Column(modifier = Modifier.fillMaxSize().padding(top = 16.dp, bottom = 24.dp)) {
            Card(
                modifier = Modifier.fillMaxWidth().weight(1f).padding(horizontal = 16.dp),
                shape = RoundedCornerShape(CornerDimensions.xl),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp),
                ) {
                    DefaultTextField(
                        value = state.code,
                        onValueChanged = viewModel::updateCode,
                        placeholderText = stringResource(Res.string.coupon_code),
                        errorText = state.codeError?.let { stringResource(it) },
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    SectionTitle(text = stringResource(Res.string.discount_type))
                    Spacer(modifier = Modifier.height(8.dp))

                    ChipCloud(
                        items = listOf(
                            CouponDiscountType.PERCENTAGE,
                            CouponDiscountType.FIXED_AMOUNT
                        ),
                        selectedItems = setOf(state.discountType),
                        label = { type ->
                            when (type) {
                                CouponDiscountType.PERCENTAGE -> stringResource(Res.string.discount_percentage)
                                CouponDiscountType.FIXED_AMOUNT -> stringResource(Res.string.discount_fixed)
                            }
                        },
                        onToggle = { viewModel.updateDiscountType(it) },
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DefaultTextField(
                        value = state.discountValue,
                        onValueChanged = viewModel::updateDiscountValue,
                        placeholderText = if (state.discountType == CouponDiscountType.PERCENTAGE) "%" else "EGP",
                        keyboardType = KeyboardType.Number,
                        errorText = state.discountError?.let { stringResource(it) },
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    DefaultTextField(
                        value = state.minCartValue,
                        onValueChanged = viewModel::updateMinCartValue,
                        placeholderText = stringResource(Res.string.min_cart_value),
                        keyboardType = KeyboardType.Number,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    DefaultTextField(
                        value = state.maxUses,
                        onValueChanged = viewModel::updateMaxUses,
                        placeholderText = stringResource(Res.string.max_uses),
                        keyboardType = KeyboardType.Number,
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    SectionTitle(text = stringResource(Res.string.coupon_duration))
                    Spacer(modifier = Modifier.height(8.dp))

                    DefaultTextField(
                        value = state.startDate,
                        onValueChanged = {},
                        placeholderText = stringResource(Res.string.start_date),
                        isEnabled = false,
                        onClick = { showStartDatePicker = true },
                        errorText = state.dateError?.let { stringResource(it) },
                        leadingIcon = {
                            Icon(
                                Icons.Rounded.CalendarMonth,
                                null,
                                modifier = Modifier.size(24.dp),
                                tint = MahalatkTheme.primary
                            )
                        },
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DefaultTextField(
                        value = state.endDate,
                        onValueChanged = {},
                        placeholderText = stringResource(Res.string.end_date),
                        isEnabled = false,
                        onClick = { showEndDatePicker = true },
                        leadingIcon = {
                            Icon(
                                Icons.Rounded.CalendarMonth,
                                null,
                                modifier = Modifier.size(24.dp),
                                tint = MahalatkTheme.primary
                            )
                        },
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            DefaultButton(
                text = stringResource(Res.string.save),
                onClick = viewModel::save,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            )
        }
    }

    // Date pickers
    if (showStartDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        viewModel.updateStartDate(
                            formatDateMillis(it)
                        )
                    }
                    showStartDatePicker = false
                }) { Text(stringResource(Res.string.confirm)) }
            },
            dismissButton = {
                TextButton(onClick = {
                    showStartDatePicker = false
                }) { Text(stringResource(Res.string.cancel)) }
            },
        ) { DatePicker(state = datePickerState) }
    }

    if (showEndDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        viewModel.updateEndDate(
                            formatDateMillis(it)
                        )
                    }
                    showEndDatePicker = false
                }) { Text(stringResource(Res.string.confirm)) }
            },
            dismissButton = {
                TextButton(onClick = {
                    showEndDatePicker = false
                }) { Text(stringResource(Res.string.cancel)) }
            },
        ) { DatePicker(state = datePickerState) }
    }

    // Success
    SuccessBottomSheet(
        message = stringResource(Res.string.coupon_added_success),
        visible = state.showSuccess,
        onDismiss = onBack,
    )
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MahalatkTheme.bodyMedium,
        color = AppColor.TextPrimary,
        fontWeight = FontWeight.SemiBold,
    )
}

private fun formatDateMillis(millis: Long): String {
    val totalDays = (millis / 86400000L).toInt()
    var y = 1970
    var remaining = totalDays
    while (true) {
        val daysInYear = if (y % 4 == 0 && (y % 100 != 0 || y % 400 == 0)) 366 else 365
        if (remaining < daysInYear) break
        remaining -= daysInYear
        y++
    }
    val monthDays = intArrayOf(
        31,
        if (y % 4 == 0 && (y % 100 != 0 || y % 400 == 0)) 29 else 28,
        31,
        30,
        31,
        30,
        31,
        31,
        30,
        31,
        30,
        31
    )
    var m = 0
    while (m < 12 && remaining >= monthDays[m]) {
        remaining -= monthDays[m]
        m++
    }
    val d = remaining + 1
    return "${d.toString().padStart(2, '0')}/${(m + 1).toString().padStart(2, '0')}/$y"
}
