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

sealed interface BottomNavItem {
    val title: StringResource
    val icon: DrawableResource
    val iconSelected: DrawableResource
    val route: Route

    data class Home(
        override val title: StringResource = Res.string.home,
        override val icon: DrawableResource = Res.drawable.ic_nav_home,
        override val iconSelected: DrawableResource = Res.drawable.ic_nav_home_selected,
        override val route: Route = Route.Home,
    ) : BottomNavItem

    data class Parts(
        override val title: StringResource = Res.string.parts,
        override val icon: DrawableResource = Res.drawable.ic_nav_parts,
        override val iconSelected: DrawableResource = Res.drawable.ic_nav_parts_selected,
        override val route: Route = Route.Parts,
    ) : BottomNavItem

    data class More(
        override val title: StringResource = Res.string.more,
        override val icon: DrawableResource = Res.drawable.ic_nav_more,
        override val iconSelected: DrawableResource = Res.drawable.ic_nav_more_selected,
        override val route: Route = Route.More,
    ) : BottomNavItem

    companion object {
        val items = listOf(Home(), Parts(), More())
    }
}

@Composable
fun AppBottomBar(
    currentRoute: Route?,
    onItemSelected: (BottomNavItem) -> Unit,
) {
    val selectedIndex = remember(currentRoute) {
        BottomNavItem.items.indexOfFirst {
            it.route::class == currentRoute?.let { r -> r::class }
        }.coerceAtLeast(0)
    }

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
            BottomNavItem.items.forEachIndexed { index, item ->
                val isSelected = index == selectedIndex
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
                            if (!isSelected) {
                                onItemSelected(item)
                            }
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        painter = painterResource(
                            if (isSelected) item.iconSelected else item.icon
                        ),
                        contentDescription = stringResource(item.title),
                        tint = color,
                        modifier = Modifier.size(24.dp),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(item.title),
                        color = color,
                        fontWeight = fontWeight,
                        fontSize = 14.sp,
                    )
                }
            }
        }
    }
}
