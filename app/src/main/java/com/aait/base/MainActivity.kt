package com.aait.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aait.base.common.component.toolbar.DefaultAppBar
import com.aait.base.common.component.toolbar.ToolBarState
import com.aait.base.ui.navigation.BottomAppBar
import com.aait.base.ui.navigation.HomeNavKey
import com.aait.base.ui.navigation.Navigation
import com.aait.base.ui.navigation.NavigationHelper.popToAndPush
import com.aait.base.ui.navigation.NavigationHelper.popUp
import com.aait.base.ui.navigation.bottomBarItems
import com.aait.base.ui.theme.BaseTheme
import com.aait.base.util.UIMessage
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                scrim = android.graphics.Color.TRANSPARENT,
                darkScrim = android.graphics.Color.TRANSPARENT,
            )
        )
        setContent {
            BaseTheme {
                val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
                val snackbarHostState = remember { SnackbarHostState() }
                val backStack = viewModel.navigationStack
                val currentScreen = backStack.lastOrNull()

                val showBottomBar by remember(currentScreen) {
                    derivedStateOf {
                        currentScreen?.let { key ->
                            bottomBarItems.any { it.navScreen::class == key::class }
                        } ?: false
                    }
                }
                val toolbar = currentScreen?.toolBarState ?: ToolBarState.Hidden

                LaunchedEffect(Unit) {
                    viewModel.uiMessages.collectLatest { message ->
                        val text = when (message) {
                            is UIMessage.Text -> message.message
                            is UIMessage.Resource -> getString(message.messageResId)
                        }
                        snackbarHostState.showSnackbar(text)
                    }
                }

                Scaffold(
                    snackbarHost = {
                        SnackbarHost(
                            hostState = snackbarHostState,
                            modifier = Modifier.padding(bottom = 26.dp),
                        )
                    },
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
                                currentKey = currentScreen,
                                items = bottomBarItems
                            ) { item ->
                                backStack.popToAndPush(HomeNavKey, newKey = item.navScreen)
                            }
                        }
                    },
                ) { innerPadding ->
                    val contentPadding = PaddingValues(
                        top = if (currentScreen?.hasTopPadding == true) innerPadding.calculateTopPadding() else 0.dp,
                        bottom = if (currentScreen?.hasBottomPadding == true) innerPadding.calculateBottomPadding() else 0.dp,
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(contentPadding)
                    ) {
                        Navigation(
                            modifier = Modifier.fillMaxSize(),
                            backStack = backStack
                        )
                        if (isLoading) {
                            LoadingOverlay()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
            .pointerInput(Unit) {},
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}
