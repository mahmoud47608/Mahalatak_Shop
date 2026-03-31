package com.mahalatk.features.offers.add

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.LocalShipping
import androidx.compose.material.icons.rounded.Percent
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material.icons.rounded.ViewInAr
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.animation.AnimatedListItem
import com.mahalatk.common.component.bottomsheet.SuccessBottomSheet
import com.mahalatk.common.component.button.ButtonStyle
import com.mahalatk.common.component.button.DefaultButton
import com.mahalatk.common.component.chips.ChipCloud
import com.mahalatk.common.component.header.ScreenHeader
import com.mahalatk.common.component.inputs.DefaultTextField
import com.mahalatk.common.component.stepper.StepIndicator
import com.mahalatk.common.component.utilis.noRippleClickable
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.CornerDimensions
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.add_offer
import mahalatk.shared.generated.resources.all_products_scope
import mahalatk.shared.generated.resources.buy_how_many
import mahalatk.shared.generated.resources.buy_x_get_y_desc
import mahalatk.shared.generated.resources.buy_x_get_y_offer
import mahalatk.shared.generated.resources.cancel
import mahalatk.shared.generated.resources.categories_scope
import mahalatk.shared.generated.resources.confirm
import mahalatk.shared.generated.resources.discount_fixed
import mahalatk.shared.generated.resources.discount_offer
import mahalatk.shared.generated.resources.discount_offer_desc
import mahalatk.shared.generated.resources.discount_percentage
import mahalatk.shared.generated.resources.duration_review
import mahalatk.shared.generated.resources.end_date
import mahalatk.shared.generated.resources.free_shipping_offer
import mahalatk.shared.generated.resources.free_shipping_offer_desc
import mahalatk.shared.generated.resources.get_how_many_free
import mahalatk.shared.generated.resources.min_cart_value
import mahalatk.shared.generated.resources.next_step
import mahalatk.shared.generated.resources.offer_published_success
import mahalatk.shared.generated.resources.offer_summary
import mahalatk.shared.generated.resources.package_name
import mahalatk.shared.generated.resources.package_offer
import mahalatk.shared.generated.resources.package_offer_desc
import mahalatk.shared.generated.resources.package_price
import mahalatk.shared.generated.resources.previous_step
import mahalatk.shared.generated.resources.products_scope
import mahalatk.shared.generated.resources.publish_offer
import mahalatk.shared.generated.resources.select_offer_type
import mahalatk.shared.generated.resources.select_package_products
import mahalatk.shared.generated.resources.select_scope
import mahalatk.shared.generated.resources.set_offer_logic
import mahalatk.shared.generated.resources.start_date
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddOfferScreen(
    viewModel: AddOfferViewModel = koinViewModel(),
    onBack: () -> Unit = {},
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.ScreenBackground),
    ) {
        ScreenHeader(
            title = stringResource(Res.string.add_offer),
            onBackClick = onBack,
        )

        StepIndicator(
            totalSteps = 4,
            currentStep = state.currentStep,
        )

        // Step error
        state.stepError?.let { errorRes ->
            Text(
                text = stringResource(errorRes),
                style = MahalatkTheme.bodySmall,
                color = AppColor.Error,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        // Animated step content
        AnimatedContent(
            targetState = state.currentStep,
            modifier = Modifier.weight(1f),
            transitionSpec = {
                if (targetState > initialState) {
                    (slideInHorizontally { it } + fadeIn()) togetherWith
                            (slideOutHorizontally { -it } + fadeOut())
                } else {
                    (slideInHorizontally { -it } + fadeIn()) togetherWith
                            (slideOutHorizontally { it } + fadeOut())
                }
            },
            label = "step_content",
        ) { step ->
            when (step) {
                0 -> Step1OfferTypeSelection(state = state, viewModel = viewModel)
                1 -> Step2LogicSetup(state = state, viewModel = viewModel)
                2 -> {
                    if (state.offerType == OfferType.PACKAGE) {
                        Step3PackageProducts(state = state, viewModel = viewModel)
                    } else {
                        Step3Scope(state = state, viewModel = viewModel)
                    }
                }

                3 -> Step4DurationReview(state = state, viewModel = viewModel)
            }
        }

        // Sticky bottom buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (state.currentStep > 0) {
                DefaultButton(
                    text = stringResource(Res.string.previous_step),
                    style = ButtonStyle.OUTLINED,
                    modifier = Modifier.weight(1f),
                    onClick = viewModel::previousStep,
                )
            }
            DefaultButton(
                text = if (state.currentStep == 3) {
                    stringResource(Res.string.publish_offer)
                } else {
                    stringResource(Res.string.next_step)
                },
                modifier = Modifier.weight(1f),
                onClick = if (state.currentStep == 3) viewModel::publish else viewModel::nextStep,
            )
        }
    }

    // Success bottom sheet
    SuccessBottomSheet(
        message = stringResource(Res.string.offer_published_success),
        visible = state.showSuccess,
        onDismiss = onBack,
    )
}

