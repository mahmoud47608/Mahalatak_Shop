package com.mahalatk.common.component.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.utilis.noRippleClickable
import com.mahalatk.features.auth.register.CityItem
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.select_city
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityBottomSheet(
    showBottomSheet: Boolean,
    cities: List<CityItem>,
    selectedCity: CityItem?,
    onDismiss: () -> Unit,
    onCitySelected: (CityItem) -> Unit,
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(Res.string.select_city),
                style = MahalatkTheme.titleMedium,
                color = MahalatkTheme.black,
                textAlign = TextAlign.Center
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(cities) { city ->
                    val isSelected = selectedCity?.id == city.id
                    val backgroundColor =
                        if (isSelected) MahalatkTheme.primary.copy(alpha = 0.08f)
                        else MahalatkTheme.white
                    val borderColor =
                        if (isSelected) MahalatkTheme.primary else MahalatkTheme.border

                    Text(
                        text = city.name,
                        style = MahalatkTheme.bodyMedium,
                        color = if (isSelected) MahalatkTheme.primary else MahalatkTheme.black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(backgroundColor, RoundedCornerShape(12.dp))
                            .border(1.5.dp, borderColor, RoundedCornerShape(12.dp))
                            .noRippleClickable {
                                onCitySelected(city)
                                onDismiss()
                            }
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}
