package com.aait.common.component.image

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Lottie animation placeholder for commonMain.
 * Actual Lottie implementation is platform-specific (Android only).
 */
@Composable
fun DefaultLottie(
    modifier: Modifier = Modifier,
    resId: Int = 0,
    iterations: Int = Int.MAX_VALUE,
    isPlaying: Boolean = true,
    speed: Float = 1f,
    onEnd: (() -> Unit)? = null
) {
    // Placeholder - Lottie is not available in commonMain
    Box(modifier = modifier)
}
