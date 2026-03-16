package com.aait.base.common.component.image

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun DefaultLottie(
    modifier: Modifier = Modifier,
    resId: Int,
    iterations: Int = LottieConstants.IterateForever,
    isPlaying: Boolean = true,
    speed: Float = 1f,
    onEnd: (() -> Unit)? = null
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resId))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = iterations,
        isPlaying = isPlaying,
        speed = speed,
        restartOnPlay = false
    )

    // Detect animation end (only for finite iterations)
    LaunchedEffect(progress, iterations) {
        if (iterations != LottieConstants.IterateForever && progress >= 1f) {
            onEnd?.invoke()
        }
    }

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier
    )
}

