package com.mahalatk.features.auth.register

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.mahalatk.common.component.bottomsheet.MultiSelectBottomSheet
import com.mahalatk.common.component.bottomsheet.SingleSelectBottomSheet
import com.mahalatk.common.component.button.DefaultButton
import com.mahalatk.common.component.header.ScreenHeader
import com.mahalatk.common.component.imagepicker.rememberImagePickerLauncher
import com.mahalatk.common.component.imagepicker.toImageBitmap
import com.mahalatk.common.component.inputs.DefaultTextField
import com.mahalatk.common.component.utilis.noRippleClickable
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.CornerDimensions
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.already_have_account
import mahalatk.shared.generated.resources.confirm_password
import mahalatk.shared.generated.resources.employee
import mahalatk.shared.generated.resources.employee_name
import mahalatk.shared.generated.resources.exchange
import mahalatk.shared.generated.resources.exchange_and_return
import mahalatk.shared.generated.resources.ic_camera
import mahalatk.shared.generated.resources.ic_city
import mahalatk.shared.generated.resources.ic_location
import mahalatk.shared.generated.resources.ic_lock
import mahalatk.shared.generated.resources.ic_phone
import mahalatk.shared.generated.resources.ic_profile
import mahalatk.shared.generated.resources.ic_user
import mahalatk.shared.generated.resources.not_available_policy
import mahalatk.shared.generated.resources.online_shop
import mahalatk.shared.generated.resources.owner_name
import mahalatk.shared.generated.resources.password
import mahalatk.shared.generated.resources.phone
import mahalatk.shared.generated.resources.physical_shop
import mahalatk.shared.generated.resources.register
import mahalatk.shared.generated.resources.select_city
import mahalatk.shared.generated.resources.select_location
import mahalatk.shared.generated.resources.select_return_period
import mahalatk.shared.generated.resources.select_return_policy
import mahalatk.shared.generated.resources.select_shop
import mahalatk.shared.generated.resources.select_shop_type
import mahalatk.shared.generated.resources.shop_category
import mahalatk.shared.generated.resources.shop_name
import mahalatk.shared.generated.resources.shop_owner
import mahalatk.shared.generated.resources.sign_in
import mahalatk.shared.generated.resources.upload_personal_photo
import mahalatk.shared.generated.resources.upload_shop_logo
import mahalatk.shared.generated.resources.within_14_days
import mahalatk.shared.generated.resources.within_2_days
import mahalatk.shared.generated.resources.within_3_days
import mahalatk.shared.generated.resources.within_7_days
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = koinViewModel(),
    onNavigateToLogin: () -> Unit = {},
    onNavigateToPickLocation: () -> Unit = {},
    onNavigateToActivation: (phoneNumber: String) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()
    rememberCoroutineScope()
    rememberPagerState(
        initialPage = if (uiState.accountType == AccountType.SHOP_OWNER) 0 else 1,
        pageCount = { 2 },
    )
    var showShopTypeSheet by remember { mutableStateOf(false) }
    var showCitySheet by remember { mutableStateOf(false) }
    var showShopSheet by remember { mutableStateOf(false) }
    var showCategorySheet by remember { mutableStateOf(false) }
    var showReturnPolicySheet by remember { mutableStateOf(false) }
    var showReturnPeriodSheet by remember { mutableStateOf(false) }

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
            AccountType.SHOP_OWNER -> viewModel.updateState {
                copy(
                    shopImage = bytes,
                    imageError = null
                )
            }

            AccountType.EMPLOYEE -> viewModel.updateState {
                copy(
                    employeeImage = bytes,
                    imageError = null
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.ScreenBackground),
    ) {
        ScreenHeader(
            title = stringResource(Res.string.register),
            onBackClick = onNavigateToLogin,
        )

        val cardShape = RoundedCornerShape(24.dp)

        Spacer(modifier = Modifier.height(12.dp))

        // Segmented toggle with sliding indicator
        SlidingTabSelector(
            selectedIndex = if (uiState.accountType == AccountType.SHOP_OWNER) 0 else 1,
            tabs = listOf(
                TabItem(
                    title = stringResource(Res.string.shop_owner),
                    icon = Icons.Filled.Storefront,
                ),
                TabItem(
                    title = stringResource(Res.string.employee),
                    icon = Icons.Filled.Badge,
                ),
            ),
            onTabSelected = { index ->
                val type = if (index == 0) AccountType.SHOP_OWNER else AccountType.EMPLOYEE
                if (uiState.accountType != type) viewModel.switchAccountType(type)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Card wrapping Form
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp)
                .padding(bottom = 20.dp),
            shape = cardShape,
            colors = CardDefaults.cardColors(containerColor = MahalatkTheme.white),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Crossfade(
                targetState = uiState.accountType,
                animationSpec = tween(200),
                modifier = Modifier.fillMaxSize(),
            ) { accountType ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (accountType) {
                        AccountType.SHOP_OWNER -> ShopOwnerForm(
                            uiState = uiState,
                            viewModel = viewModel,
                            onPickImage = pickImage,
                            onShowShopTypeSheet = { showShopTypeSheet = true },
                            onShowCitySheet = { showCitySheet = true },
                            onShowCategorySheet = { showCategorySheet = true },
                            onShowReturnPolicySheet = { showReturnPolicySheet = true },
                            onShowReturnPeriodSheet = { showReturnPeriodSheet = true },
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
                                viewModel.updateState {
                                    copy(password = it, passwordError = null)
                                }
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
                                    tint = MahalatkTheme.primary
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
                                        tint = MahalatkTheme.primary
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
                                    tint = MahalatkTheme.primary
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
                                        tint = MahalatkTheme.primary
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Register Button
        DefaultButton(
            text = stringResource(Res.string.register),
            onClick = {
                viewModel.register()
                val state = viewModel.uiState.value
                if (state.mobileError == null && state.mobile.isNotBlank() && state.mobile.length >= 9) {
                    onNavigateToActivation(state.mobile)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

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

        Spacer(modifier = Modifier.height(8.dp))
    }

    // Bottom Sheets
    SingleSelectBottomSheet(
        showBottomSheet = showShopTypeSheet,
        title = stringResource(Res.string.select_shop_type),
        items = ShopType.entries.toList(),
        selectedItem = uiState.shopType,
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
        items = uiState.availableCities,
        selectedItem = uiState.selectedCity,
        itemLabel = { it.name },
        onItemSelected = { viewModel.selectCity(it) },
        onDismiss = { showCitySheet = false },
        isItemSelected = { item, selected -> item.id == selected?.id }
    )

    SingleSelectBottomSheet(
        showBottomSheet = showShopSheet,
        title = stringResource(Res.string.select_shop),
        items = uiState.availableShops,
        selectedItem = uiState.selectedShop,
        itemLabel = { it },
        onItemSelected = { viewModel.selectShop(it) },
        onDismiss = { showShopSheet = false }
    )

    MultiSelectBottomSheet(
        showBottomSheet = showCategorySheet,
        title = stringResource(Res.string.shop_category),
        items = ShopCategory.entries.toList(),
        selectedItems = uiState.selectedCategories,
        itemLabel = { stringResource(it.labelRes) },
        onItemToggle = { viewModel.toggleCategory(it) },
        onDismiss = { showCategorySheet = false }
    )

    SingleSelectBottomSheet(
        showBottomSheet = showReturnPolicySheet,
        title = stringResource(Res.string.select_return_policy),
        items = ReturnPolicy.entries.toList(),
        selectedItem = uiState.returnPolicy,
        itemLabel = { policy ->
            when (policy) {
                ReturnPolicy.EXCHANGE -> stringResource(Res.string.exchange)
                ReturnPolicy.EXCHANGE_AND_RETURN -> stringResource(Res.string.exchange_and_return)
                ReturnPolicy.NOT_AVAILABLE -> stringResource(Res.string.not_available_policy)
            }
        },
        onItemSelected = { viewModel.selectReturnPolicy(it) },
        onDismiss = { showReturnPolicySheet = false }
    )

    SingleSelectBottomSheet(
        showBottomSheet = showReturnPeriodSheet,
        title = stringResource(Res.string.select_return_period),
        items = ReturnPeriod.entries.toList(),
        selectedItem = uiState.returnPeriod,
        itemLabel = { period ->
            when (period) {
                ReturnPeriod.DAYS_2 -> stringResource(Res.string.within_2_days)
                ReturnPeriod.DAYS_3 -> stringResource(Res.string.within_3_days)
                ReturnPeriod.DAYS_7 -> stringResource(Res.string.within_7_days)
                ReturnPeriod.DAYS_14 -> stringResource(Res.string.within_14_days)
            }
        },
        onItemSelected = { viewModel.selectReturnPeriod(it) },
        onDismiss = { showReturnPeriodSheet = false }
    )
}

// ─── Shop Owner Form ─────────────────────────────────────────────────

@Composable
private fun ShopOwnerForm(
    uiState: RegisterState,
    viewModel: RegisterViewModel,
    onPickImage: () -> Unit,
    onShowShopTypeSheet: () -> Unit,
    onShowCitySheet: () -> Unit,
    onShowCategorySheet: () -> Unit,
    onShowReturnPolicySheet: () -> Unit,
    onShowReturnPeriodSheet: () -> Unit,
    onPickLocation: () -> Unit,
) {
    // Shop Logo
    ProfileImagePicker(
        imageBytes = uiState.shopImage,
        label = stringResource(Res.string.upload_shop_logo),
        errorText = uiState.imageError?.let { stringResource(it) },
        onClick = onPickImage,
    )

    Spacer(modifier = Modifier.height(24.dp))

    Column(
        modifier = Modifier.padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Shop Name
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
                    Icons.Filled.Storefront,
                    null,
                    tint = MahalatkTheme.primary
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Owner Name
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
                    tint = MahalatkTheme.primary
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Phone
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
                    tint = MahalatkTheme.primary
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 4. Shop Type
        val shopTypeLabel = when (uiState.shopType) {
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
                Icon(
                    Icons.Filled.Storefront,
                    null,
                    tint = MahalatkTheme.primary
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MahalatkTheme.primary,
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Fields only visible for physical shops
        if (uiState.shopType == ShopType.PHYSICAL) {
            Spacer(modifier = Modifier.height(20.dp))

            // 5. Location
            DefaultTextField(
                value = uiState.locationAddress,
                onValueChanged = {},
                placeholderText = stringResource(Res.string.select_location),
                isEnabled = false,
                onClick = { onPickLocation() },
                errorText = uiState.locationError?.let { stringResource(it) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.ic_location),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MahalatkTheme.primary,
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 6. City
            DefaultTextField(
                value = uiState.selectedCity?.name ?: "",
                onValueChanged = {},
                placeholderText = stringResource(Res.string.select_city),
                isEnabled = false,
                onClick = { onShowCitySheet() },
                errorText = uiState.cityError?.let { stringResource(it) },
                leadingIcon = {
                    Icon(
                        painterResource(Res.drawable.ic_city),
                        null,
                        modifier = Modifier.size(24.dp),
                        tint = MahalatkTheme.primary
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = MahalatkTheme.primary,
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 7. Shop Category
        val categoryNames = uiState.selectedCategories.map { stringResource(it.labelRes) }
        val categoryLabel = categoryNames.joinToString(", ")
        DefaultTextField(
            value = categoryLabel,
            onValueChanged = {},
            placeholderText = stringResource(Res.string.shop_category),
            isEnabled = false,
            onClick = { onShowCategorySheet() },
            errorText = uiState.categoryError?.let { stringResource(it) },
            leadingIcon = {
                Icon(
                    Icons.Filled.Storefront,
                    null,
                    tint = MahalatkTheme.primary
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MahalatkTheme.primary,
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Return Policy & Period only for physical shops
        if (uiState.shopType == ShopType.PHYSICAL) {
            Spacer(modifier = Modifier.height(20.dp))

            // 8. Return Policy dropdown
            val returnPolicyLabel = when (uiState.returnPolicy) {
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
                    Icon(
                        Icons.Filled.Storefront,
                        null,
                        tint = MahalatkTheme.primary
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = MahalatkTheme.primary,
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            // 9. Return Period dropdown (only visible when policy != NOT_AVAILABLE)
            if (uiState.returnPolicy != ReturnPolicy.NOT_AVAILABLE) {
                Spacer(modifier = Modifier.height(20.dp))

                val returnPeriodLabel = when (uiState.returnPeriod) {
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
                        Icon(
                            Icons.Filled.Storefront,
                            null,
                            tint = MahalatkTheme.primary
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowDown,
                            contentDescription = null,
                            tint = MahalatkTheme.primary,
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

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
        label = stringResource(Res.string.upload_personal_photo),
        errorText = uiState.imageError?.let { stringResource(it) },
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
                    tint = MahalatkTheme.primary
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
                    tint = MahalatkTheme.primary
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
                    tint = MahalatkTheme.primary,
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
    label: String,
    errorText: String? = null,
    onClick: () -> Unit,
) {
    // Pulse animation for dashed border when no image
    val infiniteTransition = rememberInfiniteTransition()
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse,
        ),
    )
    val dashedBorderAlpha = if (imageBytes == null) pulseAlpha else 0.5f

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.noRippleClickable { onClick() },
        ) {
            // Dashed circular border
            Box(
                modifier = Modifier
                    .size(116.dp)
                    .drawBehind {
                        val stroke = Stroke(
                            width = 2.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(
                                floatArrayOf(10f, 10f), 0f
                            ),
                        )
                        drawCircle(
                            color = AppColor.Primary.copy(alpha = dashedBorderAlpha),
                            style = stroke,
                            radius = size.minDimension / 2f,
                        )
                    },
                contentAlignment = Alignment.Center,
            ) {
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
            }

            // Camera button overlay at bottom-right
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 4.dp, bottom = 4.dp)
                    .shadow(elevation = 3.dp, shape = CircleShape)
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(AppColor.Primary),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(Res.drawable.ic_camera),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
            }
        }

        Text(
            text = label,
            style = MahalatkTheme.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MahalatkTheme.black,
            modifier = Modifier.padding(top = 8.dp)
        )

        if (errorText != null) {
            Text(
                text = errorText,
                style = MahalatkTheme.bodySmall,
                color = MahalatkTheme.error,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

// ─── Sliding Tab Selector ───────────────────────────────────────────

private data class TabItem(
    val title: String,
    val icon: ImageVector,
)

@Composable
private fun SlidingTabSelector(
    selectedIndex: Int,
    tabs: List<TabItem>,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val indicatorBias by animateFloatAsState(
        targetValue = if (selectedIndex == 0) -1f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow,
        ),
    )

    Box(
        modifier = modifier
            .height(52.dp)
            .clip(RoundedCornerShape(CornerDimensions.md))
            .background(AppColor.SurfaceContainerHigh)
            .padding(4.dp),
    ) {
        // Sliding indicator
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.5f)
                .align(BiasAlignment(horizontalBias = indicatorBias, verticalBias = 0f))
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(CornerDimensions.sm),
                    ambientColor = AppColor.Primary.copy(alpha = 0.2f),
                    spotColor = AppColor.Primary.copy(alpha = 0.3f),
                )
                .clip(RoundedCornerShape(CornerDimensions.sm))
                .background(AppColor.Primary),
        )

        // Tab items
        Row(modifier = Modifier.fillMaxSize()) {
            tabs.forEachIndexed { index, tab ->
                val isSelected = selectedIndex == index

                val textColor by animateColorAsState(
                    targetValue = if (isSelected) Color.White else AppColor.TextHint,
                    animationSpec = tween(200),
                )
                val iconColor by animateColorAsState(
                    targetValue = if (isSelected) Color.White else AppColor.TextHint,
                    animationSpec = tween(200),
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .noRippleClickable { onTabSelected(index) },
                    contentAlignment = Alignment.Center,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = iconColor,
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = tab.title,
                            style = MahalatkTheme.titleSmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = textColor,
                        )
                    }
                }
            }
        }
    }
}
