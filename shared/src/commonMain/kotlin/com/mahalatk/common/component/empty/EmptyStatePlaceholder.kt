package com.mahalatk.common.component.empty

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.MahalatkTheme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/**
 * Reusable empty state placeholder with icon in a circle and a message.
 */
@Composable
fun EmptyStatePlaceholder(
    icon: DrawableResource,
    message: String,
    modifier: Modifier = Modifier,
    iconBackgroundColor: Color = AppColor.Primary.copy(alpha = 0.08f),
    iconTint: Color? = null,
    circleSize: Dp = 80.dp,
    iconSize: Dp = 40.dp,
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(circleSize)
                    .background(
                        brush = Brush.radialGradient(
                            listOf(
                                iconBackgroundColor,
                                iconBackgroundColor.copy(alpha = 0.02f),
                            )
                        ),
                        shape = CircleShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                if (iconTint != null) {
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = null,
                        modifier = Modifier.size(iconSize),
                        tint = iconTint,
                    )
                } else {
                    Image(
                        painter = painterResource(icon),
                        contentDescription = null,
                        modifier = Modifier.size(iconSize),
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MahalatkTheme.bodyLarge,
                color = AppColor.TextHint,
                textAlign = TextAlign.Center,
            )
        }
    }
}
