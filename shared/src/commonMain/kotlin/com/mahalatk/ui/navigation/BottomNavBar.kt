package com.mahalatk.ui.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahalatk.ui.theme.AppColor
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.home
import mahalatk.shared.generated.resources.ic_nav_home
import mahalatk.shared.generated.resources.ic_nav_home_selected
import mahalatk.shared.generated.resources.ic_nav_more
import mahalatk.shared.generated.resources.ic_nav_more_selected
import mahalatk.shared.generated.resources.ic_nav_parts
import mahalatk.shared.generated.resources.ic_nav_parts_selected
import mahalatk.shared.generated.resources.more
import mahalatk.shared.generated.resources.parts
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * Bottom navigation items.
 * data object (not data class) → one instance, no allocation per render.
 */
enum class BottomNavItem(
    val icon: DrawableResource,
    val iconSelected: DrawableResource,
    val label: StringResource,
    val route: Route,
) {
    Home(
        icon = Res.drawable.ic_nav_home,
        iconSelected = Res.drawable.ic_nav_home_selected,
        label = Res.string.home,
        route = Route.Home,
    ),
    Parts(
        icon = Res.drawable.ic_nav_parts,
        iconSelected = Res.drawable.ic_nav_parts_selected,
        label = Res.string.parts,
        route = Route.Parts,
    ),
    More(
        icon = Res.drawable.ic_nav_more,
        iconSelected = Res.drawable.ic_nav_more_selected,
        label = Res.string.more,
        route = Route.More,
    );

    companion object {
        fun fromRoute(route: Route): BottomNavItem? = when (route) {
            is Route.Home -> Home
            is Route.Parts -> Parts
            is Route.More -> More
            else -> null
        }
    }
}

@Composable
fun AppBottomBar(
    currentRoute: Route,
    onItemSelected: (BottomNavItem) -> Unit,
) {
    val selectedItem = BottomNavItem.fromRoute(currentRoute)

    Surface(
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        shadowElevation = 10.dp,
        color = Color.White,
        tonalElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            BottomNavItem.entries.forEach { item ->
                val isSelected = item == selectedItem
                val color = if (isSelected) AppColor.Primary else AppColor.NavInactive
                val fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                        ) {
                            if (!isSelected) onItemSelected(item)
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        painter = painterResource(if (isSelected) item.iconSelected else item.icon),
                        contentDescription = stringResource(item.label),
                        tint = color,
                        modifier = Modifier.size(24.dp),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(item.label),
                        color = color,
                        fontWeight = fontWeight,
                        fontSize = 14.sp,
                    )
                }
            }
        }
    }
}
