package com.aait.common.component.bottomNavigation

import mahalatak.shared.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

sealed class BottomNavItem(
    val name: StringResource,
    val route: String,
    val icon: DrawableResource,
    val iconSelected: DrawableResource,
    val isBadgeEnabled: Boolean = false,
) {

    data object Home : BottomNavItem(
        name = Res.string.home,
        route = "home",
        icon = Res.drawable.app_icon,
        iconSelected = Res.drawable.app_icon
    )


    companion object {
        val bottomNavItems = listOf(Home)
    }
}
