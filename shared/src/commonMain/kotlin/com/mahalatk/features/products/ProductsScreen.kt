package com.mahalatk.features.products

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.mahalatk.common.component.animation.AnimatedListItem
import com.mahalatk.common.component.card.GlassCard
import com.mahalatk.common.component.header.ScreenHeader
import com.mahalatk.common.component.loading.ShimmerBox
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
fun ProductsScreen(
    viewModel: ProductsViewModel = koinViewModel(),
    onAddProduct: () -> Unit = {},
) {
    val state by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize().background(AppColor.ScreenBackground),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            ScreenHeader(title = stringResource(Res.string.my_products))

            if (state.isLoading) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                    items(4) { ProductCardSkeleton() }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                    itemsIndexed(state.products, key = { _, p -> p.id }) { index, product ->
                        AnimatedListItem(index) { ProductCard(product = product) }
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }

        // FAB
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 20.dp)
                .size(60.dp)
                .shadow(elevation = 8.dp, shape = CircleShape)
                .clip(CircleShape)
                .background(
                    brush = Brush.verticalGradient(
                        listOf(AppColor.Primary, AppColor.Primary.copy(alpha = 0.85f))
                    )
                )
                .noRippleClickable { onAddProduct() },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(28.dp),
            )
        }
    }
}

// ──────────────────────────────────────────────
// Product Card
// ──────────────────────────────────────────────
@Composable
private fun ProductCard(product: ProductItem) {
    GlassCard(
        cornerRadius = CornerDimensions.lg,
        contentPadding = 16.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
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
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${product.price.toInt()} ${stringResource(Res.string.currency)}",
                    style = MahalatkTheme.titleSmall,
                    color = AppColor.Primary,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(8.dp))
                ActionButtons()
            }

            Spacer(modifier = Modifier.width(12.dp))

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
                        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(14.dp)),
                        contentScale = ContentScale.Crop,
                    )
                } else {
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

// ──────────────────────────────────────────────
// Product Card Skeleton
// ──────────────────────────────────────────────
@Composable
private fun ProductCardSkeleton() {
    GlassCard(
        cornerRadius = CornerDimensions.lg,
        contentPadding = 16.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                ShimmerBox(width = 140.dp, height = 14.dp)
                Spacer(modifier = Modifier.height(4.dp))
                ShimmerBox(width = 180.dp, height = 10.dp)
                Spacer(modifier = Modifier.height(8.dp))
                ShimmerBox(width = 80.dp, height = 14.dp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    ShimmerBox(width = 60.dp, height = 28.dp, shape = RoundedCornerShape(8.dp))
                    ShimmerBox(width = 60.dp, height = 28.dp, shape = RoundedCornerShape(8.dp))
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            ShimmerBox(width = 90.dp, height = 90.dp, shape = RoundedCornerShape(14.dp))
        }
    }
}

@Composable
private fun ActionButtons() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(
                    brush = Brush.linearGradient(
                        listOf(
                            AppColor.Primary.copy(alpha = 0.06f),
                            AppColor.Primary.copy(alpha = 0.12f),
                        )
                    )
                )
                .border(0.5.dp, AppColor.Primary.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
                .noRippleClickable { }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painterResource(Res.drawable.ic_edit),
                null,
                tint = AppColor.Primary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                stringResource(Res.string.edit),
                style = MahalatkTheme.labelMedium,
                color = AppColor.Primary,
                fontWeight = FontWeight.SemiBold
            )
        }
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(
                    brush = Brush.linearGradient(
                        listOf(
                            AppColor.Error.copy(alpha = 0.06f),
                            AppColor.Error.copy(alpha = 0.12f),
                        )
                    )
                )
                .border(0.5.dp, AppColor.Error.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
                .noRippleClickable { }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painterResource(Res.drawable.ic_delete),
                null,
                tint = AppColor.Error,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                stringResource(Res.string.delete),
                style = MahalatkTheme.labelMedium,
                color = AppColor.Error,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
