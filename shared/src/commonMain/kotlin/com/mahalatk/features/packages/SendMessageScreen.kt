package com.mahalatk.features.packages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.bottomsheet.SuccessBottomSheet
import com.mahalatk.common.component.button.DefaultButton
import com.mahalatk.common.component.header.ScreenHeader
import com.mahalatk.common.component.inputs.DefaultTextField
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.message_content
import mahalatk.shared.generated.resources.message_sent_success
import mahalatk.shared.generated.resources.send_message
import mahalatk.shared.generated.resources.send_to_all_customers
import org.jetbrains.compose.resources.stringResource

@Composable
fun SendMessageScreen(
    onBack: () -> Unit = {},
) {
    var messageText by remember { mutableStateOf("") }
    var showSuccess by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().background(AppColor.ScreenBackground),
    ) {
        ScreenHeader(
            title = stringResource(Res.string.send_message),
            onBackClick = onBack,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 20.dp),
        ) {
            Text(
                text = stringResource(Res.string.send_to_all_customers),
                style = MahalatkTheme.titleSmall,
                color = AppColor.TextPrimary,
                fontWeight = FontWeight.SemiBold,
            )

            Spacer(modifier = Modifier.height(16.dp))

            DefaultTextField(
                value = messageText,
                onValueChanged = { messageText = it },
                placeholderText = stringResource(Res.string.message_content),
                modifier = Modifier.fillMaxWidth(),
                maxLines = 6,
                minLines = 4,
            )

            Spacer(modifier = Modifier.height(24.dp))

            DefaultButton(
                text = stringResource(Res.string.send_message),
                onClick = {
                    if (messageText.isNotBlank()) {
                        showSuccess = true
                        messageText = ""
                    }
                },
                enabled = messageText.isNotBlank(),
            )
        }
    }

    SuccessBottomSheet(
        visible = showSuccess,
        message = stringResource(Res.string.message_sent_success),
        onDismiss = {
            showSuccess = false
            onBack()
        },
    )
}
