package com.mahalatk.features.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.app_icon
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = koinViewModel(),
    onNavigateToLogin: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    LaunchedEffect(isLoggedIn) {
        // Wait for login check + splash animation
        delay(1500)
        when (isLoggedIn) {
            true -> onNavigateToHome()
            false -> onNavigateToLogin()
            null -> {} // still checking, wait
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.app_icon),
            contentDescription = "App Logo",
        )
    }
}
