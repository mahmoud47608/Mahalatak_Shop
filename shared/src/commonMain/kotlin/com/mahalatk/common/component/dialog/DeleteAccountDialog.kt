package com.mahalatk.common.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.cancel
import mahalatk.shared.generated.resources.delete
import mahalatk.shared.generated.resources.delete_account
import mahalatk.shared.generated.resources.delete_account_confirmation
import mahalatk.shared.generated.resources.ic_delete
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private val DeleteRed = Color(0xFFF44336)

@Composable
fun DeleteAccountDialog(
    visible: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    TopSlideDialog(
        visible = visible,
        onDismiss = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Trash icon
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(DeleteRed.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_delete),
                    contentDescription = null,
                    tint = DeleteRed,
                    modifier = Modifier.size(36.dp),
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(Res.string.delete_account),
                style = MahalatkTheme.titleMedium,
                color = AppColor.TextPrimary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(Res.string.delete_account_confirmation),
                style = MahalatkTheme.bodyMedium,
                color = AppColor.TextHint,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Buttons row
            Row(modifier = Modifier.fillMaxWidth()) {
                // Delete button
                Button(
                    onClick = onConfirm,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DeleteRed,
                        contentColor = Color.White,
                    ),
                ) {
                    Text(
                        text = stringResource(Res.string.delete),
                        style = MahalatkTheme.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Cancel button
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = ButtonDefaults.outlinedButtonBorder(enabled = true),
                ) {
                    Text(
                        text = stringResource(Res.string.cancel),
                        style = MahalatkTheme.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AppColor.TextPrimary,
                    )
                }
            }
        }
    }
}
