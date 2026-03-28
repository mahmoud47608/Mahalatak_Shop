package com.mahalatk.features.products.add

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahalatk.common.component.bottomsheet.MultiSelectBottomSheet
import com.mahalatk.common.component.button.DefaultButton
import com.mahalatk.common.component.header.ScreenHeader
import com.mahalatk.common.component.imagepicker.rememberMultiImagePickerLauncher
import com.mahalatk.common.component.imagepicker.toImageBitmap
import com.mahalatk.common.component.inputs.DefaultTextField
import com.mahalatk.common.component.utilis.DashedAttachBox
import com.mahalatk.common.component.utilis.noRippleClickable
import com.mahalatk.common.component.videopicker.rememberVideoPickerLauncher
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.add_images
import mahalatk.shared.generated.resources.add_product
import mahalatk.shared.generated.resources.add_video
import mahalatk.shared.generated.resources.description_ar
import mahalatk.shared.generated.resources.description_en
import mahalatk.shared.generated.resources.discount
import mahalatk.shared.generated.resources.discount_value
import mahalatk.shared.generated.resources.fixed_price_discount
import mahalatk.shared.generated.resources.no_discount
import mahalatk.shared.generated.resources.percentage_discount
import mahalatk.shared.generated.resources.price
import mahalatk.shared.generated.resources.product_images
import mahalatk.shared.generated.resources.product_name_ar
import mahalatk.shared.generated.resources.product_name_en
import mahalatk.shared.generated.resources.product_video
import mahalatk.shared.generated.resources.select_category
import mahalatk.shared.generated.resources.select_sub_category
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

private const val MAX_IMAGES = 6

