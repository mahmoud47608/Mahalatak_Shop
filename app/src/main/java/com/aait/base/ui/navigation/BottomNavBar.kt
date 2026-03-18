package com.aait.base.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.mahalatak.R

sealed interface BottomAppBarModel {
    val title: Int
    val icon: Int
    val iconSelected: Int
    val navScreen: NavScreen

    data class Home(
        override val title: Int = R.string.home,
        override val icon: Int = R.drawable.app_icon,
        override val iconSelected: Int = R.drawable.app_icon,
        override val navScreen: NavScreen = HomeNavKey
    ) : BottomAppBarModel
    data class More(
        override val title: Int = R.string.more,
        override val icon: Int = R.drawable.app_icon,
        override val iconSelected: Int = R.drawable.app_icon,
        override val navScreen: NavScreen = MoreNavKey()
    ) : BottomAppBarModel
}
@Composable
fun BottomAppBar(
    currentKey: NavScreen?,
    items: List<BottomAppBarModel>,
    onItemSelected: (BottomAppBarModel) -> Unit
) {
    val selectedIndex = remember(currentKey) {
        items.indexOfFirst {
            it.navScreen::class == currentKey?.let { key -> key::class }
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
                        painter = painterResource(if (selectedIndex == index) item.iconSelected else item.icon),
                        contentDescription = stringResource(item.title)
                    )
                },
                label = {
                    Text(text = stringResource(item.title))
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
