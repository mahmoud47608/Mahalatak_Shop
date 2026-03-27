package com.mahalatk.common.component.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput

private val ScrimColor = Color.Black.copy(alpha = 0.3f)

/** Full-screen loading overlay that blocks touch events. */
@Composable
fun LoadingOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ScrimColor)
            .pointerInput(Unit) {},
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}
