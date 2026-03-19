package com.mahalatk.ui.navigation

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.toolbar.DefaultAppBar
import com.mahalatk.ui.theme.BaseTheme
import com.mahalatk.ui.util.UIMessage
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(viewModel: MainViewModel = koinViewModel()) {
    BaseTheme {
        val navigator = rememberAppNavigator()
        val isLoading by viewModel.isLoading.collectAsState()
        val snackbarHostState = remember { SnackbarHostState() }
        val currentRoute = navigator.currentRoute
        val config = currentRoute?.screenConfig() ?: ScreenConfig()

        LaunchedEffect(Unit) {
            viewModel.uiMessages.collectLatest { message ->
                val text = when (message) {
                    is UIMessage.Text -> message.message
                    is UIMessage.StringKey -> message.key
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
                val toolbar = if (config.toolBarState.onBackButtonClicked == null) {
                    config.toolBarState.updateBackButtonClicked { navigator.pop() }
                } else {
                    config.toolBarState
                }
                DefaultAppBar(toolbar)
            },
            bottomBar = {
                if (config.showBottomBar) {
                    AppBottomBar(
                        currentRoute = currentRoute,
                    ) { item ->
                        navigator.popToRoot()
                        if (navigator.currentRoute != item.route) {
                            navigator.push(item.route)
                        }
                    }
                }
            },
        ) { innerPadding ->
            val contentPadding = PaddingValues(
                top = if (config.hasTopPadding) innerPadding.calculateTopPadding() else 0.dp,
                bottom = if (config.hasBottomPadding) innerPadding.calculateBottomPadding() else 0.dp,
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                NavigationContent(
                    modifier = Modifier.fillMaxSize(),
                    currentRoute = currentRoute,
                    navigator = navigator,
                )

                if (isLoading) {
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
            }
        }
    }
}
