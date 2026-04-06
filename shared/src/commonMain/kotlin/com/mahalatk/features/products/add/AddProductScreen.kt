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
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.bottomsheet.SingleSelectBottomSheet
import com.mahalatk.common.component.bottomsheet.SuccessBottomSheet
import com.mahalatk.common.component.button.ButtonStyle
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
import mahalatk.shared.generated.resources.add_piece
import mahalatk.shared.generated.resources.add_product
import mahalatk.shared.generated.resources.add_video
import mahalatk.shared.generated.resources.confirm_finish
import mahalatk.shared.generated.resources.continue_adding_piece
import mahalatk.shared.generated.resources.description
import mahalatk.shared.generated.resources.discount
import mahalatk.shared.generated.resources.discount_value
import mahalatk.shared.generated.resources.finish
import mahalatk.shared.generated.resources.fixed_price_discount
import mahalatk.shared.generated.resources.no_discount
import mahalatk.shared.generated.resources.percentage_discount
import mahalatk.shared.generated.resources.pieces_count
import mahalatk.shared.generated.resources.price
import mahalatk.shared.generated.resources.product_added_success
import mahalatk.shared.generated.resources.product_attributes
import mahalatk.shared.generated.resources.product_images
import mahalatk.shared.generated.resources.product_name_ar
import mahalatk.shared.generated.resources.product_name_en
import mahalatk.shared.generated.resources.product_video
import mahalatk.shared.generated.resources.quantity
import mahalatk.shared.generated.resources.select_category
import mahalatk.shared.generated.resources.select_color
import mahalatk.shared.generated.resources.select_season
import mahalatk.shared.generated.resources.select_size
import mahalatk.shared.generated.resources.select_sub_category
import mahalatk.shared.generated.resources.warning_piece_not_added
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

private const val MAX_IMAGES = 6

