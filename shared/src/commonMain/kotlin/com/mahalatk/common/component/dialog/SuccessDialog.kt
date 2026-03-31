package com.mahalatk.common.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.ic_check_circle
import org.jetbrains.compose.resources.painterResource

@Composable
fun SuccessDialog(
    message: String,
    visible: Boolean,
    onDismiss: () -> Unit,
    autoDismissMillis: Long = 3000L,
) {
    TopSlideDialog(
        visible = visible,
        onDismiss = onDismiss,
        autoDismissMillis = autoDismissMillis,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(AppColor.Success.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_check_circle),
                    contentDescription = null,
                    tint = AppColor.Success,
                    modifier = Modifier.size(40.dp),
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = message,
                style = MahalatkTheme.titleMedium,
                color = AppColor.TextPrimary,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
