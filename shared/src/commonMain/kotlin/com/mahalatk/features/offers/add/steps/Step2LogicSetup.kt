package com.mahalatk.features.offers.add.steps

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.chips.ChipCloud
import com.mahalatk.common.component.inputs.DefaultTextField
import com.mahalatk.features.offers.add.AddOfferState
import com.mahalatk.features.offers.add.AddOfferViewModel
import com.mahalatk.features.offers.add.DiscountMode
import com.mahalatk.features.offers.add.OfferType
import com.mahalatk.features.offers.add.components.SectionLabel
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.buy_how_many
import mahalatk.shared.generated.resources.discount_fixed
import mahalatk.shared.generated.resources.discount_percentage
import mahalatk.shared.generated.resources.get_how_many_free
import mahalatk.shared.generated.resources.min_cart_value
import mahalatk.shared.generated.resources.package_name
import mahalatk.shared.generated.resources.package_price
import mahalatk.shared.generated.resources.set_offer_logic
import org.jetbrains.compose.resources.stringResource

@Composable
fun Step2LogicSetup(state: AddOfferState, viewModel: AddOfferViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SectionLabel(
            text = stringResource(Res.string.set_offer_logic),
            subtitle = "حدد تفاصيل ومنطق العرض",
        )

        Spacer(modifier = Modifier.height(4.dp))

        AnimatedContent(
            targetState = state.offerType,
            label = "logic_content",
        ) { offerType ->
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                when (offerType) {
                    OfferType.DISCOUNT -> DiscountLogicFields(state, viewModel)
                    OfferType.BUY_X_GET_Y -> BuyXGetYLogicFields(state, viewModel)
                    OfferType.FREE_SHIPPING -> FreeShippingLogicFields(state, viewModel)
                    OfferType.PACKAGE -> PackageLogicFields(state, viewModel)
                    null -> {}
                }
            }
        }
    }
}

@Composable
private fun DiscountLogicFields(state: AddOfferState, viewModel: AddOfferViewModel) {
    ChipCloud(
        items = listOf(DiscountMode.PERCENTAGE, DiscountMode.FIXED_AMOUNT),
        selectedItems = setOf(state.discountMode),
        label = { mode ->
            when (mode) {
                DiscountMode.PERCENTAGE -> stringResource(Res.string.discount_percentage)
                DiscountMode.FIXED_AMOUNT -> stringResource(Res.string.discount_fixed)
            }
        },
        onToggle = { viewModel.updateDiscountMode(it) },
    )

    Spacer(modifier = Modifier.height(8.dp))

    DefaultTextField(
        value = state.discountValue,
        onValueChanged = viewModel::updateDiscountValue,
        placeholderText = if (state.discountMode == DiscountMode.PERCENTAGE) {
            stringResource(Res.string.discount_percentage)
        } else {
            stringResource(Res.string.discount_fixed)
        },
        keyboardType = KeyboardType.Number,
    )

    DefaultTextField(
        value = state.minCartValue,
        onValueChanged = viewModel::updateMinCartValue,
        placeholderText = stringResource(Res.string.min_cart_value),
        keyboardType = KeyboardType.Number,
    )
}

@Composable
private fun BuyXGetYLogicFields(state: AddOfferState, viewModel: AddOfferViewModel) {
    DefaultTextField(
        value = state.buyQuantity,
        onValueChanged = viewModel::updateBuyQuantity,
        placeholderText = stringResource(Res.string.buy_how_many),
        keyboardType = KeyboardType.Number,
    )

    DefaultTextField(
        value = state.getQuantity,
        onValueChanged = viewModel::updateGetQuantity,
        placeholderText = stringResource(Res.string.get_how_many_free),
        keyboardType = KeyboardType.Number,
    )
}

@Composable
private fun FreeShippingLogicFields(state: AddOfferState, viewModel: AddOfferViewModel) {
    DefaultTextField(
        value = state.freeShippingMinCart,
        onValueChanged = viewModel::updateFreeShippingMinCart,
        placeholderText = stringResource(Res.string.min_cart_value),
        keyboardType = KeyboardType.Number,
    )
}

@Composable
private fun PackageLogicFields(state: AddOfferState, viewModel: AddOfferViewModel) {
    DefaultTextField(
        value = state.packageName,
        onValueChanged = viewModel::updatePackageName,
        placeholderText = stringResource(Res.string.package_name),
    )

    DefaultTextField(
        value = state.packagePrice,
        onValueChanged = viewModel::updatePackagePrice,
        placeholderText = stringResource(Res.string.package_price),
        keyboardType = KeyboardType.Number,
    )
}
