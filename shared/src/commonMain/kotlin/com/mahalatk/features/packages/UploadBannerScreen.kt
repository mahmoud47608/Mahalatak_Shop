package com.mahalatk.features.packages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.bottomsheet.SuccessBottomSheet
import com.mahalatk.common.component.button.DefaultButton
import com.mahalatk.common.component.header.ScreenHeader
import com.mahalatk.common.component.inputs.DefaultTextField
import com.mahalatk.common.component.utilis.noRippleClickable
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.banner_description
import mahalatk.shared.generated.resources.banner_type_cart
import mahalatk.shared.generated.resources.banner_type_home
import mahalatk.shared.generated.resources.banner_type_more
import mahalatk.shared.generated.resources.banner_uploaded_success
import mahalatk.shared.generated.resources.image_selected
import mahalatk.shared.generated.resources.select_image
import mahalatk.shared.generated.resources.upload_banner
import org.jetbrains.compose.resources.stringResource

@Composable
fun UploadBannerScreen(
    bannerType: String,
    onBack: () -> Unit = {},
) {
    var description by remember { mutableStateOf("") }
    var imageSelected by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }

    val title = when (bannerType) {
        "home_slider" -> stringResource(Res.string.banner_type_home)
        "more_slider" -> stringResource(Res.string.banner_type_more)
        "cart_slider" -> stringResource(Res.string.banner_type_cart)
        else -> stringResource(Res.string.upload_banner)
    }

    Column(
        modifier = Modifier.fillMaxSize().background(AppColor.ScreenBackground),
    ) {
        ScreenHeader(
            title = title,
            onBackClick = onBack,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 20.dp),
        ) {
            // Image picker area
            Text(
                text = stringResource(Res.string.select_image),
                style = MahalatkTheme.titleSmall,
                color = AppColor.TextPrimary,
                fontWeight = FontWeight.SemiBold,
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(AppColor.PrimaryContainer.copy(alpha = 0.3f))
                    .border(
                        width = 1.dp,
                        color = AppColor.Primary.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(16.dp),
                    )
                    .noRippleClickable { imageSelected = true },
                contentAlignment = Alignment.Center,
            ) {
                if (imageSelected) {
                    Text(
                        text = stringResource(Res.string.image_selected),
                        style = MahalatkTheme.bodyMedium,
                        color = AppColor.Primary,
                        fontWeight = FontWeight.Medium,
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = null,
                            tint = AppColor.Primary.copy(alpha = 0.5f),
                            modifier = Modifier.size(40.dp),
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(Res.string.select_image),
                            style = MahalatkTheme.bodySmall,
                            color = AppColor.TextHint,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Description
            Text(
                text = stringResource(Res.string.banner_description),
                style = MahalatkTheme.titleSmall,
                color = AppColor.TextPrimary,
                fontWeight = FontWeight.SemiBold,
            )

            Spacer(modifier = Modifier.height(12.dp))

            DefaultTextField(
                value = description,
                onValueChanged = { description = it },
                placeholderText = stringResource(Res.string.banner_description),
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3,
                minLines = 2,
            )

            Spacer(modifier = Modifier.height(24.dp))

            DefaultButton(
                text = stringResource(Res.string.upload_banner),
                onClick = {
                    if (imageSelected) {
                        showSuccess = true
                    }
                },
                enabled = imageSelected,
            )
        }
    }

    SuccessBottomSheet(
        visible = showSuccess,
        message = stringResource(Res.string.banner_uploaded_success),
        onDismiss = {
            showSuccess = false
            onBack()
        },
    )
}
