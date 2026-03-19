package com.mahalatk.common.component.utilis

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlin.time.ComparableTimeMark
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeSource

@Composable
fun Modifier.noRippleClickable(
    enabled: Boolean = true,
    debounceMillis: Long = 500L,
    onClick: () -> Unit
): Modifier {
    val lastClickMark = remember { mutableStateOf<ComparableTimeMark?>(null) }

    return this.clickable(
        indication = null,
        interactionSource = null,
        enabled = enabled
    ) {
        val now = TimeSource.Monotonic.markNow()
        val last = lastClickMark.value
        if (last == null || now - last >= debounceMillis.milliseconds) {
            lastClickMark.value = now
            onClick()
        }
    }
}
