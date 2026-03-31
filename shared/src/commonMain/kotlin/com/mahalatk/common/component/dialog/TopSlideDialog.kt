package com.mahalatk.common.component.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.utilis.noRippleClickable
import kotlinx.coroutines.delay

@Composable
fun TopSlideDialog(
    visible: Boolean,
    onDismiss: () -> Unit,
    autoDismissMillis: Long = 0L,
    content: @Composable () -> Unit,
) {
    if (!visible) return

    var animateIn by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        animateIn = true
    }

    if (autoDismissMillis > 0) {
        LaunchedEffect(Unit) {
            delay(autoDismissMillis)
            onDismiss()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f))
            .noRippleClickable { onDismiss() },
        contentAlignment = Alignment.Center,
    ) {
        AnimatedVisibility(
            visible = animateIn,
            enter = slideInVertically(
                initialOffsetY = { -it },
                animationSpec = tween(400),
            ),
            exit = slideOutVertically(
                targetOffsetY = { -it },
                animationSpec = tween(300),
            ),
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .noRippleClickable { /* prevent dismiss on card click */ },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            ) {
                content()
            }
        }
    }
}
