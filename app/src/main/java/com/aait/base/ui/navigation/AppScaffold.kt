package com.aait.base.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.aait.base.common.component.toolbar.DefaultAppBar
import com.aait.base.ui.navigation.NavigationHelper.popToAndPush
import com.aait.base.ui.navigation.NavigationHelper.popUp
import com.aait.base.common.component.toolbar.ToolBarState

@Composable
fun AppScaffold(
    backStack: MutableList<NavScreen>,
    isLoading: Boolean,
    snackbarHostState: SnackbarHostState
) {
    val currentScreen = backStack.lastOrNull()

    val showBottomBar by remember(currentScreen) {
        derivedStateOf {
            currentScreen?.let { key ->
                bottomBarItems.any { it.navScreen::class == key::class }
            } ?: false
        }
    }
    val toolbar = currentScreen?.toolBarState ?: ToolBarState.Hidden

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                val updatedToolbar = if (toolbar.onBackButtonClicked == null) {
                    toolbar.updateBackButtonClicked { backStack.popUp() }
                } else {
                    toolbar
                }
                DefaultAppBar(updatedToolbar)
            },
            bottomBar = {
                if (showBottomBar) {
                    BottomAppBar(
                        currentKey = currentScreen, items = bottomBarItems
                    ) { item ->
                        backStack.popToAndPush(HomeNavKey, newKey = item.navScreen)
                    }
                }
            }
        ) { innerPadding ->
            val contentPadding =
                PaddingValues(
                    top = if (currentScreen?.hasTopPadding == true) innerPadding.calculateTopPadding() else 0.dp,
                    bottom = if (currentScreen?.hasBottomPadding == true) innerPadding.calculateBottomPadding() else 0.dp,
                )
            Navigation(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize(),
                backStack = backStack
            )
        }

        // Global loading overlay
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .pointerInput(Unit) {} // Block all touch interactions
                    .wrapContentSize(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        // Global snackbar for messages
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 26.dp)
        )
    }
}
