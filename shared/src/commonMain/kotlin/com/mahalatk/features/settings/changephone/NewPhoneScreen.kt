package com.mahalatk.features.settings.changephone

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.button.DefaultButton
import com.mahalatk.common.component.header.ScreenHeader
import com.mahalatk.common.component.inputs.DefaultTextField
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.change_phone_title
import mahalatk.shared.generated.resources.confirm
import mahalatk.shared.generated.resources.enter_new_phone_subtitle
import mahalatk.shared.generated.resources.ic_phone
import mahalatk.shared.generated.resources.new_phone_number
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NewPhoneScreen(
    viewModel: ChangePhoneViewModel = koinViewModel(),
    onBack: () -> Unit = {},
    onConfirm: (newPhoneNumber: String) -> Unit,
) {
    val state by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(AppColor.ScreenBackground)) {

        ScreenHeader(
            title = stringResource(Res.string.change_phone_title),
            onBackClick = onBack,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 16.dp, bottom = 24.dp),
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(Res.string.enter_new_phone_subtitle),
                        style = MahalatkTheme.bodyMedium,
                        color = MahalatkTheme.hint,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    DefaultTextField(
                        value = state.phone,
                        onValueChanged = viewModel::onPhoneChanged,
                        placeholderText = stringResource(Res.string.new_phone_number),
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done,
                        errorText = state.phoneError?.let { stringResource(it) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(Res.drawable.ic_phone),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = MahalatkTheme.primary,
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            DefaultButton(
                text = stringResource(Res.string.confirm),
                onClick = {
                    if (viewModel.validate()) {
                        onConfirm(state.phone)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
        }
    }
}
