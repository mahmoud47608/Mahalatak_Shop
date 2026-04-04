package com.mahalatk.features.offers.add.steps

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.animation.AnimatedListItem
import com.mahalatk.common.component.chips.ChipCloud
import com.mahalatk.features.offers.add.AddOfferState
import com.mahalatk.features.offers.add.AddOfferViewModel
import com.mahalatk.features.offers.add.OfferScopeType
import com.mahalatk.features.offers.add.OfferType
import com.mahalatk.features.offers.add.ProductItem
import com.mahalatk.features.offers.add.components.ProductCheckRow
import com.mahalatk.features.offers.add.components.ScopeOptionCard
import com.mahalatk.features.offers.add.components.SectionLabel
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.CornerDimensions
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.all_products_scope
import mahalatk.shared.generated.resources.categories_scope
import mahalatk.shared.generated.resources.offer_step3_subtitle_categories
import mahalatk.shared.generated.resources.offer_step3_subtitle_products
import mahalatk.shared.generated.resources.products_scope
import mahalatk.shared.generated.resources.select_package_products
import mahalatk.shared.generated.resources.select_scope
import mahalatk.shared.generated.resources.selected_products
import org.jetbrains.compose.resources.stringResource

@Composable
fun Step3Scope(state: AddOfferState, viewModel: AddOfferViewModel) {
    if (state.offerType == OfferType.PACKAGE) {
        PackageProductSelection(state, viewModel)
    } else {
        ScopeSelection(state, viewModel)
    }
}

// ── Scope Selection (non-Package) ───────────────────────────────────────────

@Composable
private fun ScopeSelection(state: AddOfferState, viewModel: AddOfferViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SectionLabel(
            text = stringResource(Res.string.select_scope),
            subtitle = stringResource(Res.string.offer_step3_subtitle_products),
        )

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

        // Product filter + selection
        AnimatedVisibility(
            visible = state.scopeType == OfferScopeType.SPECIFIC_PRODUCTS,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut(),
        ) {
            ProductPickerSection(
                categories = state.availableCategories,
                allProducts = state.availableProducts,
                filterCategories = state.filterCategories,
                selectedIds = state.selectedProductIds,
                onToggleFilter = viewModel::toggleFilterCategory,
                onToggleProduct = viewModel::toggleProduct,
            )
        }
    }
}

// ── Package Product Selection ───────────────────────────────────────────────

@Composable
private fun PackageProductSelection(state: AddOfferState, viewModel: AddOfferViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SectionLabel(
            text = stringResource(Res.string.categories_scope),
            subtitle = stringResource(Res.string.offer_step3_subtitle_categories),
        )

        ProductPickerSection(
            categories = state.availableCategories,
            allProducts = state.availableProducts,
            filterCategories = state.filterCategories,
            selectedIds = state.packageProductIds,
            onToggleFilter = viewModel::togglePackageFilterCategory,
            onToggleProduct = viewModel::togglePackageProduct,
            sectionTitle = stringResource(Res.string.select_package_products),
        )
    }
}

// ── Shared: Product Picker (filter chips + product list) ────────────────────

@Composable
private fun ProductPickerSection(
    categories: List<String>,
    allProducts: List<ProductItem>,
    filterCategories: Set<String>,
    selectedIds: Set<String>,
    onToggleFilter: (String) -> Unit,
    onToggleProduct: (String) -> Unit,
    sectionTitle: String? = null,
) {
    val filteredProducts = remember(filterCategories, allProducts) {
        if (filterCategories.isEmpty()) emptyList()
        else allProducts.filter { it.category in filterCategories }
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ChipCloud(
            items = categories,
            selectedItems = filterCategories,
            label = { it },
            onToggle = onToggleFilter,
        )

        AnimatedVisibility(
            visible = filteredProducts.isNotEmpty(),
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut(),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (sectionTitle != null) {
                    SectionLabel(text = sectionTitle)
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(CornerDimensions.lg),
                    colors = CardDefaults.cardColors(containerColor = AppColor.Surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        filteredProducts.forEachIndexed { index, product ->
                            AnimatedListItem(index) {
                                ProductCheckRow(
                                    product = product,
                                    isChecked = product.id in selectedIds,
                                    onToggle = { onToggleProduct(product.id) },
                                )
                            }
                        }
                    }
                }
            }
        }

        SelectedCount(count = selectedIds.size)
    }
}

// ── Selected count badge ────────────────────────────────────────────────────

@Composable
private fun SelectedCount(count: Int) {
    if (count <= 0) return

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(CornerDimensions.sm))
            .background(AppColor.PrimaryContainer)
            .padding(horizontal = 10.dp, vertical = 6.dp),
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(AppColor.Primary),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "$count",
                style = MahalatkTheme.labelSmall,
                color = AppColor.OnPrimary,
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = stringResource(Res.string.selected_products),
            style = MahalatkTheme.bodySmall,
            color = AppColor.OnPrimaryContainer,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
