package com.mahalatk.features.profile.shopowner

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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.bottomsheet.MultiSelectBottomSheet
import com.mahalatk.common.component.bottomsheet.SingleSelectBottomSheet
import com.mahalatk.common.component.button.DefaultButton
import com.mahalatk.common.component.header.ScreenHeader
import com.mahalatk.common.component.imagepicker.rememberImagePickerLauncher
import com.mahalatk.common.component.inputs.DefaultTextField
import com.mahalatk.features.auth.register.LocationResultHolder
import com.mahalatk.features.auth.register.ReturnPeriod
import com.mahalatk.features.auth.register.ReturnPolicy
import com.mahalatk.features.auth.register.ShopCategory
import com.mahalatk.features.auth.register.ShopType
import com.mahalatk.features.profile.component.ProfileImagePicker
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.edit_profile
import mahalatk.shared.generated.resources.exchange
import mahalatk.shared.generated.resources.exchange_and_return
import mahalatk.shared.generated.resources.ic_city
import mahalatk.shared.generated.resources.ic_location
import mahalatk.shared.generated.resources.ic_phone
import mahalatk.shared.generated.resources.ic_user
import mahalatk.shared.generated.resources.not_available_policy
import mahalatk.shared.generated.resources.online_shop
import mahalatk.shared.generated.resources.owner_name
import mahalatk.shared.generated.resources.phone_number
import mahalatk.shared.generated.resources.physical_shop
import mahalatk.shared.generated.resources.save
import mahalatk.shared.generated.resources.select_city
import mahalatk.shared.generated.resources.select_location
import mahalatk.shared.generated.resources.select_return_period
import mahalatk.shared.generated.resources.select_return_policy
import mahalatk.shared.generated.resources.select_shop_type
import mahalatk.shared.generated.resources.shop_category
import mahalatk.shared.generated.resources.shop_name
import mahalatk.shared.generated.resources.upload_shop_logo
import mahalatk.shared.generated.resources.within_14_days
import mahalatk.shared.generated.resources.within_2_days
import mahalatk.shared.generated.resources.within_3_days
import mahalatk.shared.generated.resources.within_7_days
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EditShopOwnerProfileScreen(
    viewModel: ShopOwnerProfileViewModel = koinViewModel(),
    onBack: () -> Unit = {},
    onNavigateToPickLocation: () -> Unit = {},
    onChangePhone: () -> Unit = {},
) {
    val state by viewModel.uiState.collectAsState()

    // Bottom sheet states
    var showShopTypeSheet by remember { mutableStateOf(false) }
    var showCitySheet by remember { mutableStateOf(false) }
    var showCategorySheet by remember { mutableStateOf(false) }
    var showReturnPolicySheet by remember { mutableStateOf(false) }
    var showReturnPeriodSheet by remember { mutableStateOf(false) }

    // Image picker
    val pickImage = rememberImagePickerLauncher { bytes ->
        viewModel.updateState { copy(shopImage = bytes, imageError = null) }
    }

    // Location result
    val locationResult by LocationResultHolder.result.collectAsState()
    LaunchedEffect(locationResult) {
        locationResult?.let {
            viewModel.updateLocation(it.lat, it.lng, it.address)
            LocationResultHolder.consume()
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(AppColor.ScreenBackground)) {

        ScreenHeader(
            title = stringResource(Res.string.edit_profile),
            onBackClick = onBack,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp, bottom = 24.dp),
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = AppColor.Surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // Shop Logo (editable)
                    ProfileImagePicker(
                        imageUrl = state.shopImageUrl,
                        imageBytes = state.shopImage,
                        label = stringResource(Res.string.upload_shop_logo),
                        errorText = state.imageError?.let { stringResource(it) },
                        editable = true,
                        onPickImage = pickImage,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Column(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        // 1. Shop Name
                        DefaultTextField(
                            value = state.shopName,
                            onValueChanged = {
                                viewModel.updateState { copy(shopName = it, shopNameError = null) }
                            },
                            placeholderText = stringResource(Res.string.shop_name),
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                            errorText = state.shopNameError?.let { stringResource(it) },
                            leadingIcon = {
                                Icon(Icons.Filled.Storefront, null, tint = MahalatkTheme.primary)
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // 2. Owner Name
                        DefaultTextField(
                            value = state.ownerName,
                            onValueChanged = {
                                viewModel.updateState {
                                    copy(
                                        ownerName = it,
                                        ownerNameError = null
                                    )
                                }
                            },
                            placeholderText = stringResource(Res.string.owner_name),
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                            errorText = state.ownerNameError?.let { stringResource(it) },
                            leadingIcon = {
                                Icon(
                                    painterResource(Res.drawable.ic_user), null,
                                    modifier = Modifier.size(24.dp),
                                    tint = MahalatkTheme.primary,
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // 3. Phone Number (read-only, click to change)
                        DefaultTextField(
                            value = state.phone,
                            onValueChanged = {},
                            placeholderText = stringResource(Res.string.phone_number),
                            isEnabled = false,
                            onClick = onChangePhone,
                            leadingIcon = {
                                Icon(
                                    painterResource(Res.drawable.ic_phone), null,
                                    modifier = Modifier.size(24.dp),
                                    tint = MahalatkTheme.primary,
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    Icons.Outlined.Edit, null,
                                    tint = MahalatkTheme.primary,
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // 4. Shop Type
                        val shopTypeLabel = when (state.shopType) {
                            ShopType.PHYSICAL -> stringResource(Res.string.physical_shop)
                            ShopType.ONLINE -> stringResource(Res.string.online_shop)
                        }
                        DefaultTextField(
                            value = shopTypeLabel,
                            onValueChanged = {},
                            placeholderText = stringResource(Res.string.select_shop_type),
                            isEnabled = false,
                            onClick = { showShopTypeSheet = true },
                            leadingIcon = {
                                Icon(Icons.Filled.Storefront, null, tint = MahalatkTheme.primary)
                            },
                            trailingIcon = {
                                Icon(
                                    Icons.Filled.KeyboardArrowDown,
                                    null,
                                    tint = MahalatkTheme.primary
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )

                        // Fields only for physical shops
                        if (state.shopType == ShopType.PHYSICAL) {

                            Spacer(modifier = Modifier.height(20.dp))

                            // 4. Location
                        DefaultTextField(
                            value = state.locationAddress,
                            onValueChanged = {},
                            placeholderText = stringResource(Res.string.select_location),
                            isEnabled = false,
                            onClick = { onNavigateToPickLocation() },
                            errorText = state.locationError?.let { stringResource(it) },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_location),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = MahalatkTheme.primary,
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // 4. City
                        DefaultTextField(
                            value = state.selectedCity?.name ?: "",
                            onValueChanged = {},
                            placeholderText = stringResource(Res.string.select_city),
                            isEnabled = false,
                            onClick = { showCitySheet = true },
                            errorText = state.cityError?.let { stringResource(it) },
                            leadingIcon = {
                                Icon(
                                    painterResource(Res.drawable.ic_city), null,
                                    modifier = Modifier.size(24.dp),
                                    tint = MahalatkTheme.primary,
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    Icons.Filled.KeyboardArrowDown,
                                    null,
                                    tint = MahalatkTheme.primary
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )

                        } // end if PHYSICAL (location + city)

                        Spacer(modifier = Modifier.height(20.dp))

                        // 6. Shop Category
                        val categoryNames =
                            state.selectedCategories.map { stringResource(it.labelRes) }
                        val categoryLabel = categoryNames.joinToString(", ")
                        DefaultTextField(
                            value = categoryLabel,
                            onValueChanged = {},
                            placeholderText = stringResource(Res.string.shop_category),
                            isEnabled = false,
                            onClick = { showCategorySheet = true },
                            errorText = state.categoryError?.let { stringResource(it) },
                            leadingIcon = {
                                Icon(Icons.Filled.Storefront, null, tint = MahalatkTheme.primary)
                            },
                            trailingIcon = {
                                Icon(
                                    Icons.Filled.KeyboardArrowDown,
                                    null,
                                    tint = MahalatkTheme.primary
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )

                        if (state.shopType == ShopType.PHYSICAL) {
                        Spacer(modifier = Modifier.height(20.dp))

                            // 7. Return Policy
                        val returnPolicyLabel = when (state.returnPolicy) {
                            ReturnPolicy.EXCHANGE -> stringResource(Res.string.exchange)
                            ReturnPolicy.EXCHANGE_AND_RETURN -> stringResource(Res.string.exchange_and_return)
                            ReturnPolicy.NOT_AVAILABLE -> stringResource(Res.string.not_available_policy)
                        }
                        DefaultTextField(
                            value = returnPolicyLabel,
                            onValueChanged = {},
                            placeholderText = stringResource(Res.string.select_return_policy),
                            isEnabled = false,
                            onClick = { showReturnPolicySheet = true },
                            leadingIcon = {
                                Icon(Icons.Filled.Storefront, null, tint = MahalatkTheme.primary)
                            },
                            trailingIcon = {
                                Icon(
                                    Icons.Filled.KeyboardArrowDown,
                                    null,
                                    tint = MahalatkTheme.primary
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )

                            // 8. Return Period (conditional)
                        if (state.returnPolicy != ReturnPolicy.NOT_AVAILABLE) {
                            Spacer(modifier = Modifier.height(20.dp))

                            val returnPeriodLabel = when (state.returnPeriod) {
                                ReturnPeriod.DAYS_2 -> stringResource(Res.string.within_2_days)
                                ReturnPeriod.DAYS_3 -> stringResource(Res.string.within_3_days)
                                ReturnPeriod.DAYS_7 -> stringResource(Res.string.within_7_days)
                                ReturnPeriod.DAYS_14 -> stringResource(Res.string.within_14_days)
                            }
                            DefaultTextField(
                                value = returnPeriodLabel,
                                onValueChanged = {},
                                placeholderText = stringResource(Res.string.select_return_period),
                                isEnabled = false,
                                onClick = { showReturnPeriodSheet = true },
                                leadingIcon = {
                                    Icon(
                                        Icons.Filled.Storefront,
                                        null,
                                        tint = MahalatkTheme.primary
                                    )
                                },
                                trailingIcon = {
                                    Icon(
                                        Icons.Filled.KeyboardArrowDown,
                                        null,
                                        tint = MahalatkTheme.primary
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                        } // end if PHYSICAL (return policy + period)

                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Save button
            DefaultButton(
                text = stringResource(Res.string.save),
                onClick = { viewModel.saveProfile() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
        }
    }

    // ─── Bottom Sheets ─────────────────────────────────────

    SingleSelectBottomSheet(
        showBottomSheet = showShopTypeSheet,
        title = stringResource(Res.string.select_shop_type),
        items = ShopType.entries.toList(),
        selectedItem = state.shopType,
        itemLabel = { type ->
            when (type) {
                ShopType.PHYSICAL -> stringResource(Res.string.physical_shop)
                ShopType.ONLINE -> stringResource(Res.string.online_shop)
            }
        },
        onItemSelected = { viewModel.selectShopType(it) },
        onDismiss = { showShopTypeSheet = false },
    )

    SingleSelectBottomSheet(
        showBottomSheet = showCitySheet,
        title = stringResource(Res.string.select_city),
        items = state.availableCities,
        selectedItem = state.selectedCity,
        itemLabel = { it.name },
        onItemSelected = { viewModel.selectCity(it) },
        onDismiss = { showCitySheet = false },
        isItemSelected = { item, selected -> item.id == selected?.id },
    )

    MultiSelectBottomSheet(
        showBottomSheet = showCategorySheet,
        title = stringResource(Res.string.shop_category),
        items = ShopCategory.entries.toList(),
        selectedItems = state.selectedCategories,
        itemLabel = { stringResource(it.labelRes) },
        onItemToggle = { viewModel.toggleCategory(it) },
        onDismiss = { showCategorySheet = false },
    )

    SingleSelectBottomSheet(
        showBottomSheet = showReturnPolicySheet,
        title = stringResource(Res.string.select_return_policy),
        items = ReturnPolicy.entries.toList(),
        selectedItem = state.returnPolicy,
        itemLabel = { policy ->
            when (policy) {
                ReturnPolicy.EXCHANGE -> stringResource(Res.string.exchange)
                ReturnPolicy.EXCHANGE_AND_RETURN -> stringResource(Res.string.exchange_and_return)
                ReturnPolicy.NOT_AVAILABLE -> stringResource(Res.string.not_available_policy)
            }
        },
        onItemSelected = { viewModel.selectReturnPolicy(it) },
        onDismiss = { showReturnPolicySheet = false },
    )

    SingleSelectBottomSheet(
        showBottomSheet = showReturnPeriodSheet,
        title = stringResource(Res.string.select_return_period),
        items = ReturnPeriod.entries.toList(),
        selectedItem = state.returnPeriod,
        itemLabel = { period ->
            when (period) {
                ReturnPeriod.DAYS_2 -> stringResource(Res.string.within_2_days)
                ReturnPeriod.DAYS_3 -> stringResource(Res.string.within_3_days)
                ReturnPeriod.DAYS_7 -> stringResource(Res.string.within_7_days)
                ReturnPeriod.DAYS_14 -> stringResource(Res.string.within_14_days)
            }
        },
        onItemSelected = { viewModel.selectReturnPeriod(it) },
        onDismiss = { showReturnPeriodSheet = false },
    )
}
