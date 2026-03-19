package com.mahalatk.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.app_icon
import mahalatk.shared.generated.resources.home
import mahalatk.shared.generated.resources.more
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
        override val icon: DrawableResource = Res.drawable.app_icon,
        override val iconSelected: DrawableResource = Res.drawable.app_icon,
        override val route: Route = Route.Home,
    ) : BottomNavItem

    data class More(
        override val title: StringResource = Res.string.more,
        override val icon: DrawableResource = Res.drawable.app_icon,
        override val iconSelected: DrawableResource = Res.drawable.app_icon,
        override val route: Route = Route.More,
    ) : BottomNavItem
}

val bottomNavItems = listOf(BottomNavItem.Home(), BottomNavItem.More())

@Composable
fun AppBottomBar(
    currentRoute: Route?,
    items: List<BottomNavItem> = bottomNavItems,
    onItemSelected: (BottomNavItem) -> Unit,
) {
    val selectedIndex = remember(currentRoute) {
        items.indexOfFirst {
            it.route::class == currentRoute?.let { r -> r::class }
        }.coerceAtLeast(0)
    }

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = index == selectedIndex,
                onClick = {
                    if (index != selectedIndex) {
                        onItemSelected(item)
                    }
                },
                icon = {
                    Image(
                        painter = painterResource(
                            if (selectedIndex == index) item.iconSelected else item.icon
                        ),
                        contentDescription = stringResource(item.title),
                    )
                },
                label = {
                    Text(text = stringResource(item.title))
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                ),
            )
        }
    }
}