@Composable
fun AddProductScreen(
    viewModel: AddProductViewModel = koinViewModel(),
    onBack: () -> Unit = {},
) {
    val state by viewModel.uiState.collectAsState()
    var showSeasonSheet by remember { mutableStateOf(false) }
    var showCategorySheet by remember { mutableStateOf(false) }
    var showSubCategorySheet by remember { mutableStateOf(false) }
    var showColorSheet by remember { mutableStateOf(false) }
    var showSizeSheet by remember { mutableStateOf(false) }

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
            // ════════════════════════════════════════════
            // PART 1: Product Info
            // ════════════════════════════════════════════

            val part1Locked = state.isPart1Locked

            // ── Category Selector (Single Select) ──
            val categoryLabel = state.selectedCategory?.name ?: ""
            DefaultTextField(
                value = categoryLabel,
                onValueChanged = {},
                placeholderText = stringResource(Res.string.select_category),
                isEnabled = false,
                onClick = { if (!part1Locked) showCategorySheet = true },
                errorText = state.categoryError?.let { stringResource(it) },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Category,
                        null,
                        tint = MahalatkTheme.primary,
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = MahalatkTheme.primary,
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Sub-Category Selector (Single Select, depends on Category) ──
            val subCategoryLabel = state.selectedSubCategory?.name ?: ""
            DefaultTextField(
                value = subCategoryLabel,
                onValueChanged = {},
                placeholderText = stringResource(Res.string.select_sub_category),
                isEnabled = false,
                onClick = {
                    if (!part1Locked && state.selectedCategory != null) showSubCategorySheet = true
                },
                errorText = state.subCategoryError?.let { stringResource(it) },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Category,
                        null,
                        tint = MahalatkTheme.primary,
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = MahalatkTheme.primary,
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            )

            // ── Season Selector (only for non-shoes categories) ──
            val showSeason = state.selectedCategory?.id != 4
            if (showSeason) {
                Spacer(modifier = Modifier.height(16.dp))

                DefaultTextField(
                    value = state.selectedSeason,
                    onValueChanged = {},
                    placeholderText = stringResource(Res.string.select_season),
                    isEnabled = false,
                    onClick = { if (!part1Locked) showSeasonSheet = true },
                    errorText = state.seasonError?.let { stringResource(it) },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowDown,
                            contentDescription = null,
                            tint = MahalatkTheme.primary,
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Product Name AR ──
            DefaultTextField(
                value = state.nameAr,
                onValueChanged = {
                    if (!part1Locked) viewModel.updateState {
                        copy(
                            nameAr = it,
                            nameArError = null
                        )
                    }
                },
                placeholderText = stringResource(Res.string.product_name_ar),
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                isEnabled = !part1Locked,
                errorText = state.nameArError?.let { stringResource(it) },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Product Name EN ──
            DefaultTextField(
                value = state.nameEn,
                onValueChanged = {
                    if (!part1Locked) viewModel.updateState {
                        copy(
                            nameEn = it,
                            nameEnError = null
                        )
                    }
                },
                placeholderText = stringResource(Res.string.product_name_en),
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                isEnabled = !part1Locked,
                errorText = state.nameEnError?.let { stringResource(it) },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Description (single field) ──
            DefaultTextField(
                value = state.description,
                onValueChanged = {
                    if (!part1Locked) viewModel.updateState {
                        copy(
                            description = it,
                            descriptionError = null
                        )
                    }
                },
                placeholderText = stringResource(Res.string.description),
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                isEnabled = !part1Locked,
                maxLines = 8,
                minLines = 5,
                fieldHeight = 100,
                errorText = state.descriptionError?.let { stringResource(it) },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── Divider between Part 1 and Part 2 ──
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(
                        color = Color.LightGray.copy(alpha = if (AppColor.isDark) 0.5f else 1f)
                    )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ════════════════════════════════════════════
            // PART 2: Piece Attributes
            // ════════════════════════════════════════════

            Text(
                text = stringResource(Res.string.product_attributes),
                style = MahalatkTheme.titleSmall,
                color = AppColor.TextPrimary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth(),
            )

            if (state.pieces.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(Res.string.pieces_count, state.pieces.size),
                    style = MahalatkTheme.bodySmall,
                    color = AppColor.Success,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Color Selector (Single Select) ──
            DefaultTextField(
                value = state.color,
                onValueChanged = {},
                placeholderText = stringResource(Res.string.select_color),
                isEnabled = false,
                onClick = { showColorSheet = true },
                errorText = state.colorError?.let { stringResource(it) },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = MahalatkTheme.primary,
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Size Selector (Single Select) ──
            DefaultTextField(
                value = state.size,
                onValueChanged = {},
                placeholderText = stringResource(Res.string.select_size),
                isEnabled = false,
                onClick = { showSizeSheet = true },
                errorText = state.sizeError?.let { stringResource(it) },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = MahalatkTheme.primary,
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Quantity ──
            DefaultTextField(
                value = state.quantity,
                onValueChanged = {
                    viewModel.updateState { copy(quantity = it, quantityError = null) }
                },
                placeholderText = stringResource(Res.string.quantity),
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
                errorText = state.quantityError?.let { stringResource(it) },
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
                        tint = MahalatkTheme.primary,
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
                            tint = MahalatkTheme.primary,
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

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

            Spacer(modifier = Modifier.height(24.dp))

            // ── Add Piece Button ──
            DefaultButton(
                text = stringResource(Res.string.add_piece),
                onClick = { viewModel.addPiece() },
                style = ButtonStyle.OUTLINED,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ── Finish Button ──
            DefaultButton(
                text = stringResource(Res.string.finish),
                onClick = { viewModel.onFinishClicked() },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // ── Bottom Sheets ──

    SingleSelectBottomSheet(
        showBottomSheet = showSeasonSheet,
        title = stringResource(Res.string.select_season),
        items = state.availableSeasons,
        selectedItem = state.selectedSeason.ifBlank { null },
        itemLabel = { it },
        onItemSelected = { viewModel.selectSeason(it) },
        onDismiss = { showSeasonSheet = false },
    )

    SingleSelectBottomSheet(
        showBottomSheet = showCategorySheet,
        title = stringResource(Res.string.select_category),
        items = state.availableCategories,
        selectedItem = state.selectedCategory,
        itemLabel = { it.name },
        onItemSelected = { viewModel.selectCategory(it) },
        onDismiss = { showCategorySheet = false },
        isItemSelected = { item, selected -> item?.id == selected?.id },
    )

    SingleSelectBottomSheet(
        showBottomSheet = showSubCategorySheet,
        title = stringResource(Res.string.select_sub_category),
        items = state.availableSubCategories,
        selectedItem = state.selectedSubCategory,
        itemLabel = { it.name },
        onItemSelected = { viewModel.selectSubCategory(it) },
        onDismiss = { showSubCategorySheet = false },
        isItemSelected = { item, selected -> item?.id == selected?.id },
    )

    SingleSelectBottomSheet(
        showBottomSheet = showColorSheet,
        title = stringResource(Res.string.select_color),
        items = state.availableColors,
        selectedItem = state.color.ifBlank { null },
        itemLabel = { it },
        onItemSelected = { viewModel.selectColor(it) },
        onDismiss = { showColorSheet = false },
    )

    SingleSelectBottomSheet(
        showBottomSheet = showSizeSheet,
        title = stringResource(Res.string.select_size),
        items = state.availableSizes,
        selectedItem = state.size.ifBlank { null },
        itemLabel = { it },
        onItemSelected = { viewModel.selectSize(it) },
        onDismiss = { showSizeSheet = false },
    )

    // ── Warning Dialog: pending piece data ──
    if (state.showWarningDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissWarningDialog() },
            title = null,
            text = {
                Text(
                    text = stringResource(Res.string.warning_piece_not_added),
                    style = MahalatkTheme.bodyMedium,
                    color = AppColor.TextPrimary,
                )
            },
            confirmButton = {
                TextButton(onClick = { viewModel.confirmFinishAnyway() }) {
                    Text(
                        text = stringResource(Res.string.confirm_finish),
                        color = AppColor.Error,
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissWarningDialog() }) {
                    Text(
                        text = stringResource(Res.string.continue_adding_piece),
                        color = AppColor.Primary,
                    )
                }
            },
        )
    }

    // ── Product Added Success ──
    SuccessBottomSheet(
        message = stringResource(Res.string.product_added_success),
        visible = state.showSuccessSheet,
        onDismiss = {
            viewModel.dismissSuccessSheet()
            onBack()
        },
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
                    val bitmap = remember(imageBytes) { imageBytes.toImageBitmap() }
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
                            .background(
                                brush = Brush.linearGradient(
                                    listOf(
                                        AppColor.Error.copy(alpha = 0.7f),
                                        AppColor.Error.copy(alpha = 0.9f)
                                    )
                                ),
                                shape = CircleShape,
                            )
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
                            .background(
                                brush = Brush.linearGradient(
                                    listOf(
                                        AppColor.Error.copy(alpha = 0.7f),
                                        AppColor.Error.copy(alpha = 0.9f)
                                    )
                                ),
                                shape = CircleShape,
                            )
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
