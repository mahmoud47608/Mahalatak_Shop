package com.mahalatk.common.component.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.utilis.noRippleClickable
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.MahalatkTheme
import com.mahalatk.theme.SpacingDimensions
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/**
 * Reusable menu item row with colored icon circle, title, and arrow.
 * Used in MoreScreen, SettingsScreen, EmployeesHubScreen.
 */
@Composable
fun MenuItemRow(
    icon: DrawableResource,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconColor: Color = AppColor.Primary,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(iconColor.copy(alpha = 0.12f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painterResource(icon), null,
                tint = iconColor,
                modifier = Modifier.size(16.dp),
            )
        }

        Spacer(modifier = Modifier.width(SpacingDimensions.sp2))

        Text(
            text = title,
            style = MahalatkTheme.bodyMedium,
            color = AppColor.TextPrimary,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f),
        )

        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight, null,
            tint = AppColor.TextHint,
            modifier = Modifier.size(18.dp),
        )
    }
}
