package com.mahalatk.features.auth.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.ChildCare
import androidx.compose.material.icons.outlined.Man
import androidx.compose.material.icons.outlined.Woman
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahalatk.common.component.bottomsheet.AppLanguage
import com.mahalatk.common.component.bottomsheet.DeliveryTypeBottomSheet
import com.mahalatk.common.component.bottomsheet.LanguageSelectorBottomSheet
import com.mahalatk.common.component.bottomsheet.ShopSelectorBottomSheet
import com.mahalatk.common.component.button.DefaultButton
import com.mahalatk.common.component.button.LanguageButton
import com.mahalatk.common.component.imagepicker.rememberImagePickerLauncher
import com.mahalatk.common.component.imagepicker.toImageBitmap
import com.mahalatk.common.component.inputs.DefaultTextField
import com.mahalatk.common.util.getCurrentLanguageCode
import com.mahalatk.ui.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.already_have_account
import mahalatk.shared.generated.resources.app_delivery
import mahalatk.shared.generated.resources.confirm_password
import mahalatk.shared.generated.resources.delivery_type
import mahalatk.shared.generated.resources.employee
import mahalatk.shared.generated.resources.employee_name
import mahalatk.shared.generated.resources.owner_name
import mahalatk.shared.generated.resources.password
import mahalatk.shared.generated.resources.phone
import mahalatk.shared.generated.resources.register
import mahalatk.shared.generated.resources.select_delivery_type
import mahalatk.shared.generated.resources.select_shop
import mahalatk.shared.generated.resources.shop_category
import mahalatk.shared.generated.resources.shop_delivery
import mahalatk.shared.generated.resources.shop_name
import mahalatk.shared.generated.resources.shop_owner
import mahalatk.shared.generated.resources.sign_in
import mahalatk.shared.generated.resources.upload_photo
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = koinViewModel(key = currentCompositeKeyHash.toString()),
    onNavigateToLogin: () -> Unit = {},
    onLanguageChanged: (AppLanguage) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()
    var showLanguageSheet by remember { mutableStateOf(false) }
    var showDeliverySheet by remember { mutableStateOf(false) }
    var showShopSheet by remember { mutableStateOf(false) }

    val pickImage = rememberImagePickerLauncher { bytes ->
        when (uiState.accountType) {
            AccountType.SHOP_OWNER -> viewModel.updateState { copy(shopImage = bytes) }
            AccountType.EMPLOYEE -> viewModel.updateState { copy(employeeImage = bytes) }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MahalatkTheme.white)
    ) {
        // Top Bar - Back + Language
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateToLogin) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MahalatkTheme.black
                )
            }
            LanguageButton(onClick = { showLanguageSheet = true })
        }

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
                            imageVector = Icons.Filled.Lock,
                            contentDescription = null,
                            tint = MahalatkTheme.hint
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            viewModel.updateState { copy(passwordVisible = !uiState.passwordVisible) }
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
                            imageVector = Icons.Filled.Lock,
                            contentDescription = null,
                            tint = MahalatkTheme.hint
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            viewModel.updateState {
                                copy(confirmPasswordVisible = !uiState.confirmPasswordVisible)
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
                        .clickable { onNavigateToLogin() }
                )
            }
        }
    }

    // Bottom Sheets
    LanguageSelectorBottomSheet(
        showBottomSheet = showLanguageSheet,
        currentLanguage = if (getCurrentLanguageCode() == "ar") AppLanguage.ARABIC else AppLanguage.ENGLISH,
        onDismiss = { showLanguageSheet = false },
        onLanguageSelected = { language -> onLanguageChanged(language) }
    )

    DeliveryTypeBottomSheet(
        showBottomSheet = showDeliverySheet,
        selectedType = uiState.deliveryType,
        onDismiss = { showDeliverySheet = false },
        onTypeSelected = { viewModel.selectDeliveryType(it) }
    )

    ShopSelectorBottomSheet(
        showBottomSheet = showShopSheet,
        shops = uiState.availableShops,
        selectedShop = uiState.selectedShop,
        onDismiss = { showShopSheet = false },
        onShopSelected = { viewModel.selectShop(it) }
    )
}

