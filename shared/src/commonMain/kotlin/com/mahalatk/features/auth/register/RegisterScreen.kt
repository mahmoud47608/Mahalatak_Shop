package com.mahalatk.features.auth.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahalatk.common.component.bottomsheet.CategoryBottomSheet
import com.mahalatk.common.component.bottomsheet.CityBottomSheet
import com.mahalatk.common.component.bottomsheet.DeliveryTypeBottomSheet
import com.mahalatk.common.component.bottomsheet.ShopSelectorBottomSheet
import com.mahalatk.common.component.button.DefaultButton
import com.mahalatk.common.component.button.LanguageButton
import com.mahalatk.common.component.imagepicker.rememberImagePickerLauncher
import com.mahalatk.common.component.imagepicker.toImageBitmap
import com.mahalatk.common.component.inputs.DefaultTextField
import com.mahalatk.common.component.utilis.noRippleClickable
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.already_have_account
import mahalatk.shared.generated.resources.app_delivery
import mahalatk.shared.generated.resources.confirm_password
import mahalatk.shared.generated.resources.employee
import mahalatk.shared.generated.resources.employee_name
import mahalatk.shared.generated.resources.ic_camera
import mahalatk.shared.generated.resources.ic_location
import mahalatk.shared.generated.resources.ic_lock
import mahalatk.shared.generated.resources.ic_phone
import mahalatk.shared.generated.resources.ic_profile
import mahalatk.shared.generated.resources.ic_user
import mahalatk.shared.generated.resources.owner_name
import mahalatk.shared.generated.resources.password
import mahalatk.shared.generated.resources.phone
import mahalatk.shared.generated.resources.register
import mahalatk.shared.generated.resources.select_city
import mahalatk.shared.generated.resources.select_delivery_type
import mahalatk.shared.generated.resources.select_location
import mahalatk.shared.generated.resources.select_shop
import mahalatk.shared.generated.resources.shop_category
import mahalatk.shared.generated.resources.shop_delivery
import mahalatk.shared.generated.resources.shop_name
import mahalatk.shared.generated.resources.shop_owner
import mahalatk.shared.generated.resources.sign_in
import mahalatk.shared.generated.resources.upload_photo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = koinViewModel(key = currentCompositeKeyHash.toString()),
    onNavigateToLogin: () -> Unit = {},
    onNavigateToPickLocation: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDeliverySheet by remember { mutableStateOf(false) }
    var showCitySheet by remember { mutableStateOf(false) }
    var showShopSheet by remember { mutableStateOf(false) }
    var showCategorySheet by remember { mutableStateOf(false) }

    // Observe location result from PickLocationScreen
    val locationResult by LocationResultHolder.result.collectAsState()
    LaunchedEffect(locationResult) {
        locationResult?.let {
            viewModel.updateLocation(it.lat, it.lng, it.address)
            LocationResultHolder.consume()
        }
    }

    val pickImage = rememberImagePickerLauncher { bytes ->
        when (uiState.accountType) {
            AccountType.SHOP_OWNER -> viewModel.updateState { copy(shopImage = bytes) }
            AccountType.EMPLOYEE -> viewModel.updateState { copy(employeeImage = bytes) }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 48.dp)
    ) {
        LanguageButton(
            modifier = Modifier
                .align(AbsoluteAlignment.Left)
                .padding(horizontal = 16.dp)
        )

        // Tab Layout
        val selectedTabIndex = if (uiState.accountType == AccountType.SHOP_OWNER) 0 else 1
        val tabs = listOf(
            stringResource(Res.string.shop_owner),
            stringResource(Res.string.employee)
        )

        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MahalatkTheme.white,
            contentColor = MahalatkTheme.primary,
            indicator = { tabPositions ->
                SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = MahalatkTheme.primary,
                    height = 3.dp
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {
                        val type = if (index == 0) AccountType.SHOP_OWNER else AccountType.EMPLOYEE
                        if (uiState.accountType != type) {
                            viewModel.switchAccountType(type)
                        }
                    },
                    text = {
                        Text(
                            text = title,
                            style = MahalatkTheme.titleMedium,
                            color = if (selectedTabIndex == index) MahalatkTheme.primary else MahalatkTheme.hint,
                        )
                    }
                )
            }
        }

        // Scrollable Form Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (uiState.accountType) {
                AccountType.SHOP_OWNER -> ShopOwnerForm(
                    uiState = uiState,
                    viewModel = viewModel,
                    onPickImage = pickImage,
                    onShowDeliverySheet = { showDeliverySheet = true },
                    onShowCitySheet = { showCitySheet = true },
                    onShowCategorySheet = { showCategorySheet = true },
                    onPickLocation = onNavigateToPickLocation,
                )

                AccountType.EMPLOYEE -> EmployeeForm(
                    uiState = uiState,
                    viewModel = viewModel,
                    onPickImage = pickImage,
                    onShowShopSheet = { showShopSheet = true },
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Common fields: Password, Confirm Password
            Column(
                modifier = Modifier.padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Password field
                DefaultTextField(
                    value = uiState.password,
                    onValueChanged = {
                        viewModel.updateState { copy(password = it, passwordError = null) }
                    },
                    placeholderText = stringResource(Res.string.password),
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next,
                    errorText = uiState.passwordError?.let { stringResource(it) },
                    visualTransformation = if (uiState.passwordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(Res.drawable.ic_lock),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MahalatkTheme.hint
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            viewModel.updateState { copy(passwordVisible = !passwordVisible) }
                        }) {
                            Icon(
                                imageVector = if (uiState.passwordVisible) Icons.Filled.Visibility
                                else Icons.Filled.VisibilityOff,
                                contentDescription = null,
                                tint = MahalatkTheme.hint
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Confirm Password field
                DefaultTextField(
                    value = uiState.confirmPassword,
                    onValueChanged = {
                        viewModel.updateState {
                            copy(confirmPassword = it, confirmPasswordError = null)
                        }
                    },
                    placeholderText = stringResource(Res.string.confirm_password),
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                    errorText = uiState.confirmPasswordError?.let { stringResource(it) },
                    visualTransformation = if (uiState.confirmPasswordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(Res.drawable.ic_lock),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MahalatkTheme.hint
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            viewModel.updateState {
                                copy(confirmPasswordVisible = !confirmPasswordVisible)
                            }
                        }) {
                            Icon(
                                imageVector = if (uiState.confirmPasswordVisible) Icons.Filled.Visibility
                                else Icons.Filled.VisibilityOff,
                                contentDescription = null,
                                tint = MahalatkTheme.hint
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Register Button
                DefaultButton(
                    text = stringResource(Res.string.register),
                    onClick = { viewModel.register() },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Already have an account? Sign In
                val annotatedText = buildAnnotatedString {
                    append(stringResource(Res.string.already_have_account))
                    withStyle(
                        style = SpanStyle(
                            color = MahalatkTheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(stringResource(Res.string.sign_in))
                    }
                }
                Text(
                    text = annotatedText,
                    fontSize = 14.sp,
                    color = MahalatkTheme.black,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .noRippleClickable { onNavigateToLogin() }
                )
            }
        }
    }

    // Bottom Sheets
    DeliveryTypeBottomSheet(
        showBottomSheet = showDeliverySheet,
        selectedType = uiState.deliveryType,
        onDismiss = { showDeliverySheet = false },
        onTypeSelected = { viewModel.selectDeliveryType(it) }
    )

    CityBottomSheet(
        showBottomSheet = showCitySheet,
        cities = uiState.availableCities,
        selectedCity = uiState.selectedCity,
        onDismiss = { showCitySheet = false },
        onCitySelected = { viewModel.selectCity(it) }
    )

    ShopSelectorBottomSheet(
        showBottomSheet = showShopSheet,
        shops = uiState.availableShops,
        selectedShop = uiState.selectedShop,
        onDismiss = { showShopSheet = false },
        onShopSelected = { viewModel.selectShop(it) }
    )

    CategoryBottomSheet(
        showBottomSheet = showCategorySheet,
        selectedCategories = uiState.selectedCategories,
        onDismiss = { showCategorySheet = false },
        onCategoryToggle = { viewModel.toggleCategory(it) }
    )
}

// ─── Shop Owner Form ─────────────────────────────────────────────────

@Composable
private fun ShopOwnerForm(
    uiState: RegisterState,
    viewModel: RegisterViewModel,
    onPickImage: () -> Unit,
    onShowDeliverySheet: () -> Unit,
    onShowCitySheet: () -> Unit,
    onShowCategorySheet: () -> Unit,
    onPickLocation: () -> Unit,
) {
    // Shop Logo
    ProfileImagePicker(
        imageBytes = uiState.shopImage,
        onClick = onPickImage,
    )

    Spacer(modifier = Modifier.height(24.dp))

    Column(
        modifier = Modifier.padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Shop Name
        DefaultTextField(
            value = uiState.shopName,
            onValueChanged = {
                viewModel.updateState {
                    copy(
                        shopName = it,
                        shopNameError = null
                    )
                }
            },
            placeholderText = stringResource(Res.string.shop_name),
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
            errorText = uiState.shopNameError?.let { stringResource(it) },
            leadingIcon = {
                Icon(
                    painterResource(Res.drawable.ic_user),
                    null,
                    modifier = Modifier.size(24.dp),
                    tint = MahalatkTheme.hint
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Owner Name
        DefaultTextField(
            value = uiState.ownerName,
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
            errorText = uiState.ownerNameError?.let { stringResource(it) },
            leadingIcon = {
                Icon(
                    painterResource(Res.drawable.ic_user),
                    null,
                    modifier = Modifier.size(24.dp),
                    tint = MahalatkTheme.hint
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Mobile (moved here for shop owner to match spec order)
        DefaultTextField(
            value = uiState.mobile,
            onValueChanged = {
                viewModel.updateState { copy(mobile = it, mobileError = null) }
            },
            placeholderText = stringResource(Res.string.phone),
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Next,
            errorText = uiState.mobileError?.let { stringResource(it) },
            leadingIcon = {
                Icon(
                    painterResource(Res.drawable.ic_phone),
                    null,
                    modifier = Modifier.size(24.dp),
                    tint = MahalatkTheme.hint
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Delivery Type selector
        val deliveryLabel = when (uiState.deliveryType) {
            DeliveryType.SHOP_DELIVERY -> stringResource(Res.string.shop_delivery)
            DeliveryType.APP_DELIVERY -> stringResource(Res.string.app_delivery)
            null -> ""
        }

        DefaultTextField(
            value = deliveryLabel,
            onValueChanged = {},
            placeholderText = stringResource(Res.string.select_delivery_type),
            isEnabled = false,
            onClick = { onShowDeliverySheet() },
            errorText = uiState.deliveryTypeError?.let { stringResource(it) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MahalatkTheme.hint,
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // City selector
        DefaultTextField(
            value = uiState.selectedCity?.name ?: "",
            onValueChanged = {},
            placeholderText = stringResource(Res.string.select_city),
            isEnabled = false,
            onClick = { onShowCitySheet() },
            errorText = uiState.cityError?.let { stringResource(it) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MahalatkTheme.hint,
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Shop Location picker
        DefaultTextField(
            value = uiState.locationAddress,
            onValueChanged = {},
            placeholderText = stringResource(Res.string.select_location),
            isEnabled = false,
            onClick = { onPickLocation() },
            errorText = uiState.locationError?.let { stringResource(it) },
            trailingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.ic_location),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = if (uiState.locationLat != null) MahalatkTheme.primary else MahalatkTheme.hint,
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Shop Category selector
        val categoryNames = uiState.selectedCategories.map { stringResource(it.labelRes) }
        val categoryLabel = categoryNames.joinToString(", ")
        DefaultTextField(
            value = categoryLabel,
            onValueChanged = {},
            placeholderText = stringResource(Res.string.shop_category),
            isEnabled = false,
            onClick = { onShowCategorySheet() },
            errorText = uiState.categoryError?.let { stringResource(it) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MahalatkTheme.hint,
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// ─── Employee Form ───────────────────────────────────────────────────

@Composable
private fun EmployeeForm(
    uiState: RegisterState,
    viewModel: RegisterViewModel,
    onPickImage: () -> Unit,
    onShowShopSheet: () -> Unit,
) {
    // Employee Photo
    ProfileImagePicker(
        imageBytes = uiState.employeeImage,
        onClick = onPickImage,
    )

    Spacer(modifier = Modifier.height(24.dp))

    Column(
        modifier = Modifier.padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Employee Name
        DefaultTextField(
            value = uiState.employeeName,
            onValueChanged = {
                viewModel.updateState {
                    copy(
                        employeeName = it,
                        employeeNameError = null
                    )
                }
            },
            placeholderText = stringResource(Res.string.employee_name),
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
            errorText = uiState.employeeNameError?.let { stringResource(it) },
            leadingIcon = {
                Icon(
                    painterResource(Res.drawable.ic_user),
                    null,
                    modifier = Modifier.size(24.dp),
                    tint = MahalatkTheme.hint
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Mobile
        DefaultTextField(
            value = uiState.mobile,
            onValueChanged = {
                viewModel.updateState { copy(mobile = it, mobileError = null) }
            },
            placeholderText = stringResource(Res.string.phone),
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Next,
            errorText = uiState.mobileError?.let { stringResource(it) },
            leadingIcon = {
                Icon(
                    painterResource(Res.drawable.ic_phone),
                    null,
                    modifier = Modifier.size(24.dp),
                    tint = MahalatkTheme.hint
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Select Shop
        DefaultTextField(
            value = uiState.selectedShop ?: "",
            onValueChanged = {},
            placeholderText = stringResource(Res.string.select_shop),
            isEnabled = false,
            onClick = { onShowShopSheet() },
            errorText = uiState.selectedShopError?.let { stringResource(it) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MahalatkTheme.hint,
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// ─── Shared Components ───────────────────────────────────────────────

@Composable
private fun ProfileImagePicker(
    imageBytes: ByteArray?,
    onClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            // Profile Picture Circle
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(MahalatkTheme.iconBackground),
                contentAlignment = Alignment.Center
            ) {
                if (imageBytes != null) {
                    val bitmap = imageBytes.toImageBitmap()
                    if (bitmap != null) {
                        Image(
                            painter = BitmapPainter(bitmap),
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                } else {
                    Image(
                        painter = painterResource(Res.drawable.ic_profile),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 14.dp)
                    )
                }
            }

            // Camera button overlay at bottom-right
            IconButton(
                onClick = onClick,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(40.dp)
                    .offset(x = 1.dp, y = 1.dp)
            ) {
                Image(
                    painter = painterResource(Res.drawable.ic_camera),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Text(
            text = stringResource(Res.string.upload_photo),
            style = MahalatkTheme.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MahalatkTheme.black,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
