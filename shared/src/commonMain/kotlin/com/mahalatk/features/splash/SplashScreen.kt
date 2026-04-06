package com.mahalatk.features.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mahalatk.theme.AppColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
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
    LaunchedEffect(Unit) {
        delay(1500)
        viewModel.events.collectLatest { event ->
            when (event) {
                SplashEvent.NavigateToHome -> onNavigateToHome()
                SplashEvent.NavigateToLogin -> onNavigateToLogin()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Surface)
            .drawBehind {
                drawCircle(
                    color = AppColor.Primary.copy(alpha = if (AppColor.isDark) 0.04f else 0.06f),
                    radius = 140.dp.toPx(),
                    center = Offset(size.width * 0.8f, size.height * 0.2f),
                )
                drawCircle(
                    color = AppColor.Primary.copy(alpha = if (AppColor.isDark) 0.03f else 0.05f),
                    radius = 100.dp.toPx(),
                    center = Offset(size.width * 0.15f, size.height * 0.75f),
                )
                drawCircle(
                    color = AppColor.Primary.copy(alpha = if (AppColor.isDark) 0.025f else 0.04f),
                    radius = 60.dp.toPx(),
                    center = Offset(size.width * 0.6f, size.height * 0.9f),
                )
            },
        contentAlignment = Alignment.Center
    ) {
        // Subtle glow behind logo
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(
                    brush = Brush.radialGradient(
                        listOf(
                            AppColor.Primary.copy(alpha = if (AppColor.isDark) 0.08f else 0.10f),
                            Color.Transparent,
                        )
                    )
                ),
        )
        Image(
            painter = painterResource(Res.drawable.app_icon),
            contentDescription = "App Logo",
        )
    }
}
