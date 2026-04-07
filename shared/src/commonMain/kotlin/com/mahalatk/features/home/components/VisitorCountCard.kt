package com.mahalatk.features.home.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahalatk.common.component.card.GlassCard
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.CornerDimensions
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.ic_profile
import mahalatk.shared.generated.resources.shop_visitors
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private val VisitorAccent = Color(0xFFDEA477)

@Composable
internal fun VisitorCountCard(count: Int, modifier: Modifier = Modifier) {
    val animatedCount = remember { Animatable(0f) }
    LaunchedEffect(count) {
        animatedCount.animateTo(
            targetValue = count.toFloat(),
            animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        )
    }
    val displayCount by remember { derivedStateOf { animatedCount.value.toInt() } }

    GlassCard(
        modifier = modifier,
        accentColor = VisitorAccent,
        cornerRadius = CornerDimensions.lg,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Icon
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        brush = Brush.linearGradient(
                            listOf(
                                VisitorAccent.copy(alpha = 0.06f),
                                VisitorAccent.copy(alpha = 0.14f),
                            )
                        )
                    )
                    .border(0.5.dp, VisitorAccent.copy(alpha = 0.15f), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_profile),
                    contentDescription = null,
                    tint = VisitorAccent,
                    modifier = Modifier.size(24.dp),
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = displayCount.toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColor.TextPrimary,
                )
                Text(
                    text = stringResource(Res.string.shop_visitors),
                    style = MahalatkTheme.bodySmall,
                    color = AppColor.TextSecondary,
                )
            }
        }
    }
}
