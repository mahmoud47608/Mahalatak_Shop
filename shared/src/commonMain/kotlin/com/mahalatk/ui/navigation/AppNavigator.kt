package com.mahalatk.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember

/**
 * Simple back stack navigator - Nav3 philosophy: you own the list.
 *
 * No UUID, no NavEntry wrapper. Just a list of Routes.
 * koinViewModel() creates ONE instance per ViewModel type (no leak).
 */
@Stable
class AppNavigator(initialRoute: Route = Route.Splash) {

    private val _backStack = mutableStateListOf(initialRoute)

    val currentRoute: Route
        get() = _backStack.last()

    val canGoBack: Boolean
        get() = _backStack.size > 1

    fun push(route: Route) {
        _backStack.add(route)
    }

    fun pop(): Boolean {
        if (!canGoBack) return false
        _backStack.removeAt(_backStack.lastIndex)
        return true
    }

    fun replaceAll(route: Route) {
        _backStack.clear()
        _backStack.add(route)
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
