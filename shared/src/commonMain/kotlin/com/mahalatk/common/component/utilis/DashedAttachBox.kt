package com.mahalatk.common.component.utilis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.MahalatkTheme

@Composable
fun DashedAttachBox(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 100.dp,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(12.dp))
            .dashedBorder(1.5.dp, AppColor.Primary.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .background(AppColor.Primary.copy(alpha = 0.05f))
            .noRippleClickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                icon,
                null,
                tint = AppColor.Primary,
                modifier = Modifier.size(28.dp),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                style = MahalatkTheme.labelSmall,
                color = AppColor.Primary,
                fontSize = 10.sp,
            )
        }
    }
}

fun Modifier.dashedBorder(
    width: Dp,
    color: Color,
    shape: RoundedCornerShape,
) = this.then(
    Modifier.drawBehind {
        val stroke = Stroke(
            width = width.toPx(),
            pathEffect = PathEffect.dashPathEffect(
                floatArrayOf(10f, 10f),
                0f,
            ),
        )
        val outline = shape.createOutline(size, layoutDirection, this)
        drawOutline(outline, color, style = stroke)
    },
)
