package com.aait.base.cycles.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mahalatak.R
import kotlinx.coroutines.delay

@Preview(showBackground = true)
@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    toLoginScreen: () -> Unit = {}
) {
    // Launch effect to delay and navigate
    LaunchedEffect(Unit) {
        delay(1500)
        toLoginScreen()
    }

    // UI with centered logo
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        // Replace with your Image asset
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground), // Use your logo here
            contentDescription = "App Logo",
            modifier = Modifier.size(120.dp)
        )
    }
}
