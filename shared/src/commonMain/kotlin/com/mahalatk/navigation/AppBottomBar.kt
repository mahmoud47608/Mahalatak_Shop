package com.mahalatk.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntOffset
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
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

// ─── Data ───────────────────────────────────────────

enum class BottomNavItem(
    val icon: DrawableResource,
    val iconSelected: DrawableResource,
    val label: StringResource,
    val route: Route,
) {
    Home(Res.drawable.ic_nav_home, Res.drawable.ic_nav_home_selected, Res.string.home, Route.Home),
    Orders(
        Res.drawable.ic_nav_orders,
        Res.drawable.ic_nav_orders_selected,
        Res.string.orders,
        Route.Orders
    ),
    Products(
        Res.drawable.ic_nav_products,
        Res.drawable.ic_nav_products_selected,
        Res.string.products,
        Route.Products
    ),
    Chat(Res.drawable.ic_nav_chat, Res.drawable.ic_nav_chat_selected, Res.string.chat, Route.Chat),
    Account(
        Res.drawable.ic_nav_more,
        Res.drawable.ic_nav_more_selected,
        Res.string.account,
        Route.Account
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

// ─── Animated Bottom Bar ────────────────────────────

@Composable
fun AppBottomBar(onItemSelected: (BottomNavItem) -> Unit) {

    val items = BottomNavItem.entries
    val selectedItem = BottomNavItem.fromRoute(LocalNavigator.current.currentRoute)
    val selectedIndex = items.indexOf(selectedItem).coerceAtLeast(0)

    val circleRadius = 26.dp
    val layoutDirection = LocalLayoutDirection.current
    var barSize by remember { mutableStateOf(IntSize(0, 0)) }

    // Calculate offset for the floating circle
    val offsetStep = remember(barSize) {
        barSize.width.toFloat() / (items.size * 2)
    }
    val offset = remember(selectedIndex, offsetStep) {
        offsetStep + selectedIndex * 2 * offsetStep
    }
    val offsetWithLayout = remember(selectedIndex, offsetStep, layoutDirection) {
        if (layoutDirection == LayoutDirection.Rtl)
            offsetStep + (items.size - 1 - selectedIndex) * 2 * offsetStep
        else
            offsetStep + selectedIndex * 2 * offsetStep
    }

    val circleRadiusPx = LocalDensity.current.run { circleRadius.toPx().toInt() }
    val offsetTransition = updateTransition(offset, "offset")
    val offsetTransitionLayout = updateTransition(offsetWithLayout, "offsetLayout")
    val anim = spring<Float>(dampingRatio = 0.5f, stiffness = Spring.StiffnessVeryLow)

    // Cutout position (for the bar shape)
    val cutoutOffset by offsetTransitionLayout.animateFloat(
        transitionSpec = { if (initialState == 0f) snap() else anim },
        label = "cutout",
    ) { it }

    // Circle position
    val circleOffset by offsetTransition.animateIntOffset(
        transitionSpec = {
            if (initialState == 0f) snap()
            else spring(anim.dampingRatio, anim.stiffness)
        },
        label = "circle",
    ) { IntOffset(it.toInt() - circleRadiusPx, -circleRadiusPx) }

    val barShape = remember(cutoutOffset) {
        BarShape(offset = cutoutOffset, circleRadius = circleRadius, cornerRadius = 25.dp)
    }

    // ── Layout ──
    Box {
        // Floating circle with the selected icon
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .offset { circleOffset }
                .zIndex(1f)
                .size(circleRadius * 2)
                .clip(CircleShape)
                .background(AppColor.Primary),
        ) {
            AnimatedContent(
                targetState = items[selectedIndex].iconSelected,
                label = "circle icon",
            ) { icon ->
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp),
                )
            }
        }

        // Navigation bar with cutout
        Row(
            modifier = Modifier
                .onPlaced { barSize = it.size }
                .graphicsLayer { shape = barShape; clip = true }
                .fillMaxWidth()
                .height(64.dp)
                .background(Color.White),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = index == selectedIndex
                val iconAlpha by animateFloatAsState(
                    targetValue = if (isSelected) 0f else 1f,
                    label = "iconAlpha",
                )
                val color = if (isSelected) AppColor.Primary else AppColor.NavInactive

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .noRippleClickable { if (!isSelected) onItemSelected(item) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        painter = painterResource(if (isSelected) item.iconSelected else item.icon),
                        contentDescription = stringResource(item.label),
                        modifier = Modifier.alpha(iconAlpha).size(24.dp),
                        tint = Color.Unspecified,
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = stringResource(item.label),
                        color = color,
                        fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                        style = MahalatkTheme.labelSmall,
                    )
                }
            }
        }
    }
}

// ─── Bar Shape with animated cutout ─────────────────

private class BarShape(
    private val offset: Float,
    private val circleRadius: Dp,
    private val cornerRadius: Dp,
    private val circleGap: Dp = 5.dp,
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline =
        Outline.Generic(buildPath(size, density))

    private fun buildPath(size: Size, density: Density): Path {
        val cutoutCenterX = offset
        val cutoutR = density.run { (circleRadius + circleGap).toPx() }
        val cornerR = density.run { cornerRadius.toPx() }
        val cornerD = cornerR * 2
        val edge = cutoutR * 1.5f
        val left = cutoutCenterX - edge
        val right = cutoutCenterX + edge

        return Path().apply {
            // Bottom-left → Top-left corner
            moveTo(0f, size.height)
            if (left > 0) {
                val d = if (left >= cornerR) cornerD else left * 2
                arcTo(Rect(0f, 0f, d, d), 180f, 90f, false)
            }
            lineTo(left, 0f)

            // Cutout curve
            cubicTo(
                cutoutCenterX - cutoutR,
                0f,
                cutoutCenterX - cutoutR,
                cutoutR,
                cutoutCenterX,
                cutoutR
            )
            cubicTo(cutoutCenterX + cutoutR, cutoutR, cutoutCenterX + cutoutR, 0f, right, 0f)

            // Top-right corner → Bottom-right
            if (right < size.width) {
                val d = if (right <= size.width - cornerR) cornerD else (size.width - right) * 2
                arcTo(Rect(size.width - d, 0f, size.width, d), -90f, 90f, false)
            }
            lineTo(size.width, size.height)
            close()
        }
    }
}
