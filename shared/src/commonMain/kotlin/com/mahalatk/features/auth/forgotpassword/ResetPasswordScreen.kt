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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.bottomsheet.SuccessBottomSheet
import com.mahalatk.common.component.button.DefaultButton
import com.mahalatk.common.component.inputs.DefaultTextField
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.app_icon
import mahalatk.shared.generated.resources.confirm_new_password
import mahalatk.shared.generated.resources.ic_lock
import mahalatk.shared.generated.resources.new_password
import mahalatk.shared.generated.resources.password_reset_success
import mahalatk.shared.generated.resources.reset_password
import mahalatk.shared.generated.resources.reset_password_subtitle
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ResetPasswordScreen(
    onSuccess: () -> Unit,
    viewModel: ResetPasswordViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    var showSuccess by remember { mutableStateOf(false) }

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
            text = stringResource(Res.string.reset_password),
            style = MahalatkTheme.headlineSmall,
            color = MahalatkTheme.primary,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(Res.string.reset_password_subtitle),
            style = MahalatkTheme.bodyMedium,
            color = MahalatkTheme.hint,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(40.dp))

        // New Password
        DefaultTextField(
            value = state.password,
            onValueChanged = viewModel::onPasswordChanged,
            placeholderText = stringResource(Res.string.new_password),
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next,
            errorText = state.passwordError?.let { stringResource(it) },
            visualTransformation = if (state.passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            leadingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.ic_lock),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MahalatkTheme.hint,
                )
            },
            trailingIcon = {
                IconButton(onClick = viewModel::togglePasswordVisibility) {
                    Icon(
                        imageVector = if (state.passwordVisible) Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff,
                        contentDescription = null,
                        tint = MahalatkTheme.hint,
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password
        DefaultTextField(
            value = state.confirmPassword,
            onValueChanged = viewModel::onConfirmPasswordChanged,
            placeholderText = stringResource(Res.string.confirm_new_password),
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            errorText = state.confirmPasswordError?.let { stringResource(it) },
            visualTransformation = if (state.confirmPasswordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            leadingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.ic_lock),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MahalatkTheme.hint,
                )
            },
            trailingIcon = {
                IconButton(onClick = viewModel::toggleConfirmPasswordVisibility) {
                    Icon(
                        imageVector = if (state.confirmPasswordVisible) Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff,
                        contentDescription = null,
                        tint = MahalatkTheme.hint,
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(40.dp))

        DefaultButton(
            text = stringResource(Res.string.reset_password),
            onClick = {
                if (viewModel.submit()) {
                    showSuccess = true
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }

    // Success bottom sheet
    SuccessBottomSheet(
        message = stringResource(Res.string.password_reset_success),
        visible = showSuccess,
        onDismiss = {
            showSuccess = false
            onSuccess()
        },
    )
}
