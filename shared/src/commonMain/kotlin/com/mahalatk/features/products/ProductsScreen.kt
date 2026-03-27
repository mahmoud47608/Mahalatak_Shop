package com.mahalatk.features.products

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.mahalatk.common.component.utilis.noRippleClickable
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.CornerDimensions
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.currency
import mahalatk.shared.generated.resources.delete
import mahalatk.shared.generated.resources.edit
import mahalatk.shared.generated.resources.ic_delete
import mahalatk.shared.generated.resources.ic_edit
import mahalatk.shared.generated.resources.my_products
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProductsScreen(viewModel: ProductsViewModel = koinViewModel()) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = AppColor.ScreenBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                containerColor = AppColor.Primary,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(60.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            // ── Header (covers status bar) ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                AppColor.Primary,
                                AppColor.Primary.copy(alpha = 0.85f),
                            ),
                        ),
                    )
                    .padding(top = innerPadding.calculateTopPadding() + 8.dp, bottom = 14.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(Res.string.my_products),
                    style = MahalatkTheme.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )
            }

            // ── Product List ──
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                item { Spacer(modifier = Modifier.height(6.dp)) }

                items(state.products, key = { it.id }) { product ->
                    ProductCard(product = product)
                }

                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}

// ──────────────────────────────────────────────
// Product Card
// ──────────────────────────────────────────────
@Composable
private fun ProductCard(product: ProductItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(CornerDimensions.lg),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // ── Product info ──
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = product.name,
                    style = MahalatkTheme.titleSmall,
                    color = AppColor.TextPrimary,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = product.description,
                    style = MahalatkTheme.bodySmall,
                    color = AppColor.TextHint,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Price
                Text(
                    text = "${product.price.toInt()} ${stringResource(Res.string.currency)}",
                    style = MahalatkTheme.titleSmall,
                    color = AppColor.Primary,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Action buttons
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    // Edit button
                    Row(
                        modifier = Modifier
                            .background(
                                color = AppColor.Primary.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(8.dp),
                            )
                            .noRippleClickable { }
                            .padding(horizontal = 14.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_edit),
                            contentDescription = null,
                            tint = AppColor.Primary,
                            modifier = Modifier.size(16.dp),
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = stringResource(Res.string.edit),
                            style = MahalatkTheme.labelMedium,
                            color = AppColor.Primary,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }

                    // Delete button
                    Row(
                        modifier = Modifier
                            .background(
                                color = AppColor.Error.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(8.dp),
                            )
                            .noRippleClickable { }
                            .padding(horizontal = 14.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_delete),
                            contentDescription = null,
                            tint = AppColor.Error,
                            modifier = Modifier.size(16.dp),
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = stringResource(Res.string.delete),
                            style = MahalatkTheme.labelMedium,
                            color = AppColor.Error,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // ── Product image ──
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(AppColor.Secondary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center,
            ) {
                if (product.imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = product.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(14.dp)),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    // Placeholder with first letter
                    Text(
                        text = product.name.take(1).uppercase(),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColor.Primary.copy(alpha = 0.4f),
                    )
                }
            }
        }
    }
}
