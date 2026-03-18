package com.aait.base.common.component.bottomNavigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.aait.base.ui.Screens
import com.mahalatak.R

sealed class BottomNavItem(
    @param:StringRes val name: Int,
    val route: Screens,
    @param:DrawableRes val icon: Int,
    @param:DrawableRes val iconSelected: Int,
    val isBadgeEnabled: Boolean = false,
) {

    data object Home : BottomNavItem(
        name = R.string.home,
        route = Screens.Login,
        icon = R.drawable.app_icon,
        iconSelected = R.drawable.app_icon
    )


    companion object {
        val bottomNavItems = listOf(Home)
    }
}
