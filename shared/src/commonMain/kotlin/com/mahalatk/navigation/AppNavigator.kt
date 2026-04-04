package com.mahalatk.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList

/**
 * App-level navigator that owns the back stack.
 *
 * **Tab screens** are tracked via [currentTab] — switching tabs only changes
 * this state variable, so NavDisplay never destroys/recreates tab screens.
 *
 * **Detail screens** are pushed onto [backStack] normally and managed by NavDisplay.
 */
@Stable
class AppNavigator(initialRoute: Route = Route.Splash) {

    /** The back stack — NavDisplay reads this directly. */
    val backStack: SnapshotStateList<Route> = mutableStateListOf(initialRoute)

    /** Currently selected tab — survives tab switches without backStack churn. */
    var currentTab: Route by mutableStateOf(Route.Home)
        private set

    /** The top-most route on the back stack. */
    val currentRoute: Route
        get() = backStack.last()

    val canGoBack: Boolean
        get() = backStack.size > 1

    // ─── Stack Operations ───────────────────────────

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
        if (route.isTabRoute) currentTab = route
    }

    fun popUntil(predicate: (Route) -> Boolean) {
        while (backStack.size > 1 && !predicate(backStack.last())) {
            backStack.removeAt(backStack.lastIndex)
        }
    }

    // ─── Tab Switching ──────────────────────────────

    /**
     * Switch between the 5 main tabs **without** touching the backStack entry.
     *
     * 1. Pop any detail screens above the tab level.
     * 2. Update [currentTab] — MainNavGraph reads this to show/hide tabs.
     * 3. Update the backStack entry so NavDisplay and `currentRoute` stay in sync.
     *
     * Because the backStack entry type stays a tab Route, NavDisplay reuses
     * the same NavEntry → no destroy/recreate, no lag.
     */
    fun switchTab(route: Route) {
        if (currentTab == route) return

        // Pop detail screens back to tab level
        val tabIndex = backStack.indexOfFirst { it.isTabRoute }
        if (tabIndex >= 0) {
            while (backStack.size > tabIndex + 1) {
                backStack.removeAt(backStack.lastIndex)
            }
            backStack[tabIndex] = route
        } else {
            backStack.clear()
            backStack.add(route)
        }

        currentTab = route
    }
}

// ─── CompositionLocal ───────────────────────────────

val LocalNavigator = compositionLocalOf<AppNavigator> {
    error("No AppNavigator provided. Wrap your content with CompositionLocalProvider.")
}

@Composable
fun rememberAppNavigator(initialRoute: Route = Route.Splash): AppNavigator {
    return remember { AppNavigator(initialRoute) }
}
