package com.mahalatk.features.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.mahalatk.common.util.rememberLanguageChanger
import com.mahalatk.navigation.AppBottomBar
import com.mahalatk.navigation.BottomNavItem
import com.mahalatk.navigation.LocalNavigator
import com.mahalatk.navigation.NavigationHost
import com.mahalatk.navigation.rememberAppNavigator
import com.mahalatk.theme.MahalatkTheme
import com.mahalatk.util.UIMessage
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(viewModel: MainViewModel = koinViewModel()) {
    MahalatkTheme {
        val navigator = rememberAppNavigator()
        val changeLanguage = rememberLanguageChanger()
        val isLoading by viewModel.isLoading.collectAsState()
        val snackbarHostState = remember { SnackbarHostState() }

        val isTabScreen = BottomNavItem.fromRoute(navigator.currentRoute) != null

        // Snackbar messages
        LaunchedEffect(Unit) {
            viewModel.uiMessages.collectLatest { message ->
                val text = when (message) {
                    is UIMessage.Text -> message.message
                    is UIMessage.StringKey -> message.key
                }
                snackbarHostState.showSnackbar(text)
            }
        }

        // Provide navigator to all children via CompositionLocal
        CompositionLocalProvider(LocalNavigator provides navigator) {
            Scaffold(
                snackbarHost = {
                    SnackbarHost(
                        hostState = snackbarHostState,
                        modifier = Modifier.padding(bottom = 26.dp),
                    )
                },
                bottomBar = {
                    if (isTabScreen) {
                        AppBottomBar(
                            onItemSelected = { item -> navigator.replaceAll(item.route) },
                        )
                    }
                },
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            bottom = if (isTabScreen) innerPadding.calculateBottomPadding() else 0.dp
                        )
                ) {
                    // Screen routing
                    NavigationHost { language ->
                        changeLanguage.invoke(language)
                    }

                    // Loading overlay
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
}
