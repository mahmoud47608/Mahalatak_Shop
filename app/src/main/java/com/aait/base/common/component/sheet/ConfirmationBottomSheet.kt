package com.aait.base.common.component.sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aait.base.common.component.button.ButtonStyle
import com.aait.base.common.component.button.DefaultButton
import com.aait.base.ui.theme.BaseTheme
import com.aait.base.ui.theme.PaddingDimensions
import com.mahalatak.R

/**
 * Generic confirmation bottom sheet component
 *
 * @param title The question or confirmation message to display
 * @param confirmText Text for the confirm button (default: "نعم" - Yes)
 * @param cancelText Text for the cancel button (default: "لا" - No)
 * @param onConfirm Callback when the confirm button is clicked
 * @param onDismiss Callback when the sheet is dismissed or cancel is clicked
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationBottomSheet(
    title: String,
    confirmText: String = stringResource(R.string.yes),
    cancelText: String = stringResource(R.string.no),
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss.invoke()
        },
        sheetState = sheetState,
        dragHandle = null,
        shape = RoundedCornerShape(
            topStart = 10.dp,
            topEnd = 10.dp,
            bottomStart = 0.dp,
            bottomEnd = 0.dp
        ),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 24.dp,
                    start = PaddingDimensions.high,
                    end = PaddingDimensions.high,
                    bottom = 24.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(PaddingDimensions.high)
        ) {
            // Title text
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            // Buttons row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(PaddingDimensions.high)
            ) {
                // Cancel button (Red)
                DefaultButton(
                    text = cancelText,
                    onClick = {
                        onDismiss.invoke()
                    },
                    modifier = Modifier.weight(1f),
                    style = ButtonStyle.ERROR
                )

                // Confirm button (Green)
                DefaultButton(
                    text = confirmText,
                    onClick = {
                        onConfirm.invoke()
                    },
                    modifier = Modifier.weight(1f),
                    style = ButtonStyle.SUCCESS
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConfirmationBottomSheetPreview() {
    BaseTheme {
        ConfirmationBottomSheet(
            title = "هل أنت متأكد من تسجيل الخروج ؟",
            confirmText = "نعم",
            cancelText = "لا",
            onConfirm = { },
            onDismiss = { }
        )
    }
}