// ---------------------------------------------------------------------------
// Step 1 - Offer Type Selection
// ---------------------------------------------------------------------------

@Composable
private fun Step1OfferTypeSelection(state: AddOfferState, viewModel: AddOfferViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SectionLabel(text = stringResource(Res.string.select_offer_type))

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

@Composable
private fun OfferTypeCard(
    index: Int,
    icon: ImageVector,
    iconColor: Color,
    title: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    AnimatedListItem(index = index) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (isSelected) {
                        Modifier.border(
                            width = 2.dp,
                            color = AppColor.Primary,
                            shape = RoundedCornerShape(CornerDimensions.lg),
                        )
                    } else {
                        Modifier
                    },
                )
                .noRippleClickable { onClick() },
            shape = RoundedCornerShape(CornerDimensions.lg),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(iconColor.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(24.dp),
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MahalatkTheme.titleSmall,
                        color = AppColor.TextPrimary,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = description,
                        style = MahalatkTheme.bodySmall,
                        color = AppColor.TextHint,
                    )
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Step 2 - Logic Setup
// ---------------------------------------------------------------------------

@Composable
private fun Step2LogicSetup(state: AddOfferState, viewModel: AddOfferViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SectionLabel(text = stringResource(Res.string.set_offer_logic))

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
    // Discount mode chips
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

// ---------------------------------------------------------------------------
// Step 3 - Scope
// ---------------------------------------------------------------------------

@Composable
private fun Step3Scope(state: AddOfferState, viewModel: AddOfferViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SectionLabel(text = stringResource(Res.string.select_scope))

        Spacer(modifier = Modifier.height(4.dp))

        // Scope option cards
        ScopeOptionCard(
            index = 0,
            title = stringResource(Res.string.all_products_scope),
            isSelected = state.scopeType == OfferScopeType.ALL_PRODUCTS,
            onClick = { viewModel.selectScopeType(OfferScopeType.ALL_PRODUCTS) },
        )

        ScopeOptionCard(
            index = 1,
            title = stringResource(Res.string.categories_scope),
            isSelected = state.scopeType == OfferScopeType.CATEGORIES,
            onClick = { viewModel.selectScopeType(OfferScopeType.CATEGORIES) },
        )

        ScopeOptionCard(
            index = 2,
            title = stringResource(Res.string.products_scope),
            isSelected = state.scopeType == OfferScopeType.SPECIFIC_PRODUCTS,
            onClick = { viewModel.selectScopeType(OfferScopeType.SPECIFIC_PRODUCTS) },
        )

        // Categories chip cloud
        AnimatedVisibility(
            visible = state.scopeType == OfferScopeType.CATEGORIES,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut(),
        ) {
            ChipCloud(
                items = state.availableCategories,
                selectedItems = state.selectedCategories,
                label = { it },
                onToggle = viewModel::toggleCategory,
                modifier = Modifier.padding(top = 8.dp),
            )
        }

        // Products: categories first, then filtered products
        AnimatedVisibility(
            visible = state.scopeType == OfferScopeType.SPECIFIC_PRODUCTS,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut(),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Select categories to filter products
                Text(
                    text = stringResource(Res.string.categories_scope),
                    style = MahalatkTheme.bodySmall,
                    color = AppColor.TextHint,
                    modifier = Modifier.padding(top = 4.dp),
                )

                ChipCloud(
                    items = state.availableCategories,
                    selectedItems = state.filterCategories,
                    label = { it },
                    onToggle = viewModel::toggleFilterCategory,
                )

                // Show filtered products
                val filteredProducts = if (state.filterCategories.isEmpty()) {
                    emptyList()
                } else {
                    state.availableProducts.filter { it.category in state.filterCategories }
                }

                AnimatedVisibility(
                    visible = filteredProducts.isNotEmpty(),
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut(),
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(CornerDimensions.lg),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    ) {
                        Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                            filteredProducts.forEach { product ->
                                ProductCheckRow(
                                    product = product,
                                    isChecked = product.id in state.selectedProductIds,
                                    onToggle = { viewModel.toggleProduct(product.id) },
                                )
                            }
                        }
                    }
                }

                if (state.selectedProductIds.isNotEmpty()) {
                    Text(
                        text = "${state.selectedProductIds.size} منتجات مختارة",
                        style = MahalatkTheme.bodySmall,
                        color = AppColor.Primary,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}

@Composable
private fun ScopeOptionCard(
    index: Int,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    AnimatedListItem(index = index) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (isSelected) {
                        Modifier.border(
                            width = 2.dp,
                            color = AppColor.Primary,
                            shape = RoundedCornerShape(CornerDimensions.md),
                        )
                    } else {
                        Modifier
                    },
                )
                .noRippleClickable { onClick() },
            shape = RoundedCornerShape(CornerDimensions.md),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Radio indicator
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) AppColor.Primary else Color.Transparent,
                        )
                        .border(
                            width = 2.dp,
                            color = if (isSelected) AppColor.Primary else AppColor.Border,
                            shape = CircleShape,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = title,
                    style = MahalatkTheme.titleSmall,
                    color = AppColor.TextPrimary,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                )
            }
        }
    }
}

