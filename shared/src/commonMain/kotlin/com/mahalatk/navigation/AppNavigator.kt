package com.mahalatk.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList

/**
 * Nav3-style navigator — you own the back stack.
 *
 * The [backStack] is a [SnapshotStateList] passed directly to NavDisplay.
 * NavDisplay + entryDecorators keep ViewModel & saveable state alive
 * for every entry that stays on the stack.
 */
@Stable
class AppNavigator(initialRoute: Route = Route.Splash) {

    /** The back stack — Nav3 NavDisplay reads this directly. */
    val backStack: SnapshotStateList<Route> = mutableStateListOf(initialRoute)

    val currentRoute: Route
        get() = backStack.last()

    val canGoBack: Boolean
        get() = backStack.size > 1

    fun push(route: Route) {
        if (backStack.last() == route) return
        backStack.add(route)
    }

    fun pop(): Boolean {
        if (!canGoBack) return false
        backStack.removeAt(backStack.lastIndex)
        return true
    }

    fun replace(route: Route) {
        backStack[backStack.lastIndex] = route
    }

    fun replaceAll(route: Route) {
        backStack.clear()
        backStack.add(route)
    }

    /**
     * Switch bottom-nav tab without destroying other tab states.
     * Keeps only the first entry (or replaces it) so NavDisplay
     * can still hold saveable state for previously visited tabs.
     */
    fun switchTab(route: Route) {
        if (backStack.lastOrNull() == route) return
        // Pop any detail screens back to the tab level, then swap the tab
        val tabIndex =
            backStack.indexOfFirst { it is Route.Home || it is Route.Products || it is Route.Orders || it is Route.Chat || it is Route.Account }
        if (tabIndex >= 0) {
            // Remove everything above the tab entry
            while (backStack.size > tabIndex + 1) {
                backStack.removeAt(backStack.lastIndex)
            }
            // Replace the tab
            backStack[tabIndex] = route
        } else {
            backStack.clear()
            backStack.add(route)
        }
    }

    fun popUntil(predicate: (Route) -> Boolean) {
        while (backStack.size > 1 && !predicate(backStack.last())) {
            backStack.removeAt(backStack.lastIndex)
        }
    }
}

/**
 * Access the navigator from any composable without lambda drilling.
 * Usage: val navigator = LocalNavigator.current
 */
val LocalNavigator = compositionLocalOf<AppNavigator> {
    error("No AppNavigator provided. Wrap your content with CompositionLocalProvider.")
}

@Composable
fun rememberAppNavigator(initialRoute: Route = Route.Splash): AppNavigator {
    return remember { AppNavigator(initialRoute) }
}
