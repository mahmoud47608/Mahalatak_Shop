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
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.header.ScreenHeader
import com.mahalatk.common.component.inputs.DefaultTextField
import com.mahalatk.features.auth.register.DeliveryType
import com.mahalatk.features.profile.component.ProfileImagePicker
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.app_delivery
import mahalatk.shared.generated.resources.confirm_password
import mahalatk.shared.generated.resources.ic_city
import mahalatk.shared.generated.resources.ic_location
import mahalatk.shared.generated.resources.ic_lock
import mahalatk.shared.generated.resources.ic_phone
import mahalatk.shared.generated.resources.ic_user
import mahalatk.shared.generated.resources.owner_name
import mahalatk.shared.generated.resources.password
import mahalatk.shared.generated.resources.phone
import mahalatk.shared.generated.resources.profile
import mahalatk.shared.generated.resources.select_city
import mahalatk.shared.generated.resources.select_delivery_type
import mahalatk.shared.generated.resources.select_location
import mahalatk.shared.generated.resources.shop_category
import mahalatk.shared.generated.resources.shop_delivery
import mahalatk.shared.generated.resources.shop_name
import mahalatk.shared.generated.resources.upload_shop_logo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ShopOwnerProfileScreen(
    viewModel: ShopOwnerProfileViewModel = koinViewModel(),
    onBack: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(AppColor.ScreenBackground)) {

        // ── ScreenHeader (same as AddProduct) ──
        ScreenHeader(
            title = stringResource(Res.string.profile),
            onBackClick = onBack,
        )

        // ── Scrollable Content ──
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 16.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // Shop Logo (display only — no camera icon)
                    ProfileImagePicker(
                        imageUrl = uiState.shopImageUrl,
                        label = stringResource(Res.string.upload_shop_logo),
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Column(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        // Shop Name
                        DefaultTextField(
                            value = uiState.shopName,
                            onValueChanged = {},
                            placeholderText = stringResource(Res.string.shop_name),
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                            isEnabled = false,
                            leadingIcon = {
                                Icon(Icons.Filled.Storefront, null, tint = MahalatkTheme.primary)
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Owner Name
                        DefaultTextField(
                            value = uiState.ownerName,
                            onValueChanged = {},
                            placeholderText = stringResource(Res.string.owner_name),
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                            isEnabled = false,
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

                        // Mobile
                        DefaultTextField(
                            value = uiState.mobile,
                            onValueChanged = {},
                            placeholderText = stringResource(Res.string.phone),
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Next,
                            isEnabled = false,
                            leadingIcon = {
                                Icon(
                                    painterResource(Res.drawable.ic_phone), null,
                                    modifier = Modifier.size(24.dp),
                                    tint = MahalatkTheme.primary,
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Delivery Type
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
                            leadingIcon = {
                                Icon(Icons.Filled.LocalShipping, null, tint = MahalatkTheme.primary)
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

                        Spacer(modifier = Modifier.height(20.dp))

                        // City
                        DefaultTextField(
                            value = uiState.selectedCity?.name ?: "",
                            onValueChanged = {},
                            placeholderText = stringResource(Res.string.select_city),
                            isEnabled = false,
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

                        Spacer(modifier = Modifier.height(20.dp))

                        // Location
                        DefaultTextField(
                            value = uiState.locationAddress,
                            onValueChanged = {},
                            placeholderText = stringResource(Res.string.select_location),
                            isEnabled = false,
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

                        // Shop Category
                        val categoryNames =
                            uiState.selectedCategories.map { stringResource(it.labelRes) }
                        val categoryLabel = categoryNames.joinToString(", ")
                        DefaultTextField(
                            value = categoryLabel,
                            onValueChanged = {},
                            placeholderText = stringResource(Res.string.shop_category),
                            isEnabled = false,
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

                        Spacer(modifier = Modifier.height(16.dp))

                        // Password
                        DefaultTextField(
                            value = uiState.password,
                            onValueChanged = {},
                            placeholderText = stringResource(Res.string.password),
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next,
                            isEnabled = false,
                            visualTransformation = PasswordVisualTransformation(),
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_lock),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = MahalatkTheme.primary,
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Confirm Password
                        DefaultTextField(
                            value = uiState.confirmPassword,
                            onValueChanged = {},
                            placeholderText = stringResource(Res.string.confirm_password),
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done,
                            isEnabled = false,
                            visualTransformation = PasswordVisualTransformation(),
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_lock),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = MahalatkTheme.primary,
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
