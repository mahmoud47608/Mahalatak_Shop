package com.mahalatk.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class NavEntry(val route: Route, val id: String) {
    @OptIn(ExperimentalUuidApi::class)
    constructor(route: Route) : this(route, Uuid.random().toString())
}

@Stable
class AppNavigator(initialRoute: Route = Route.Splash) {

    private val _backStack = mutableStateListOf(NavEntry(initialRoute))

    val currentEntry: NavEntry?
        get() = _backStack.lastOrNull()

    val currentRoute: Route?
        get() = currentEntry?.route

    val canGoBack: Boolean
        get() = _backStack.size > 1

    fun push(route: Route) {
        _backStack.add(NavEntry(route))
    }

    fun pop(): Boolean {
        if (!canGoBack) return false
        _backStack.removeAt(_backStack.lastIndex)
        return true
    }

    fun replaceAll(route: Route) {
        _backStack.clear()
        _backStack.add(NavEntry(route))
    }

    fun replaceTop(route: Route) {
        if (_backStack.isNotEmpty()) {
            _backStack.removeAt(_backStack.lastIndex)
        }
        _backStack.add(NavEntry(route))
    }

    fun popToRoot() {
        while (_backStack.size > 1) {
            _backStack.removeAt(_backStack.lastIndex)
        }
    }

    fun popTo(predicate: (Route) -> Boolean, inclusive: Boolean = false) {
        val index = _backStack.indexOfLast { predicate(it.route) }
        if (index == -1) return
        val fromIndex = if (inclusive) index else index + 1
        if (fromIndex < _backStack.size) {
            _backStack.subList(fromIndex, _backStack.size).clear()
        }
    }
}

@Composable
fun rememberAppNavigator(initialRoute: Route = Route.Splash): AppNavigator {
    return remember { AppNavigator(initialRoute) }
}
