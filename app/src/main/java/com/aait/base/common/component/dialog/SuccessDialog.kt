package com.aait.base.common.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.aait.base.common.component.text.DefaultText
import com.aait.base.ui.theme.BaseTheme
import com.aait.base.ui.theme.CornerDimensions
import com.aait.base.ui.theme.PaddingDimensions
import com.aait.base.ui.theme.colorText
import com.aait.base.ui.theme.colorTextHint
import com.mahalatak.R


@Composable
fun SuccessDialog(
    title: String,
    description: String = "",
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
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
                Icon(
                    painter = painterResource(id = R.drawable.app_icon),
                    contentDescription = "Success",
                    modifier = Modifier.size(50.dp),
                    tint = Color.Unspecified
                )
                // Success Message
                DefaultText(
                    text = title,
                    textStyle = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 14.sp
                    ),
                    textColor = colorText(),
                    textAlign = TextAlign.Start
                )
                // Success Message
                DefaultText(
                    text = description,
                    textStyle = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 12.sp
                    ),
                    textColor = colorTextHint(),
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}
@Preview(showBackground = true, locale = "ar")
@Composable
fun SuccessDialogPreview() {
    BaseTheme {
        SuccessDialog(
            title = "تم إضافة الخبر بنجاح",
            onDismiss = {}
        )
    }
}
