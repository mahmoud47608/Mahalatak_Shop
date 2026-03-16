package com.aait.ui.component.bottomNavigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val iconSelected: ImageVector,
    val isBadgeEnabled: Boolean = false,
) {
    data object Home : BottomNavItem(
        label = "home",
        icon = Icons.Outlined.Home,
        iconSelected = Icons.Filled.Home
    )

    companion object {
        val bottomNavItems = listOf(Home)
    }
}
