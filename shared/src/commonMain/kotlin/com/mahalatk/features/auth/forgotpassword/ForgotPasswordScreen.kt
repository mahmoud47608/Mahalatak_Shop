package com.mahalatk.features.auth.forgotpassword

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.button.DefaultButton
import com.mahalatk.common.component.inputs.DefaultTextField
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.app_icon
import mahalatk.shared.generated.resources.forgot_password_subtitle
import mahalatk.shared.generated.resources.forgot_password_title
import mahalatk.shared.generated.resources.ic_phone
import mahalatk.shared.generated.resources.phone_number
import mahalatk.shared.generated.resources.send_code
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ForgotPasswordScreen(
    onSendCode: (phoneNumber: String) -> Unit,
    viewModel: ForgotPasswordViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Image(
            painter = painterResource(Res.drawable.app_icon),
            contentDescription = null,
            contentScale = ContentScale.Fit,
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = stringResource(Res.string.forgot_password_title),
            style = MahalatkTheme.headlineSmall,
            color = MahalatkTheme.primary,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(Res.string.forgot_password_subtitle),
            style = MahalatkTheme.bodyMedium,
            color = MahalatkTheme.hint,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(40.dp))

        DefaultTextField(
            value = state.phone,
            onValueChanged = viewModel::onPhoneChanged,
            placeholderText = stringResource(Res.string.phone_number),
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Done,
            errorText = state.phoneError?.let { stringResource(it) },
            leadingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.ic_phone),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MahalatkTheme.hint,
                )
            },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(40.dp))

        DefaultButton(
            text = stringResource(Res.string.send_code),
            onClick = {
                if (viewModel.validate()) {
                    onSendCode(state.phone)
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
