package com.mahalatk.common.component.stepper

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.MahalatkTheme

/**
 * Step indicator with animated circles, connecting lines, and optional labels.
 */
@Composable
fun StepIndicator(
    totalSteps: Int,
    currentStep: Int,
    modifier: Modifier = Modifier,
    steps: List<StepData>? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center,
    ) {
        for (i in 0 until totalSteps) {
            StepItem(
                index = i,
                isCompleted = i < currentStep,
                isActive = i == currentStep,
                label = steps?.getOrNull(i)?.label,
            )

            if (i < totalSteps - 1) {
                ConnectingLine(isFilled = i < currentStep)
            }
        }
    }
}

// ── Single step (circle + label) — isolated to scope animations ─────────────

@Composable
private fun StepItem(
    index: Int,
    isCompleted: Boolean,
    isActive: Boolean,
    label: String?,
) {
    val bgColor by animateColorAsState(
        targetValue = if (isCompleted || isActive) AppColor.Primary else AppColor.Outline,
        animationSpec = tween(300),
    )
    val scale by animateFloatAsState(
        targetValue = if (isActive) 1.1f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .graphicsLayer { scaleX = scale; scaleY = scale }
                .clip(CircleShape)
                .background(bgColor),
            contentAlignment = Alignment.Center,
        ) {
            if (isCompleted) {
                Icon(
                    Icons.Filled.Check, null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp),
                )
            } else {
                Text(
                    text = "${index + 1}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isActive) Color.White else AppColor.TextHint,
                )
            }
        }

        if (label != null) {
            Spacer(modifier = Modifier.height(6.dp))
            StepLabel(label = label, isActive = isActive, isCompleted = isCompleted)
        }
    }
}

// ── Label with animated color ───────────────────────────────────────────────

@Composable
private fun StepLabel(label: String, isActive: Boolean, isCompleted: Boolean) {
    val color by animateColorAsState(
        targetValue = when {
            isActive -> AppColor.Primary
            isCompleted -> AppColor.TextSecondary
            else -> AppColor.TextHint
        },
        animationSpec = tween(300),
    )
    Text(
        text = label,
        style = MahalatkTheme.labelSmall,
        fontSize = 10.sp,
        color = color,
        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
        textAlign = TextAlign.Center,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

// ── Connecting line ─────────────────────────────────────────────────────────

@Composable
private fun RowScope.ConnectingLine(isFilled: Boolean) {
    val color by animateColorAsState(
        targetValue = if (isFilled) AppColor.Primary else AppColor.Outline,
        animationSpec = tween(300),
    )
    Box(
        modifier = Modifier
            .weight(1f)
            .padding(top = 15.dp)
            .height(3.dp)
            .padding(horizontal = 8.dp)
            .clip(CircleShape)
            .background(color),
    )
}

// ── Data ─────────────────────────────────────────────────────────────────────

data class StepData(
    val icon: ImageVector,
    val label: String,
)
