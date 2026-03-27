package com.mahalatk.features.products

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Immutable
data class ProductItem(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String = "",
)

@Immutable
data class ProductsState(
    val products: List<ProductItem> = listOf(
        ProductItem(
            id = "1",
            name = "Flavors of Rose",
            description = "A bouquet of flowers carefully selected to combine beauty and fragrant scent...",
            price = 250.0,
        ),
        ProductItem(
            id = "2",
            name = "White Elegance",
            description = "Premium white roses arranged with delicate greenery for a classic look...",
            price = 320.0,
        ),
        ProductItem(
            id = "3",
            name = "Spring Collection",
            description = "A vibrant mix of seasonal flowers celebrating the colors of spring...",
            price = 180.0,
        ),
        ProductItem(
            id = "4",
            name = "Royal Bouquet",
            description = "Luxurious arrangement featuring premium imported roses and lilies...",
            price = 450.0,
        ),
    ),
)

class ProductsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProductsState())
    val uiState: StateFlow<ProductsState> = _uiState.asStateFlow()
}
