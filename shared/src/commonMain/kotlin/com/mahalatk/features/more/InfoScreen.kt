package com.mahalatk.features.more

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.card.GlassCard
import com.mahalatk.common.component.header.ScreenHeader
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.CornerDimensions
import com.mahalatk.theme.MahalatkTheme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/**
 * Reusable info screen layout used by About, Terms, and Privacy screens.
 * Shows a ScreenHeader, an icon, and text content inside a Card.
 */
@Composable
fun InfoScreen(
    title: String,
    icon: DrawableResource,
    content: String,
    onBack: () -> Unit = {},
) {
    Column(modifier = Modifier.fillMaxSize().background(AppColor.ScreenBackground)) {

        ScreenHeader(
            title = title,
            onBackClick = onBack,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 16.dp, bottom = 24.dp),
        ) {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                cornerRadius = CornerDimensions.lg,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // Icon
                    Image(
                        painter = painterResource(icon),
                        contentDescription = null,
                        modifier = Modifier.size(100.dp),
                        contentScale = ContentScale.Fit,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Content text
                    Text(
                        text = content,
                        style = MahalatkTheme.bodyMedium,
                        color = AppColor.TextHint,
                        textAlign = TextAlign.Center,
                        lineHeight = MahalatkTheme.bodyMedium.lineHeight,
                    )
                }
            }
        }
    }
}
