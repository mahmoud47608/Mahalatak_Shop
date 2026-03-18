package com.aait.cycles.screens.success

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aait.common.component.image.DefaultLottie
import com.aait.common.component.text.DefaultText
import com.aait.ui.theme.PaddingDimensions
import com.aait.ui.theme.colorText
import kotlinx.coroutines.launch
import mahalatak.shared.generated.resources.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessBottomSheetScreen(
    title: String,
    onDismiss: () -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    ModalBottomSheet(
        containerColor = MaterialTheme.colorScheme.background,
        onDismissRequest = {
            onDismiss.invoke()
        },
        sheetState = sheetState,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = PaddingDimensions.high,
                    start = PaddingDimensions.medium,
                    bottom = PaddingDimensions.medium,
                    end = PaddingDimensions.medium
                ),
            verticalArrangement = Arrangement.Top
        ) {
            DefaultLottie(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                resId = 0,
                iterations = 1
            ) {
                coroutineScope.launch {
                    sheetState.hide()
                    onDismiss.invoke()
                }
            }
            DefaultText(
                text = title,
                textStyle = MaterialTheme.typography.titleLarge.copy(fontSize = 16.sp),
                textColor = colorText(),
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
