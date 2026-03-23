package com.mahalatk.common.component.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mahalatk.features.auth.register.DeliveryType
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.app_delivery
import mahalatk.shared.generated.resources.select_delivery_type
import mahalatk.shared.generated.resources.shop_delivery
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryTypeBottomSheet(
    showBottomSheet: Boolean,
    selectedType: DeliveryType?,
    onDismiss: () -> Unit,
    onTypeSelected: (DeliveryType) -> Unit,
) {
    if (!showBottomSheet) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MahalatkTheme.white,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(Res.string.select_delivery_type),
                style = MahalatkTheme.titleMedium,
                color = MahalatkTheme.black,
                textAlign = TextAlign.Center
            )

            DeliveryOptionCard(
                label = stringResource(Res.string.shop_delivery),
                isSelected = selectedType == DeliveryType.SHOP_DELIVERY,
                icon = {
                    Icon(
                        Icons.Filled.LocalShipping,
                        null,
                        Modifier.size(28.dp),
                        tint = if (selectedType == DeliveryType.SHOP_DELIVERY) MahalatkTheme.primary else MahalatkTheme.hint
                    )
                },
                onClick = {
                    onTypeSelected(DeliveryType.SHOP_DELIVERY)
                    onDismiss()
                }
            )

            DeliveryOptionCard(
                label = stringResource(Res.string.app_delivery),
                isSelected = selectedType == DeliveryType.APP_DELIVERY,
                icon = {
                    Icon(
                        Icons.Filled.DirectionsCar,
                        null,
                        Modifier.size(28.dp),
                        tint = if (selectedType == DeliveryType.APP_DELIVERY) MahalatkTheme.primary else MahalatkTheme.hint
                    )
                },
                onClick = {
                    onTypeSelected(DeliveryType.APP_DELIVERY)
                    onDismiss()
                }
            )
        }
    }
}

@Composable
private fun DeliveryOptionCard(
    label: String,
    isSelected: Boolean,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    val backgroundColor =
        if (isSelected) MahalatkTheme.primary.copy(alpha = 0.08f) else MahalatkTheme.white
    val borderColor = if (isSelected) MahalatkTheme.primary else MahalatkTheme.border

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, RoundedCornerShape(12.dp))
            .border(1.5.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        icon()
        Text(
            text = label,
            style = MahalatkTheme.bodyMedium,
            color = MahalatkTheme.black,
        )
    }
}
