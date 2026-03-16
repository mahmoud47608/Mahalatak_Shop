package com.aait.ui.component.utilis

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeSource

@Composable
fun Modifier.noRippleClickable(
    enabled: Boolean = true,
    debounceMillis: Long = 500L,
    onClick: () -> Unit
): Modifier {
    val lastClickMark =
        remember { mutableStateOf(TimeSource.Monotonic.markNow() - debounceMillis.milliseconds) }
    return this.clickable(
        indication = null,
        interactionSource = null,
        enabled = enabled
    ) {
        val now = TimeSource.Monotonic.markNow()
        if ((now - lastClickMark.value) >= debounceMillis.milliseconds) {
            lastClickMark.value = now
            onClick()
        }
    }
}
