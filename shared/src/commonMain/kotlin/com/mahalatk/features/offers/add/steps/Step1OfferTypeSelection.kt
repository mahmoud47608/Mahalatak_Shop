package com.mahalatk.features.offers.add.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocalShipping
import androidx.compose.material.icons.rounded.Percent
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material.icons.rounded.ViewInAr
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mahalatk.features.offers.add.AddOfferState
import com.mahalatk.features.offers.add.AddOfferViewModel
import com.mahalatk.features.offers.add.OfferType
import com.mahalatk.features.offers.add.components.OfferTypeCard
import com.mahalatk.features.offers.add.components.SectionLabel
import com.mahalatk.theme.AppColor
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.buy_x_get_y_desc
import mahalatk.shared.generated.resources.buy_x_get_y_offer
import mahalatk.shared.generated.resources.discount_offer
import mahalatk.shared.generated.resources.discount_offer_desc
import mahalatk.shared.generated.resources.free_shipping_offer
import mahalatk.shared.generated.resources.free_shipping_offer_desc
import mahalatk.shared.generated.resources.package_offer
import mahalatk.shared.generated.resources.package_offer_desc
import mahalatk.shared.generated.resources.select_offer_type
import org.jetbrains.compose.resources.stringResource

@Composable
fun Step1OfferTypeSelection(state: AddOfferState, viewModel: AddOfferViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SectionLabel(
            text = stringResource(Res.string.select_offer_type),
            subtitle = "اختر نوع العرض المناسب لمنتجاتك",
        )

        Spacer(modifier = Modifier.height(4.dp))

        OfferTypeCard(
            index = 0,
            icon = Icons.Rounded.Percent,
            iconColor = AppColor.Primary,
            title = stringResource(Res.string.discount_offer),
            description = stringResource(Res.string.discount_offer_desc),
            isSelected = state.offerType == OfferType.DISCOUNT,
            onClick = { viewModel.selectOfferType(OfferType.DISCOUNT) },
        )

        OfferTypeCard(
            index = 1,
            icon = Icons.Rounded.ShoppingCart,
            iconColor = AppColor.Success,
            title = stringResource(Res.string.buy_x_get_y_offer),
            description = stringResource(Res.string.buy_x_get_y_desc),
            isSelected = state.offerType == OfferType.BUY_X_GET_Y,
            onClick = { viewModel.selectOfferType(OfferType.BUY_X_GET_Y) },
        )

        OfferTypeCard(
            index = 2,
            icon = Icons.Rounded.LocalShipping,
            iconColor = AppColor.Info,
            title = stringResource(Res.string.free_shipping_offer),
            description = stringResource(Res.string.free_shipping_offer_desc),
            isSelected = state.offerType == OfferType.FREE_SHIPPING,
            onClick = { viewModel.selectOfferType(OfferType.FREE_SHIPPING) },
        )

        OfferTypeCard(
            index = 3,
            icon = Icons.Rounded.ViewInAr,
            iconColor = AppColor.Warning,
            title = stringResource(Res.string.package_offer),
            description = stringResource(Res.string.package_offer_desc),
            isSelected = state.offerType == OfferType.PACKAGE,
            onClick = { viewModel.selectOfferType(OfferType.PACKAGE) },
        )
    }
}