// ─── Shop Owner Form ─────────────────────────────────────────────────

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ShopOwnerForm(
    uiState: RegisterState,
    viewModel: RegisterViewModel,
    onPickImage: () -> Unit,
    onShowDeliverySheet: () -> Unit,
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
                Icon(Icons.Filled.Storefront, null, tint = MahalatkTheme.hint)
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
                Icon(Icons.Filled.Person, null, tint = MahalatkTheme.hint)
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
                Icon(Icons.Filled.Phone, null, tint = MahalatkTheme.hint)
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Shop Category section
        Text(
            text = stringResource(Res.string.shop_category),
            style = MahalatkTheme.titleMedium,
            color = MahalatkTheme.black,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            ShopCategory.entries.forEach { category ->
                CategoryCircle(
                    category = category,
                    isSelected = category in uiState.selectedCategories,
                    onClick = { viewModel.toggleCategory(category) }
                )
            }
        }

        if (uiState.categoryError != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(uiState.categoryError),
                color = MahalatkTheme.error,
                style = MahalatkTheme.labelMedium,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Delivery Type selector
        Text(
            text = stringResource(Res.string.delivery_type),
            style = MahalatkTheme.titleMedium,
            color = MahalatkTheme.black,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        val deliveryLabel = when (uiState.deliveryType) {
            DeliveryType.SHOP_DELIVERY -> stringResource(Res.string.shop_delivery)
            DeliveryType.APP_DELIVERY -> stringResource(Res.string.app_delivery)
            null -> stringResource(Res.string.select_delivery_type)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    1.dp,
                    if (uiState.deliveryTypeError != null) MahalatkTheme.error else MahalatkTheme.border,
                    RoundedCornerShape(12.dp)
                )
                .clickable { onShowDeliverySheet() }
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = deliveryLabel,
                style = MahalatkTheme.bodyMedium,
                color = if (uiState.deliveryType != null) MahalatkTheme.black else MahalatkTheme.hint,
            )
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = null,
                tint = MahalatkTheme.hint,
            )
        }

        if (uiState.deliveryTypeError != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(uiState.deliveryTypeError),
                color = MahalatkTheme.error,
                style = MahalatkTheme.labelMedium,
                modifier = Modifier.fillMaxWidth()
            )
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
                Icon(Icons.Filled.Person, null, tint = MahalatkTheme.hint)
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
                Icon(Icons.Filled.Phone, null, tint = MahalatkTheme.hint)
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Select Shop
        Text(
            text = stringResource(Res.string.select_shop),
            style = MahalatkTheme.titleMedium,
            color = MahalatkTheme.black,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    1.dp,
                    if (uiState.selectedShopError != null) MahalatkTheme.error else MahalatkTheme.border,
                    RoundedCornerShape(12.dp)
                )
                .clickable { onShowShopSheet() }
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = uiState.selectedShop ?: stringResource(Res.string.select_shop),
                style = MahalatkTheme.bodyMedium,
                color = if (uiState.selectedShop != null) MahalatkTheme.black else MahalatkTheme.hint,
            )
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = null,
                tint = MahalatkTheme.hint,
            )
        }

        if (uiState.selectedShopError != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(uiState.selectedShopError),
                color = MahalatkTheme.error,
                style = MahalatkTheme.labelMedium,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// ─── Shared Components ───────────────────────────────────────────────

@Composable
private fun ProfileImagePicker(
    imageBytes: ByteArray?,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(MahalatkTheme.iconBackground)
            .border(2.dp, MahalatkTheme.border, CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (imageBytes != null) {
            val bitmap = imageBytes.toImageBitmap()
            if (bitmap != null) {
                Image(
                    painter = BitmapPainter(bitmap),
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Filled.CameraAlt,
                    contentDescription = null,
                    tint = MahalatkTheme.hint,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(Res.string.upload_photo),
                    color = MahalatkTheme.hint,
                    fontSize = 10.sp,
                )
            }
        }
    }
}

@Composable
private fun CategoryCircle(
    category: ShopCategory,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val icon: ImageVector = when (category) {
        ShopCategory.MEN -> Icons.Outlined.Man
        ShopCategory.WOMEN -> Icons.Outlined.Woman
        ShopCategory.KIDS -> Icons.Outlined.ChildCare
        ShopCategory.MEN_SHOES -> Icons.Outlined.Man
        ShopCategory.WOMEN_SHOES -> Icons.Outlined.Woman
    }

    val bgColor =
        if (isSelected) MahalatkTheme.primary.copy(alpha = 0.12f) else MahalatkTheme.iconBackground
    val borderColor = if (isSelected) MahalatkTheme.primary else MahalatkTheme.border
    val iconTint = if (isSelected) MahalatkTheme.primary else MahalatkTheme.hint

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(64.dp)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(bgColor)
                .border(2.dp, borderColor, CircleShape)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(category.labelRes),
            style = MahalatkTheme.labelMedium,
            color = if (isSelected) MahalatkTheme.primary else MahalatkTheme.black,
            textAlign = TextAlign.Center,
            maxLines = 2,
        )
    }
}
