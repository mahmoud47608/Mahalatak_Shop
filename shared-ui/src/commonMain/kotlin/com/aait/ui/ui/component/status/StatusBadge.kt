package com.aait.ui.component.status

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.aait.ui.component.text.DefaultText
import com.aait.ui.theme.CornerDimensions
import com.aait.ui.theme.ExtendedTheme
import com.aait.ui.theme.PaddingDimensions

enum class StatusType {
    SUCCESS,
    ERROR,
    WARNING,
    INFO,
    NEUTRAL
}

/**
 * A small badge/chip for displaying order or service statuses.
 */
@Composable
fun StatusBadge(
    modifier: Modifier = Modifier,
    text: String,
    statusType: StatusType = StatusType.NEUTRAL
) {
    val backgroundColor = when (statusType) {
        StatusType.SUCCESS -> ExtendedTheme.colors.success.copy(alpha = 0.1f)
        StatusType.ERROR -> MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
        StatusType.WARNING -> ExtendedTheme.colors.warning.copy(alpha = 0.1f)
        StatusType.INFO -> ExtendedTheme.colors.info.copy(alpha = 0.1f)
        StatusType.NEUTRAL -> MaterialTheme.colorScheme.surfaceVariant
    }

    val contentColor = when (statusType) {
        StatusType.SUCCESS -> ExtendedTheme.colors.success
        StatusType.ERROR -> MaterialTheme.colorScheme.error
        StatusType.WARNING -> ExtendedTheme.colors.warning
        StatusType.INFO -> ExtendedTheme.colors.info
        StatusType.NEUTRAL -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(CornerDimensions.medium))
            .background(backgroundColor)
            .padding(horizontal = PaddingDimensions.medium, vertical = PaddingDimensions.low)
    ) {
        DefaultText(
            text = text,
            textStyle = MaterialTheme.typography.bodySmall,
            textColor = contentColor
        )
    }
}
