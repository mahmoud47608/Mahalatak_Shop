package com.aait.ui.navigation


import androidx.compose.foundation.Image
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import mahalatak.shared.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

sealed interface BottomAppBarModel {
    val title: StringResource
    val icon: DrawableResource
    val iconSelected: DrawableResource
    val navScreen: NavScreen

    data class Home(
        override val title: StringResource = Res.string.home,
        override val icon: DrawableResource = Res.drawable.app_icon,
        override val iconSelected: DrawableResource = Res.drawable.app_icon,
        override val navScreen: NavScreen = HomeNavKey
    ) : BottomAppBarModel

    data class More(
        override val title: StringResource = Res.string.more,
        override val icon: DrawableResource = Res.drawable.app_icon,
        override val iconSelected: DrawableResource = Res.drawable.app_icon,
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
