package com.mahalatk.features.offers.add.steps

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.animation.AnimatedListItem
import com.mahalatk.common.component.chips.ChipCloud
import com.mahalatk.common.component.utilis.noRippleClickable
import com.mahalatk.features.offers.add.AddOfferState
import com.mahalatk.features.offers.add.AddOfferViewModel
import com.mahalatk.features.offers.add.OfferScopeType
import com.mahalatk.features.offers.add.OfferType
import com.mahalatk.features.offers.add.components.ProductCheckRow
import com.mahalatk.features.offers.add.components.ScopeOptionCard
import com.mahalatk.features.offers.add.components.SectionLabel
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.CornerDimensions
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.all_products_scope
import mahalatk.shared.generated.resources.categories_scope
import mahalatk.shared.generated.resources.products_scope
import mahalatk.shared.generated.resources.select_package_products
import mahalatk.shared.generated.resources.select_scope
import org.jetbrains.compose.resources.stringResource

@Composable
fun Step3Scope(state: AddOfferState, viewModel: AddOfferViewModel) {
    if (state.offerType == OfferType.PACKAGE) {
        PackageProductSelection(state = state, viewModel = viewModel)
    } else {
        ScopeSelection(state = state, viewModel = viewModel)
    }
}

@Composable
private fun ScopeSelection(state: AddOfferState, viewModel: AddOfferViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SectionLabel(text = stringResource(Res.string.select_scope))

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

        // Category chips
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

        // Product selection (filter by category, then pick products)
        AnimatedVisibility(
            visible = state.scopeType == OfferScopeType.SPECIFIC_PRODUCTS,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut(),
        ) {
            ProductFilterSection(
                state = state,
                filterCategories = state.filterCategories,
                selectedProductIds = state.selectedProductIds,
                onToggleFilterCategory = viewModel::toggleFilterCategory,
                onToggleProduct = viewModel::toggleProduct,
            )
        }
    }
}

@Composable
private fun PackageProductSelection(state: AddOfferState, viewModel: AddOfferViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SectionLabel(text = stringResource(Res.string.categories_scope))

        ChipCloud(
            items = state.availableCategories,
            selectedItems = state.filterCategories,
            label = { it },
            onToggle = { viewModel.togglePackageFilterCategory(it) },
        )

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

        SelectedProductCount(count = state.packageProductIds.size)
    }
}

@Composable
private fun ProductFilterSection(
    state: AddOfferState,
    filterCategories: Set<String>,
    selectedProductIds: Set<String>,
    onToggleFilterCategory: (String) -> Unit,
    onToggleProduct: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = stringResource(Res.string.categories_scope),
            style = MahalatkTheme.bodySmall,
            color = AppColor.TextHint,
            modifier = Modifier.padding(top = 4.dp),
        )

        ChipCloud(
            items = state.availableCategories,
            selectedItems = filterCategories,
            label = { it },
            onToggle = onToggleFilterCategory,
        )

        val filteredProducts = if (filterCategories.isEmpty()) {
            emptyList()
        } else {
            state.availableProducts.filter { it.category in filterCategories }
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
                            isChecked = product.id in selectedProductIds,
                            onToggle = { onToggleProduct(product.id) },
                        )
                    }
                }
            }
        }

        SelectedProductCount(count = selectedProductIds.size)
    }
}

@Composable
private fun SelectedProductCount(count: Int) {
    if (count > 0) {
        Text(
            text = "$count منتجات مختارة",
            style = MahalatkTheme.bodySmall,
            color = AppColor.Primary,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
