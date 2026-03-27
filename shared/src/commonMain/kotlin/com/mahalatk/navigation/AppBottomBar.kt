package com.mahalatk.navigation

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.account
import mahalatk.shared.generated.resources.chat
import mahalatk.shared.generated.resources.home
import mahalatk.shared.generated.resources.ic_nav_chat
import mahalatk.shared.generated.resources.ic_nav_chat_selected
import mahalatk.shared.generated.resources.ic_nav_home
import mahalatk.shared.generated.resources.ic_nav_home_selected
import mahalatk.shared.generated.resources.ic_nav_more
import mahalatk.shared.generated.resources.ic_nav_more_selected
import mahalatk.shared.generated.resources.ic_nav_orders
import mahalatk.shared.generated.resources.ic_nav_orders_selected
import mahalatk.shared.generated.resources.ic_nav_products
import mahalatk.shared.generated.resources.ic_nav_products_selected
import mahalatk.shared.generated.resources.orders
import mahalatk.shared.generated.resources.products
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * Bottom navigation items.
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
    Products(
        icon = Res.drawable.ic_nav_products,
        iconSelected = Res.drawable.ic_nav_products_selected,
        label = Res.string.products,
        route = Route.Products,
    ),
    Orders(
        icon = Res.drawable.ic_nav_orders,
        iconSelected = Res.drawable.ic_nav_orders_selected,
        label = Res.string.orders,
        route = Route.Orders,
    ),
    Chat(
        icon = Res.drawable.ic_nav_chat,
        iconSelected = Res.drawable.ic_nav_chat_selected,
        label = Res.string.chat,
        route = Route.Chat,
    ),
    Account(
        icon = Res.drawable.ic_nav_more,
        iconSelected = Res.drawable.ic_nav_more_selected,
        label = Res.string.account,
        route = Route.Account,
    );

    companion object {
        fun fromRoute(route: Route): BottomNavItem? = when (route) {
            is Route.Home -> Home
            is Route.Products -> Products
            is Route.Orders -> Orders
            is Route.Chat -> Chat
            is Route.Account -> Account
            else -> null
        }
    }
}

@Composable
fun AppBottomBar(
    onItemSelected: (BottomNavItem) -> Unit,
) {
    val selectedItem = BottomNavItem.fromRoute(LocalNavigator.current.currentRoute)

    Surface(
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        shadowElevation = 10.dp,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
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
                        .noRippleClickable {
                            if (!isSelected) onItemSelected(item)
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        painter = painterResource(if (isSelected) item.iconSelected else item.icon),
                        contentDescription = stringResource(item.label),
                        tint = Color.Unspecified,
                        modifier = Modifier.size(24.dp),
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(item.label),
                        color = color,
                        fontWeight = fontWeight,
                        style = MahalatkTheme.labelSmall,
                    )
                }
            }
        }
    }
}