@Composable
fun AddProductScreen(
    viewModel: AddProductViewModel = koinViewModel(),
    onBack: () -> Unit = {},
) {
    val state by viewModel.uiState.collectAsState()
    var showCategorySheet by remember { mutableStateOf(false) }
    var showSubCategorySheet by remember { mutableStateOf(false) }

    val pickImages = rememberMultiImagePickerLauncher { images ->
        viewModel.addImages(images)
    }

    val pickVideo = rememberVideoPickerLauncher { bytes ->
        viewModel.setVideo(bytes)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.ScreenBackground),
    ) {
        ScreenHeader(
            title = stringResource(Res.string.add_product),
            onBackClick = onBack,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // ── Product Name AR ──
            DefaultTextField(
                value = state.nameAr,
                onValueChanged = {
                    viewModel.updateState { copy(nameAr = it, nameArError = null) }
                },
                placeholderText = stringResource(Res.string.product_name_ar),
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                errorText = state.nameArError?.let { stringResource(it) },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Product Name EN ──
            DefaultTextField(
                value = state.nameEn,
                onValueChanged = {
                    viewModel.updateState { copy(nameEn = it, nameEnError = null) }
                },
                placeholderText = stringResource(Res.string.product_name_en),
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                errorText = state.nameEnError?.let { stringResource(it) },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Description AR ──
            DefaultTextField(
                value = state.descriptionAr,
                onValueChanged = {
                    viewModel.updateState { copy(descriptionAr = it, descriptionArError = null) }
                },
                placeholderText = stringResource(Res.string.description_ar),
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                maxLines = 8,
                minLines = 5,
                fieldHeight = 100,
                errorText = state.descriptionArError?.let { stringResource(it) },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Description EN ──
            DefaultTextField(
                value = state.descriptionEn,
                onValueChanged = {
                    viewModel.updateState { copy(descriptionEn = it, descriptionEnError = null) }
                },
                placeholderText = stringResource(Res.string.description_en),
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                maxLines = 8,
                minLines = 5,
                fieldHeight = 100,
                errorText = state.descriptionEnError?.let { stringResource(it) },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ── Product Images ──
            ImageGridSection(
                images = state.images,
                errorText = state.imagesError?.let { stringResource(it) },
                onAddImages = {
                    if (state.images.size < MAX_IMAGES) pickImages()
                },
                onRemoveImage = { viewModel.removeImage(it) },
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ── Product Video ──
            VideoSection(
                hasVideo = state.video != null,
                onPickVideo = pickVideo,
                onRemoveVideo = { viewModel.removeVideo() },
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ── Category Selector ──
            val categoryLabel = state.selectedCategories.joinToString(", ") { it.name }
            DefaultTextField(
                value = categoryLabel,
                onValueChanged = {},
                placeholderText = stringResource(Res.string.select_category),
                isEnabled = false,
                onClick = { showCategorySheet = true },
                errorText = state.categoryError?.let { stringResource(it) },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Category,
                        null,
                        tint = MahalatkTheme.hint,
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = MahalatkTheme.hint,
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Sub-Category Selector ──
            val subCategoryLabel = state.selectedSubCategories.joinToString(", ") { it.name }
            DefaultTextField(
                value = subCategoryLabel,
                onValueChanged = {},
                placeholderText = stringResource(Res.string.select_sub_category),
                isEnabled = false,
                onClick = { showSubCategorySheet = true },
                errorText = state.subCategoryError?.let { stringResource(it) },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Category,
                        null,
                        tint = MahalatkTheme.hint,
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = MahalatkTheme.hint,
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Price ──
            DefaultTextField(
                value = state.price,
                onValueChanged = {
                    viewModel.updateState { copy(price = it, priceError = null) }
                },
                placeholderText = stringResource(Res.string.price),
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done,
                errorText = state.priceError?.let { stringResource(it) },
                leadingIcon = {
                    Icon(
                        Icons.Filled.AttachMoney,
                        null,
                        tint = MahalatkTheme.hint,
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Discount ──
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(Res.string.discount),
                    style = MahalatkTheme.titleSmall,
                    color = AppColor.TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    DiscountType.entries.forEach { type ->
                        val label = when (type) {
                            DiscountType.NONE -> stringResource(Res.string.no_discount)
                            DiscountType.FIXED_PRICE -> stringResource(Res.string.fixed_price_discount)
                            DiscountType.PERCENTAGE -> stringResource(Res.string.percentage_discount)
                        }
                        Row(
                            modifier = Modifier
                                .noRippleClickable { viewModel.setDiscountType(type) }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = if (state.discountType == type) Icons.Filled.RadioButtonChecked
                                else Icons.Filled.RadioButtonUnchecked,
                                contentDescription = null,
                                tint = if (state.discountType == type) AppColor.Primary else AppColor.TextHint,
                                modifier = Modifier.size(20.dp),
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = label,
                                style = MahalatkTheme.bodySmall,
                                color = if (state.discountType == type) AppColor.TextPrimary else AppColor.TextHint,
                            )
                        }
                    }
                }
            }

            if (state.discountType != DiscountType.NONE) {
                Spacer(modifier = Modifier.height(16.dp))

                // ── Discount Value ──
                DefaultTextField(
                    value = state.discountValue,
                    onValueChanged = {
                        viewModel.updateState { copy(discountValue = it, discountError = null) }
                    },
                    placeholderText = stringResource(Res.string.discount_value),
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done,
                    errorText = state.discountError?.let { stringResource(it) },
                    leadingIcon = {
                        Icon(
                            if (state.discountType == DiscountType.PERCENTAGE) Icons.Filled.Percent
                            else Icons.Filled.AttachMoney,
                            null,
                            tint = MahalatkTheme.hint,
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Add Product Button ──
            DefaultButton(
                text = stringResource(Res.string.add_product),
                onClick = { viewModel.addProduct() },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // ── Bottom Sheets ──

    MultiSelectBottomSheet(
        showBottomSheet = showCategorySheet,
        title = stringResource(Res.string.select_category),
        items = state.availableCategories,
        selectedItems = state.selectedCategories.toSet(),
        itemLabel = { it.name },
        onItemToggle = { viewModel.toggleCategory(it) },
        onDismiss = { showCategorySheet = false },
    )

    MultiSelectBottomSheet(
        showBottomSheet = showSubCategorySheet,
        title = stringResource(Res.string.select_sub_category),
        items = state.availableSubCategories,
        selectedItems = state.selectedSubCategories.toSet(),
        itemLabel = { it.name },
        onItemToggle = { viewModel.toggleSubCategory(it) },
        onDismiss = { showSubCategorySheet = false },
    )

}

// ──────────────────────────────────────────────
// Image Grid Section
// ──────────────────────────────────────────────
@Composable
private fun ImageGridSection(
    images: List<ByteArray>,
    errorText: String?,
    onAddImages: () -> Unit,
    onRemoveImage: (Int) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(Res.string.product_images),
                style = MahalatkTheme.titleSmall,
                color = AppColor.TextPrimary,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "${images.size} / $MAX_IMAGES",
                style = MahalatkTheme.bodySmall,
                color = AppColor.TextHint,
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // +1 for the sticky attach box at position 0
        val totalItems = images.size + 1
        val rows = (totalItems + 2) / 3
        val gridHeight = (rows * 110).dp

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .height(gridHeight),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            userScrollEnabled = false,
        ) {
            // Sticky dashed attach box at first position
            item {
                DashedAttachBox(
                    icon = Icons.Filled.Image,
                    label = stringResource(Res.string.add_images),
                    onClick = { if (images.size < MAX_IMAGES) onAddImages() },
                )
            }

            // Picked images
            itemsIndexed(images) { index, imageBytes ->
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp)),
                ) {
                    val bitmap = imageBytes.toImageBitmap()
                    if (bitmap != null) {
                        Image(
                            painter = BitmapPainter(bitmap),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop,
                        )
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .size(24.dp)
                            .background(AppColor.Error.copy(alpha = 0.8f), CircleShape)
                            .noRippleClickable { onRemoveImage(index) },
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            Icons.Filled.Close,
                            null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp),
                        )
                    }
                }
            }
        }

        if (errorText != null) {
            Text(
                text = errorText,
                style = MahalatkTheme.bodySmall,
                color = MahalatkTheme.error,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}

// ──────────────────────────────────────────────
// Video Section
// ──────────────────────────────────────────────
@Composable
private fun VideoSection(
    hasVideo: Boolean,
    onPickVideo: () -> Unit,
    onRemoveVideo: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(Res.string.product_video),
            style = MahalatkTheme.titleSmall,
            color = AppColor.TextPrimary,
            fontWeight = FontWeight.SemiBold,
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            // Sticky dashed attach box
            DashedAttachBox(
                icon = Icons.Filled.Videocam,
                label = stringResource(Res.string.add_video),
                onClick = { if (!hasVideo) onPickVideo() },
            )

            // Video thumbnail with remove
            if (hasVideo) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(AppColor.Primary.copy(alpha = 0.08f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.Filled.Videocam,
                        null,
                        tint = AppColor.Primary,
                        modifier = Modifier.size(36.dp),
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .size(24.dp)
                            .background(AppColor.Error.copy(alpha = 0.8f), CircleShape)
                            .noRippleClickable { onRemoveVideo() },
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            Icons.Filled.Close,
                            null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp),
                        )
                    }
                }
            }
        }
    }
}