@Composable
private fun ProductCheckRow(
    product: ProductItem,
    isChecked: Boolean,
    onToggle: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable { onToggle() }
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Product avatar placeholder
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(AppColor.Secondary.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = product.name.take(1),
                style = MahalatkTheme.labelSmall,
                color = AppColor.Primary,
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = product.name,
            style = MahalatkTheme.bodyMedium,
            color = AppColor.TextPrimary,
            modifier = Modifier.weight(1f),
        )

        Checkbox(
            checked = isChecked,
            onCheckedChange = { onToggle() },
            colors = CheckboxDefaults.colors(
                checkedColor = AppColor.Primary,
                uncheckedColor = AppColor.Border,
                checkmarkColor = Color.White,
            ),
        )
    }
}

// ---------------------------------------------------------------------------
// Step 3 (Package) - Select Package Products
// ---------------------------------------------------------------------------

@Composable
private fun Step3PackageProducts(state: AddOfferState, viewModel: AddOfferViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // 1. Select categories first
        SectionLabel(text = stringResource(Res.string.categories_scope))

        ChipCloud(
            items = state.availableCategories,
            selectedItems = state.filterCategories,
            label = { it },
            onToggle = { viewModel.togglePackageFilterCategory(it) },
        )

        // 2. Show products from selected categories
        val filteredProducts = if (state.filterCategories.isEmpty()) {
            emptyList()
        } else {
            state.availableProducts.filter { it.category in state.filterCategories }
        }

        AnimatedVisibility(
            visible = filteredProducts.isNotEmpty(),
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut(),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SectionLabel(text = stringResource(Res.string.select_package_products))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(CornerDimensions.lg),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        filteredProducts.forEachIndexed { index, product ->
                            val isChecked = product.id in state.packageProductIds
                            AnimatedListItem(index) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .noRippleClickable { viewModel.togglePackageProduct(product.id) }
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Checkbox(
                                        checked = isChecked,
                                        onCheckedChange = { viewModel.togglePackageProduct(product.id) },
                                        colors = CheckboxDefaults.colors(checkedColor = AppColor.Primary),
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = product.name,
                                        style = MahalatkTheme.bodyMedium,
                                        color = AppColor.TextPrimary,
                                        fontWeight = if (isChecked) FontWeight.SemiBold else FontWeight.Normal,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Show selected count
        if (state.packageProductIds.isNotEmpty()) {
            Text(
                text = "${state.packageProductIds.size} منتجات مختارة",
                style = MahalatkTheme.bodySmall,
                color = AppColor.Primary,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Step 4 - Duration & Review
// ---------------------------------------------------------------------------

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
private fun Step4DurationReview(state: AddOfferState, viewModel: AddOfferViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        var showStartDatePicker by remember { mutableStateOf(false) }
        var showEndDatePicker by remember { mutableStateOf(false) }

        SectionLabel(text = stringResource(Res.string.duration_review))

        Spacer(modifier = Modifier.height(4.dp))

        // Start date
        DefaultTextField(
            value = state.startDate,
            onValueChanged = {},
            placeholderText = stringResource(Res.string.start_date),
            isEnabled = false,
            onClick = { showStartDatePicker = true },
            leadingIcon = {
                Icon(
                    Icons.Rounded.CalendarMonth, null,
                    modifier = Modifier.size(24.dp),
                    tint = MahalatkTheme.primary,
                )
            },
        )

        // End date
        DefaultTextField(
            value = state.endDate,
            onValueChanged = {},
            placeholderText = stringResource(Res.string.end_date),
            isEnabled = false,
            onClick = { showEndDatePicker = true },
            leadingIcon = {
                Icon(
                    Icons.Rounded.CalendarMonth, null,
                    modifier = Modifier.size(24.dp),
                    tint = MahalatkTheme.primary,
                )
            },
        )

        // Start Date Picker Dialog
        if (showStartDatePicker) {
            val datePickerState = rememberDatePickerState()
            DatePickerDialog(
                onDismissRequest = { showStartDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            viewModel.updateStartDate(formatDateMillis(millis))
                        }
                        showStartDatePicker = false
                    }) { Text(stringResource(Res.string.confirm)) }
                },
                dismissButton = {
                    TextButton(onClick = { showStartDatePicker = false }) {
                        Text(stringResource(Res.string.cancel))
                    }
                },
            ) {
                DatePicker(state = datePickerState)
            }
        }

        // End Date Picker Dialog
        if (showEndDatePicker) {
            val datePickerState = rememberDatePickerState()
            DatePickerDialog(
                onDismissRequest = { showEndDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            viewModel.updateEndDate(formatDateMillis(millis))
                        }
                        showEndDatePicker = false
                    }) { Text(stringResource(Res.string.confirm)) }
                },
                dismissButton = {
                    TextButton(onClick = { showEndDatePicker = false }) {
                        Text(stringResource(Res.string.cancel))
                    }
                },
            ) {
                DatePicker(state = datePickerState)
            }
        }

        // Summary card
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(Res.string.offer_summary),
            style = MahalatkTheme.titleSmall,
            color = AppColor.TextPrimary,
            fontWeight = FontWeight.SemiBold,
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(CornerDimensions.lg),
            colors = CardDefaults.cardColors(
                containerColor = AppColor.Primary.copy(alpha = 0.05f),
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        ) {
            Text(
                text = buildSummaryText(state),
                style = MahalatkTheme.bodyMedium,
                color = AppColor.TextPrimary,
                modifier = Modifier.padding(16.dp),
                lineHeight = MahalatkTheme.bodyMedium.lineHeight,
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Helpers
// ---------------------------------------------------------------------------

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MahalatkTheme.titleMedium,
        color = AppColor.TextPrimary,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
private fun buildSummaryText(state: AddOfferState): String {
    val typeDesc = when (state.offerType) {
        OfferType.DISCOUNT -> {
            val modeLabel = when (state.discountMode) {
                DiscountMode.PERCENTAGE -> "${state.discountValue}%"
                DiscountMode.FIXED_AMOUNT -> "${state.discountValue} EGP"
            }
            "\u062E\u0635\u0645 $modeLabel" // "خصم"
        }

        OfferType.BUY_X_GET_Y ->
            "\u0627\u0634\u062A\u0631\u064A ${state.buyQuantity} \u0648\u0627\u062D\u0635\u0644 \u0639\u0644\u0649 ${state.getQuantity} \u0645\u062C\u0627\u0646\u0627\u064B"

        OfferType.FREE_SHIPPING -> {
            val minCart = if (state.freeShippingMinCart.isNotBlank()) {
                " \u0639\u0646\u062F \u0627\u0644\u0634\u0631\u0627\u0621 \u0628\u0640 ${state.freeShippingMinCart}"
            } else ""
            "\u0634\u062D\u0646 \u0645\u062C\u0627\u0646\u064A$minCart"
        }

        OfferType.PACKAGE ->
            "\u0628\u0627\u0643\u062F\u062C ${state.packageName} \u0628\u0633\u0639\u0631 ${state.packagePrice} (${state.packageProductIds.size} \u0645\u0646\u062A\u062C\u0627\u062A)"

        null -> ""
    }

    val scopeDesc = if (state.offerType == OfferType.PACKAGE) {
        "" // Package doesn't need scope — products are already selected
    } else {
        when (state.scopeType) {
            OfferScopeType.ALL_PRODUCTS -> " \u0639\u0644\u0649 \u0643\u0644 \u0627\u0644\u0645\u0646\u062A\u062C\u0627\u062A"
            OfferScopeType.CATEGORIES -> {
                val cats = state.selectedCategories.joinToString(", ")
                " \u0639\u0644\u0649 \u0623\u0642\u0633\u0627\u0645: $cats"
            }

            OfferScopeType.SPECIFIC_PRODUCTS -> {
                val count = state.selectedProductIds.size
                " \u0639\u0644\u0649 $count \u0645\u0646\u062A\u062C\u0627\u062A \u0645\u062E\u062A\u0627\u0631\u0629"
            }
        }
    }

    val dateRange = if (state.startDate.isNotBlank() && state.endDate.isNotBlank()) {
        "\u060C \u0645\u0646 ${state.startDate} \u0625\u0644\u0649 ${state.endDate}"
    } else {
        ""
    }

    return "\u0639\u0631\u0636\u0643: $typeDesc$scopeDesc$dateRange"
}

/** Formats epoch millis to DD/MM/YYYY string. */
private fun formatDateMillis(millis: Long): String {
    val totalDays = (millis / 86400000L).toInt()
    // Simple date calculation from epoch days
    var y = 1970
    var remaining = totalDays
    while (true) {
        val daysInYear = if (y % 4 == 0 && (y % 100 != 0 || y % 400 == 0)) 366 else 365
        if (remaining < daysInYear) break
        remaining -= daysInYear
        y++
    }
    val monthDays = intArrayOf(
        31,
        if (y % 4 == 0 && (y % 100 != 0 || y % 400 == 0)) 29 else 28,
        31,
        30,
        31,
        30,
        31,
        31,
        30,
        31,
        30,
        31
    )
    var m = 0
    while (m < 12 && remaining >= monthDays[m]) {
        remaining -= monthDays[m]
        m++
    }
    val d = remaining + 1
    return "${d.toString().padStart(2, '0')}/${(m + 1).toString().padStart(2, '0')}/$y"
}
