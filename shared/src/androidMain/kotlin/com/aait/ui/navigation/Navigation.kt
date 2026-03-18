package com.aait.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.aait.cycles.auth.login.LoginScreen
import com.aait.cycles.splash.SplashScreen
import com.aait.ui.navigation.NavigationHelper.clearStackAndNavigateTo
import com.aait.ui.navigation.NavigationHelper.popUp

@Composable
fun Navigation(modifier: Modifier, backStack: MutableList<NavScreen>) {

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = { backStack.popUp() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = { key ->
            when (key) {
                SplashNavKey -> NavEntry(key) {
                    SplashScreen(
                        toLoginScreen = {
                            backStack.clearStackAndNavigateTo(LoginNavKey)
                        }
                    )
                }

                LoginNavKey -> NavEntry(key) {
                    LoginScreen()
                }

                HomeNavKey -> NavEntry(key) {}


                is MoreNavKey -> NavEntry(key) {}

                is PickLocationNavKey -> NavEntry(key) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Pick Location",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }


                is ChatNavKey -> NavEntry(key) {
                    ChatPlaceholderScreen(roomId = key.roomId, title = key.title)
                }
            }
        })
}

@Composable
private fun ChatPlaceholderScreen(roomId: Int, title: String?) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Chat room #$roomId${title?.let { " - $it" } ?: ""}",
            style = MaterialTheme.typography.titleMedium
        )
    }
}
