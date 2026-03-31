package com.mahalatk.common.component.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.mahalatk.theme.AppColor
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.ic_profile
import org.jetbrains.compose.resources.painterResource

/**
 * Reusable circular user avatar.
 * Priority: imageUrl > initials > fallback icon.
 */
@Composable
fun UserAvatar(
    imageUrl: String = "",
    initials: String = "",
    size: Dp = 48.dp,
    modifier: Modifier = Modifier,
    backgroundColor: Color = AppColor.Primary.copy(alpha = 0.1f),
    initialsColor: Color = AppColor.Primary,
    fontSize: TextUnit = (size.value * 0.4f).sp,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        when {
            imageUrl.isNotEmpty() -> {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
            }

            initials.isNotEmpty() -> {
                Text(
                    text = initials.take(1),
                    fontSize = fontSize,
                    fontWeight = FontWeight.Bold,
                    color = initialsColor,
                )
            }

            else -> {
                Image(
                    painter = painterResource(Res.drawable.ic_profile),
                    contentDescription = null,
                    modifier = Modifier
                        .size(size * 0.6f)
                        .padding(top = 4.dp),
                )
            }
        }
    }
}
