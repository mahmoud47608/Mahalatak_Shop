package com.aait.base.common.component.utilis

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun Modifier.noRippleClickable(
    enabled: Boolean = true,
    debounceMillis: Long = 500L,
    onClick: () -> Unit
): Modifier {
    val lastClickTime = remember { mutableLongStateOf(0L) }

    return this.clickable(
        indication = null,
        interactionSource = null,
        enabled = enabled
    ) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime.longValue >= debounceMillis) {
            lastClickTime.longValue = currentTime
            onClick()
        }
    }
}