package com.aait.common.component.inputs

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aait.ui.theme.CornerDimensions
import com.aait.ui.theme.SpacingDimensions
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * A quantity selector with +/- buttons and manual text input.
 *
 * Features:
 * - Long-press +/- to auto-repeat with accelerating speed
 * - Haptic feedback on each step
 * - Animated quantity transitions
 * - Manual text entry with clamping on focus loss
 * - Theme-aware colors for dark mode support
 */
@Composable
fun QuantitySelector(
    modifier: Modifier = Modifier,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    minQuantity: Int = 1,
    maxQuantity: Int = Int.MAX_VALUE
) {
    val colorScheme = MaterialTheme.colorScheme
    val canDecrease = quantity > minQuantity
    val canIncrease = quantity < maxQuantity
    val haptic = LocalHapticFeedback.current
    val focusManager = LocalFocusManager.current

    var isEditing by remember { mutableStateOf(false) }
    var editText by remember { mutableStateOf("") }
    var hasFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    // Auto-focus when entering edit mode
    LaunchedEffect(isEditing) {
        if (isEditing) {
            hasFocused = false
            focusRequester.requestFocus()
        }
    }

    // Track previous quantity for animation direction
    var previousQuantity by remember { mutableIntStateOf(quantity) }
    val isIncreasing = quantity >= previousQuantity
    LaunchedEffect(quantity) { previousQuantity = quantity }

    Row(
        modifier = modifier
            .height(40.dp)
            .border(
                width = 1.dp,
                color = colorScheme.outline,
                shape = RoundedCornerShape(CornerDimensions.sm)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Decrease button
        QuantitySelectorButton(
            icon = Icons.Rounded.Remove,
            contentDescription = "Decrease",
            enabled = canDecrease,
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onQuantityChange((quantity - 1).coerceAtLeast(minQuantity))
            },
            onLongPressRepeat = { step ->
                val newValue = (quantity - step).coerceAtLeast(minQuantity)
                if (newValue != quantity) {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onQuantityChange(newValue)
                }
            }
        )

        // Vertical divider
        Box(
            Modifier
                .width(1.dp)
                .fillMaxHeight()
                .background(colorScheme.outline)
        )

        // Quantity display / text input
        Box(
            modifier = Modifier
                .widthIn(min = 56.dp)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            if (isEditing) {
                BasicTextField(
                    value = editText,
                    onValueChange = { newValue ->
                        // Only accept digits
                        if (newValue.all { it.isDigit() } && newValue.length <= 9) {
                            editText = newValue
                        }
                    },
                    textStyle = MaterialTheme.typography.titleSmall.copy(
                        textAlign = TextAlign.Center,
                        color = colorScheme.onSurface
                    ),
                    modifier = Modifier
                        .width(IntrinsicSize.Min)
                        .widthIn(min = 40.dp)
                        .padding(horizontal = SpacingDimensions.sp)
                        .focusRequester(focusRequester)
                        .onFocusChanged { focusState ->
                            if (!focusState.isFocused && isEditing) {
                                // Clamp and commit on focus loss
                                val parsed = editText.toIntOrNull() ?: minQuantity
                                onQuantityChange(parsed.coerceIn(minQuantity, maxQuantity))
                                isEditing = false
                            }
                        },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            val parsed = editText.toIntOrNull() ?: minQuantity
                            onQuantityChange(parsed.coerceIn(minQuantity, maxQuantity))
                            isEditing = false
                            focusManager.clearFocus()
                        }
                    ),
                    cursorBrush = SolidColor(colorScheme.primary),
                    singleLine = true
                )
            } else {
                AnimatedContent(
                    targetState = quantity,
                    transitionSpec = {
                        if (isIncreasing) {
                            (slideInVertically { -it } + fadeIn()) togetherWith
                                    (slideOutVertically { it } + fadeOut())
                        } else {
                            (slideInVertically { it } + fadeIn()) togetherWith
                                    (slideOutVertically { -it } + fadeOut())
                        } using SizeTransform(clip = false)
                    },
                    modifier = Modifier
                        .pointerInput(Unit) {
                            detectTapGestures {
                                editText = quantity.toString()
                                isEditing = true
                            }
                        }
                        .padding(horizontal = SpacingDimensions.sp1),
                    label = "quantity"
                ) { targetQuantity ->
                    Text(
                        text = targetQuantity.toString(),
                        style = MaterialTheme.typography.titleSmall,
                        color = colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Vertical divider
        Box(
            Modifier
                .width(1.dp)
                .fillMaxHeight()
                .background(colorScheme.outline)
        )

        // Increase button
        QuantitySelectorButton(
            icon = Icons.Rounded.Add,
            contentDescription = "Increase",
            enabled = canIncrease,
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onQuantityChange((quantity + 1).coerceAtMost(maxQuantity))
            },
            onLongPressRepeat = { step ->
                val newValue = (quantity + step).coerceAtMost(maxQuantity)
                if (newValue != quantity) {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onQuantityChange(newValue)
                }
            }
        )
    }
}

@Composable
private fun QuantitySelectorButton(
    icon: ImageVector,
    contentDescription: String,
    enabled: Boolean,
    onClick: () -> Unit,
    onLongPressRepeat: (step: Int) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val backgroundColor = if (enabled) colorScheme.primaryContainer else colorScheme.surfaceVariant
    val iconTint = if (enabled) colorScheme.primary else colorScheme.outline

    // Critical: Use rememberUpdatedState to ensure the long-press loop sees the latest callbacks
    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(CornerDimensions.xs))
            .background(backgroundColor)
            .then(
                if (enabled) {
                    Modifier.pointerInput(enabled) {
                        detectTapGestures(
                            onTap = { currentOnClick() },
                            onPress = {
                                val initialDelay = 500L
                                val normalInterval = 150L
                                val fastInterval = 60L
                                val accelerateAfter = 6

                                // Launch the repeat loop in a child coroutine
                                val repeatJob = scope.launch {
                                    delay(initialDelay)
                                    var tickCount = 0
                                    while (isActive) {
                                        val step = if (tickCount >= accelerateAfter) 2 else 1
                                        val interval =
                                            if (tickCount >= accelerateAfter) fastInterval else normalInterval
                                        currentOnLongPressRepeat(step)
                                        tickCount++
                                        delay(interval)
                                    }
                                }

                                try {
                                    // Wait until the finger is released or the gesture is cancelled
                                    awaitRelease()
                                } finally {
                                    // Ensure the repeating loop is stopped
                                    repeatJob.cancel()
                                }
                            }
                        )
                    }
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = iconTint,
            modifier = Modifier.size(20.dp)
        )
    }
}
