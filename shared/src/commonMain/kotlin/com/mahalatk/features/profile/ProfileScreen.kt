package com.mahalatk.features.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
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
import com.mahalatk.common.component.card.GlassCard
import com.mahalatk.common.component.header.ScreenHeader
import com.mahalatk.common.component.imagepicker.rememberImagePickerLauncher
import com.mahalatk.common.component.inputs.DefaultTextField
import com.mahalatk.features.auth.register.LocationResultHolder
import com.mahalatk.features.auth.register.ReturnPeriod
import com.mahalatk.features.auth.register.ReturnPolicy
import com.mahalatk.features.auth.register.ShopCategory
import com.mahalatk.features.auth.register.ShopType
import com.mahalatk.features.profile.component.ProfileImagePicker
import com.mahalatk.features.profile.employee.EmployeeProfileViewModel
import com.mahalatk.features.profile.shopowner.ShopOwnerProfileViewModel
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.employee
import mahalatk.shared.generated.resources.employee_name
import mahalatk.shared.generated.resources.exchange
import mahalatk.shared.generated.resources.exchange_and_return
import mahalatk.shared.generated.resources.ic_city
import mahalatk.shared.generated.resources.ic_location
import mahalatk.shared.generated.resources.ic_user
import mahalatk.shared.generated.resources.not_available_policy
import mahalatk.shared.generated.resources.online_shop
import mahalatk.shared.generated.resources.owner_name
import mahalatk.shared.generated.resources.physical_shop
import mahalatk.shared.generated.resources.profile
import mahalatk.shared.generated.resources.save
import mahalatk.shared.generated.resources.select_city
import mahalatk.shared.generated.resources.select_location
import mahalatk.shared.generated.resources.select_return_period
import mahalatk.shared.generated.resources.select_return_policy
import mahalatk.shared.generated.resources.select_shop
import mahalatk.shared.generated.resources.select_shop_type
import mahalatk.shared.generated.resources.shop_category
import mahalatk.shared.generated.resources.shop_name
import mahalatk.shared.generated.resources.shop_owner
import mahalatk.shared.generated.resources.upload_personal_photo
import mahalatk.shared.generated.resources.upload_shop_logo
import mahalatk.shared.generated.resources.within_14_days
import mahalatk.shared.generated.resources.within_2_days
import mahalatk.shared.generated.resources.within_3_days
import mahalatk.shared.generated.resources.within_7_days
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    shopOwnerViewModel: ShopOwnerProfileViewModel = koinViewModel(),
    employeeViewModel: EmployeeProfileViewModel = koinViewModel(),
    onBack: () -> Unit = {},
    onNavigateToPickLocation: () -> Unit = {},
) {
    val shopOwnerState by shopOwnerViewModel.uiState.collectAsState()
    val employeeState by employeeViewModel.uiState.collectAsState()

    var selectedTabIndex by remember { mutableStateOf(0) } // 0 = Shop Owner (default)

    // Bottom sheet states
    var showShopTypeSheet by remember { mutableStateOf(false) }
    var showCitySheet by remember { mutableStateOf(false) }
    var showCategorySheet by remember { mutableStateOf(false) }
    var showReturnPolicySheet by remember { mutableStateOf(false) }
    var showReturnPeriodSheet by remember { mutableStateOf(false) }
    var showShopSheet by remember { mutableStateOf(false) }

    // Image pickers
    val shopPickImage = rememberImagePickerLauncher { bytes ->
        shopOwnerViewModel.updateState { copy(shopImage = bytes, imageError = null) }
    }
    val employeePickImage = rememberImagePickerLauncher { bytes ->
        employeeViewModel.updateState { copy(employeeImage = bytes, imageError = null) }
    }

    // Location result
    val locationResult by LocationResultHolder.result.collectAsState()
    LaunchedEffect(locationResult) {
        locationResult?.let {
            shopOwnerViewModel.updateLocation(it.lat, it.lng, it.address)
            LocationResultHolder.consume()
        }
    }

    val tabs = listOf(
        stringResource(Res.string.shop_owner),
        stringResource(Res.string.employee),
    )

    Column(modifier = Modifier.fillMaxSize().background(AppColor.ScreenBackground)) {

        ScreenHeader(
            title = stringResource(Res.string.profile),
            onBackClick = onBack,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp, bottom = 24.dp),
        ) {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                cornerRadius = 24.dp,
                contentPadding = 0.dp,
            ) {
                Column {
                    // Tab Row
                    TabRow(
                        selectedTabIndex = selectedTabIndex,
                        containerColor = AppColor.Surface,
                        contentColor = MahalatkTheme.primary,
                        indicator = { tabPositions ->
                            SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                                color = MahalatkTheme.primary,
                                height = 3.dp,
                            )
                        },
                    ) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTabIndex == index,
                                onClick = { selectedTabIndex = index },
                                text = {
                                    Text(
                                        text = title,
                                        style = MahalatkTheme.titleMedium,
                                        color = if (selectedTabIndex == index) MahalatkTheme.primary else MahalatkTheme.hint,
                                    )
                                },
                            )
                        }
                    }

                    // Scrollable content
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        when (selectedTabIndex) {
                            0 -> ShopOwnerProfileForm(
                                state = shopOwnerState,
                                viewModel = shopOwnerViewModel,
                                onPickImage = shopPickImage,
                                onShowShopTypeSheet = { showShopTypeSheet = true },
                                onShowCitySheet = { showCitySheet = true },
                                onShowCategorySheet = { showCategorySheet = true },
                                onShowReturnPolicySheet = { showReturnPolicySheet = true },
                                onShowReturnPeriodSheet = { showReturnPeriodSheet = true },
                                onPickLocation = onNavigateToPickLocation,
                            )

                            1 -> EmployeeProfileForm(
                                state = employeeState,
                                viewModel = employeeViewModel,
                                onPickImage = employeePickImage,
                                onShowShopSheet = { showShopSheet = true },
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Save button
            DefaultButton(
                text = stringResource(Res.string.save),
                onClick = {
                    when (selectedTabIndex) {
                        0 -> shopOwnerViewModel.saveProfile()
                        1 -> employeeViewModel.saveProfile()
                    }
                },
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
        selectedItem = shopOwnerState.shopType,
        itemLabel = { type ->
            when (type) {
                ShopType.PHYSICAL -> stringResource(Res.string.physical_shop)
                ShopType.ONLINE -> stringResource(Res.string.online_shop)
            }
        },
        onItemSelected = { shopOwnerViewModel.selectShopType(it) },
        onDismiss = { showShopTypeSheet = false },
    )

    SingleSelectBottomSheet(
        showBottomSheet = showCitySheet,
        title = stringResource(Res.string.select_city),
        items = shopOwnerState.availableCities,
        selectedItem = shopOwnerState.selectedCity,
        itemLabel = { it.name },
        onItemSelected = { shopOwnerViewModel.selectCity(it) },
        onDismiss = { showCitySheet = false },
        isItemSelected = { item, selected -> item.id == selected?.id },
    )

    MultiSelectBottomSheet(
        showBottomSheet = showCategorySheet,
        title = stringResource(Res.string.shop_category),
        items = ShopCategory.entries.toList(),
        selectedItems = shopOwnerState.selectedCategories,
        itemLabel = { stringResource(it.labelRes) },
        onItemToggle = { shopOwnerViewModel.toggleCategory(it) },
        onDismiss = { showCategorySheet = false },
    )

    SingleSelectBottomSheet(
        showBottomSheet = showReturnPolicySheet,
        title = stringResource(Res.string.select_return_policy),
        items = ReturnPolicy.entries.toList(),
        selectedItem = shopOwnerState.returnPolicy,
        itemLabel = { policy ->
            when (policy) {
                ReturnPolicy.EXCHANGE -> stringResource(Res.string.exchange)
                ReturnPolicy.EXCHANGE_AND_RETURN -> stringResource(Res.string.exchange_and_return)
                ReturnPolicy.NOT_AVAILABLE -> stringResource(Res.string.not_available_policy)
            }
        },
        onItemSelected = { shopOwnerViewModel.selectReturnPolicy(it) },
        onDismiss = { showReturnPolicySheet = false },
    )

    SingleSelectBottomSheet(
        showBottomSheet = showReturnPeriodSheet,
        title = stringResource(Res.string.select_return_period),
        items = ReturnPeriod.entries.toList(),
        selectedItem = shopOwnerState.returnPeriod,
        itemLabel = { period ->
            when (period) {
                ReturnPeriod.DAYS_2 -> stringResource(Res.string.within_2_days)
                ReturnPeriod.DAYS_3 -> stringResource(Res.string.within_3_days)
                ReturnPeriod.DAYS_7 -> stringResource(Res.string.within_7_days)
                ReturnPeriod.DAYS_14 -> stringResource(Res.string.within_14_days)
            }
        },
        onItemSelected = { shopOwnerViewModel.selectReturnPeriod(it) },
        onDismiss = { showReturnPeriodSheet = false },
    )

    SingleSelectBottomSheet(
        showBottomSheet = showShopSheet,
        title = stringResource(Res.string.select_shop),
        items = employeeState.availableShops,
        selectedItem = employeeState.selectedShop,
        itemLabel = { it },
        onItemSelected = { employeeViewModel.selectShop(it) },
        onDismiss = { showShopSheet = false },
    )
}

// ─── Shop Owner Profile Form ──────────────────────────────

@Composable
private fun ShopOwnerProfileForm(
    state: com.mahalatk.features.profile.shopowner.ShopOwnerProfileState,
    viewModel: ShopOwnerProfileViewModel,
    onPickImage: () -> Unit,
    onShowShopTypeSheet: () -> Unit,
    onShowCitySheet: () -> Unit,
    onShowCategorySheet: () -> Unit,
    onShowReturnPolicySheet: () -> Unit,
    onShowReturnPeriodSheet: () -> Unit,
    onPickLocation: () -> Unit,
) {
    // Shop Logo (editable)
    ProfileImagePicker(
        imageUrl = state.shopImageUrl,
        imageBytes = state.shopImage,
        label = stringResource(Res.string.upload_shop_logo),
        errorText = state.imageError?.let { stringResource(it) },
        editable = true,
        onPickImage = onPickImage,
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
                viewModel.updateState { copy(ownerName = it, ownerNameError = null) }
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

        // 3. Shop Type
        val shopTypeLabel = when (state.shopType) {
            ShopType.PHYSICAL -> stringResource(Res.string.physical_shop)
            ShopType.ONLINE -> stringResource(Res.string.online_shop)
        }
        DefaultTextField(
            value = shopTypeLabel,
            onValueChanged = {},
            placeholderText = stringResource(Res.string.select_shop_type),
            isEnabled = false,
            onClick = { onShowShopTypeSheet() },
            leadingIcon = {
                Icon(Icons.Filled.Storefront, null, tint = MahalatkTheme.primary)
            },
            trailingIcon = {
                Icon(Icons.Filled.KeyboardArrowDown, null, tint = MahalatkTheme.primary)
            },
            modifier = Modifier.fillMaxWidth(),
        )

        if (state.shopType == ShopType.PHYSICAL) {
            Spacer(modifier = Modifier.height(20.dp))

            // 4. Location
        DefaultTextField(
            value = state.locationAddress,
            onValueChanged = {},
            placeholderText = stringResource(Res.string.select_location),
            isEnabled = false,
            onClick = { onPickLocation() },
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
            onClick = { onShowCitySheet() },
            errorText = state.cityError?.let { stringResource(it) },
            leadingIcon = {
                Icon(
                    painterResource(Res.drawable.ic_city), null,
                    modifier = Modifier.size(24.dp),
                    tint = MahalatkTheme.primary,
                )
            },
            trailingIcon = {
                Icon(Icons.Filled.KeyboardArrowDown, null, tint = MahalatkTheme.primary)
            },
            modifier = Modifier.fillMaxWidth(),
        )

        } // end if PHYSICAL (location + city)

        Spacer(modifier = Modifier.height(20.dp))

        // 6. Shop Category
        val categoryNames = state.selectedCategories.map { stringResource(it.labelRes) }
        val categoryLabel = categoryNames.joinToString(", ")
        DefaultTextField(
            value = categoryLabel,
            onValueChanged = {},
            placeholderText = stringResource(Res.string.shop_category),
            isEnabled = false,
            onClick = { onShowCategorySheet() },
            errorText = state.categoryError?.let { stringResource(it) },
            leadingIcon = {
                Icon(Icons.Filled.Storefront, null, tint = MahalatkTheme.primary)
            },
            trailingIcon = {
                Icon(Icons.Filled.KeyboardArrowDown, null, tint = MahalatkTheme.primary)
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
            onClick = { onShowReturnPolicySheet() },
            leadingIcon = {
                Icon(Icons.Filled.Storefront, null, tint = MahalatkTheme.primary)
            },
            trailingIcon = {
                Icon(Icons.Filled.KeyboardArrowDown, null, tint = MahalatkTheme.primary)
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
                onClick = { onShowReturnPeriodSheet() },
                leadingIcon = {
                    Icon(Icons.Filled.Storefront, null, tint = MahalatkTheme.primary)
                },
                trailingIcon = {
                    Icon(Icons.Filled.KeyboardArrowDown, null, tint = MahalatkTheme.primary)
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        } // end if PHYSICAL (return policy + period)

    }
}

// ─── Employee Profile Form ────────────────────────────────

@Composable
private fun EmployeeProfileForm(
    state: com.mahalatk.features.profile.employee.EmployeeProfileState,
    viewModel: EmployeeProfileViewModel,
    onPickImage: () -> Unit,
    onShowShopSheet: () -> Unit,
) {
    // Employee Photo (editable)
    ProfileImagePicker(
        imageUrl = state.employeeImageUrl,
        imageBytes = state.employeeImage,
        label = stringResource(Res.string.upload_personal_photo),
        errorText = state.imageError?.let { stringResource(it) },
        editable = true,
        onPickImage = onPickImage,
    )

    Spacer(modifier = Modifier.height(24.dp))

    Column(
        modifier = Modifier.padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // 1. Employee Name
        DefaultTextField(
            value = state.employeeName,
            onValueChanged = {
                viewModel.updateState { copy(employeeName = it, employeeNameError = null) }
            },
            placeholderText = stringResource(Res.string.employee_name),
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
            errorText = state.employeeNameError?.let { stringResource(it) },
            leadingIcon = {
                Icon(
                    painterResource(Res.drawable.ic_user), null,
                    modifier = Modifier.size(24.dp),
                    tint = MahalatkTheme.primary,
                )
            },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Select Shop
        DefaultTextField(
            value = state.selectedShop ?: "",
            onValueChanged = {},
            placeholderText = stringResource(Res.string.select_shop),
            isEnabled = false,
            onClick = { onShowShopSheet() },
            errorText = state.selectedShopError?.let { stringResource(it) },
            trailingIcon = {
                Icon(Icons.Filled.KeyboardArrowDown, null, tint = MahalatkTheme.primary)
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
