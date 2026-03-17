package com.aait.base.cycles.common.map.tracking

import com.aait.base.ui.navigation.HomeNavKey
import com.aait.base.ui.navigation.LoginNavKey
import com.aait.base.ui.navigation.NavScreen
import com.aait.base.ui.navigation.NavigationHelper.clearStackAndNavigateTo
import com.aait.base.ui.navigation.NavigationHelper.popToAndPush
import com.aait.base.ui.navigation.NavigationHelper.popUp
import com.aait.base.ui.navigation.NavigationHelper.popUpTo
import com.aait.base.ui.navigation.NavigationHelper.push
import com.aait.base.ui.navigation.SplashNavKey
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class MapTrackingViewModelTest {

    @Test
    fun `popUp keeps root element`() {
        val backStack = mutableListOf<NavScreen>(SplashNavKey)

        backStack.popUp()

        assertEquals(1, backStack.size)
        assertEquals(SplashNavKey, backStack.last())
    }

    @Test
    fun `push adds screen and popUp removes top`() {
        val backStack = mutableListOf<NavScreen>(SplashNavKey)

        backStack.push(LoginNavKey)
        assertEquals(LoginNavKey, backStack.last())

        backStack.popUp()
        assertEquals(SplashNavKey, backStack.last())
    }

    @Test
    fun `popUpTo trims stack up to target`() {
        val backStack = mutableListOf<NavScreen>(
            SplashNavKey,
            LoginNavKey,
            HomeNavKey,
            HomeNavKey
        )

        backStack.popUpTo(LoginNavKey, inclusive = false)

        assertEquals(2, backStack.size)
        assertEquals(LoginNavKey, backStack.last())
    }

    @Test
    fun `popToAndPush replaces top flow from target`() {
        val backStack = mutableListOf<NavScreen>(
            SplashNavKey,
            HomeNavKey,
            HomeNavKey
        )

        backStack.popToAndPush(HomeNavKey, LoginNavKey)

        assertEquals(3, backStack.size)
        assertEquals(LoginNavKey, backStack.last())
    }

    @Test
    fun `clearStackAndNavigateTo resets stack`() {
        val backStack = mutableListOf<NavScreen>(SplashNavKey, HomeNavKey)

        backStack.clearStackAndNavigateTo(HomeNavKey)

        assertEquals(1, backStack.size)
        assertEquals(HomeNavKey, backStack.last())
        assertFalse(backStack.contains(SplashNavKey))
    }
}
