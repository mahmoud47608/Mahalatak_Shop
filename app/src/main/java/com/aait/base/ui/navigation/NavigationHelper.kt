package com.aait.base.ui.navigation
import kotlin.collections.indexOfLast

object NavigationHelper {
    fun MutableList<NavScreen>.push(key: NavScreen) = add(key)

    fun MutableList<NavScreen>.popUp() {
        if (size > 1) removeAt(lastIndex)
    }

    fun MutableList<NavScreen>.popUpTo(
        target: NavScreen,
        inclusive: Boolean = false
    ) {
        val index = indexOfLast { it == target }
        if (index == -1) return

        subList(if (inclusive) index else index + 1, size).clear()
    }

    inline fun <reified T : NavScreen> MutableList<NavScreen>.dropAllOfType() {
        removeAll { it is T }
    }

    fun MutableList<NavScreen>.clearStackAndNavigateTo(key: NavScreen) {
        clear()
        add(key)
    }

    fun MutableList<NavScreen>.replaceTop(key: NavScreen) {
        if (isNotEmpty()) {
            removeAt(lastIndex)
        }
        add(key)
    }

    fun MutableList<NavScreen>.popToAndPush(
        target: NavScreen,
        newKey: NavScreen,
        inclusive: Boolean = false
    ) {
        popUpTo(target, inclusive)
        push(newKey)
    }
}