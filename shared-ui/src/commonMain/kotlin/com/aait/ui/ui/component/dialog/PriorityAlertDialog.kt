package com.aait.ui.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.aait.ui.component.button.DefaultButton
import com.aait.ui.component.text.DefaultText
import com.aait.ui.theme.CornerDimensions
import com.aait.ui.theme.PaddingDimensions
import com.aait.ui.theme.colorText
import com.aait.ui.theme.colorTextHint


/**
 * Non-cancelable priority alert dialog
 * Used for critical alerts that require user acknowledgment
 */
@Composable
fun PriorityAlertDialog(
    title: String,
    message: String,
    buttonText: String,
    onButtonClick: () -> Unit
) {
    Dialog(
        onDismissRequest = { /* Non-cancelable - do nothing */ },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = PaddingDimensions.high),
            shape = RoundedCornerShape(CornerDimensions.high)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(PaddingDimensions.high),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(PaddingDimensions.medium)
            ) {
                // Warning Icon
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = "Warning",
                    modifier = Modifier.size(50.dp),
                    tint = MaterialTheme.colorScheme.error
                )
                // Title
                DefaultText(
                    text = title,
                    textStyle = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 16.sp
                    ),
                    textColor = colorText(),
                    textAlign = TextAlign.Center
                )
                // Message
                DefaultText(
                    text = message,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp
                    ),
                    textColor = colorTextHint(),
                    textAlign = TextAlign.Center
                )
                // Action Button
                DefaultButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = buttonText,
                    onClick = onButtonClick
                )
            }
        }
    }
}
