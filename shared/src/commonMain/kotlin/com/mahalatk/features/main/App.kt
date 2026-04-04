package com.mahalatk.features.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.loading.LoadingOverlay
import com.mahalatk.domain.repository.PreferenceRepository
import com.mahalatk.navigation.AppBottomBar
import com.mahalatk.navigation.BottomNavItem
import com.mahalatk.navigation.LocalNavigator
import com.mahalatk.navigation.NavigationHost
import com.mahalatk.navigation.isAuthScreen
import com.mahalatk.navigation.isTabRoute
import com.mahalatk.navigation.rememberAppNavigator
import com.mahalatk.theme.MahalatkTheme
import com.mahalatk.util.UIMessage
import kotlinx.coroutines.flow.collectLatest
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.ic_background
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(viewModel: MainViewModel = koinViewModel()) {
    val preferenceRepository: PreferenceRepository = koinInject()

    // Load saved preference on first launch
    LaunchedEffect(Unit) {
        preferenceRepository.getDarkMode().collect { com.mahalatk.theme.AppColor.isDark = it }
    }

    // AppColor.isDark is mutableStateOf — reads here trigger recomposition instantly
    MahalatkTheme(darkTheme = com.mahalatk.theme.AppColor.isDark) {
        val navigator = rememberAppNavigator()
        val isLoading by viewModel.isLoading.collectAsState()
        val snackbarHostState = remember { SnackbarHostState() }

        // derivedStateOf prevents recomposition of the whole tree on every backstack change
        val isTabScreen by remember {
            derivedStateOf { navigator.currentRoute.isTabRoute }
        }
        val showAuthBg by remember {
            derivedStateOf { navigator.currentRoute.isAuthScreen }
        }

        // Stable lambda — won't cause BottomBar to recompose
        val onTabSelected = remember<(BottomNavItem) -> Unit> {
            { item -> navigator.switchTab(item.route) }
        }

        LaunchedEffect(Unit) {
            viewModel.uiMessages.collectLatest { message ->
                val text = when (message) {
                    is UIMessage.Text -> message.message
                    is UIMessage.StringKey -> message.key
                }
                snackbarHostState.showSnackbar(text)
            }
        }

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
                        AppBottomBar(onItemSelected = onTabSelected)
                    }
                },
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = innerPadding.calculateBottomPadding()),
                ) {
                    if (showAuthBg) {
                        Image(
                            painter = painterResource(Res.drawable.ic_background),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter),
                        )
                    }

                    NavigationHost()

                    if (isLoading) {
                        LoadingOverlay()
                    }
                }
            }
        }
    }
}
