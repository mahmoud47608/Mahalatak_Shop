package com.mahalatk.features.settings.changepassword

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.button.DefaultButton
import com.mahalatk.common.component.dialog.SuccessDialog
import com.mahalatk.common.component.header.ScreenHeader
import com.mahalatk.common.component.inputs.DefaultTextField
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.change_password
import mahalatk.shared.generated.resources.confirm
import mahalatk.shared.generated.resources.confirm_new_password
import mahalatk.shared.generated.resources.ic_lock
import mahalatk.shared.generated.resources.new_password
import mahalatk.shared.generated.resources.old_password
import mahalatk.shared.generated.resources.password_changed_success
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChangePasswordScreen(
    viewModel: ChangePasswordViewModel = koinViewModel(),
    onBack: () -> Unit = {},
) {
    val state by viewModel.uiState.collectAsState()
    var showSuccess by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(AppColor.ScreenBackground)) {

        ScreenHeader(
            title = stringResource(Res.string.change_password),
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
                colors = CardDefaults.cardColors(containerColor = AppColor.Surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // Old Password
                    DefaultTextField(
                        value = state.oldPassword,
                        onValueChanged = viewModel::onOldPasswordChanged,
                        placeholderText = stringResource(Res.string.old_password),
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next,
                        errorText = state.oldPasswordError?.let { stringResource(it) },
                        visualTransformation = if (state.oldPasswordVisible) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(Res.drawable.ic_lock),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = MahalatkTheme.primary,
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = {
                                viewModel.updateState { copy(oldPasswordVisible = !oldPasswordVisible) }
                            }) {
                                Icon(
                                    imageVector = if (state.oldPasswordVisible) Icons.Filled.Visibility
                                    else Icons.Filled.VisibilityOff,
                                    contentDescription = null,
                                    tint = MahalatkTheme.primary,
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // New Password
                    DefaultTextField(
                        value = state.newPassword,
                        onValueChanged = viewModel::onNewPasswordChanged,
                        placeholderText = stringResource(Res.string.new_password),
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next,
                        errorText = state.newPasswordError?.let { stringResource(it) },
                        visualTransformation = if (state.newPasswordVisible) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(Res.drawable.ic_lock),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = MahalatkTheme.primary,
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = {
                                viewModel.updateState { copy(newPasswordVisible = !newPasswordVisible) }
                            }) {
                                Icon(
                                    imageVector = if (state.newPasswordVisible) Icons.Filled.Visibility
                                    else Icons.Filled.VisibilityOff,
                                    contentDescription = null,
                                    tint = MahalatkTheme.primary,
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Confirm New Password
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
                                tint = MahalatkTheme.primary,
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = {
                                viewModel.updateState { copy(confirmPasswordVisible = !confirmPasswordVisible) }
                            }) {
                                Icon(
                                    imageVector = if (state.confirmPasswordVisible) Icons.Filled.Visibility
                                    else Icons.Filled.VisibilityOff,
                                    contentDescription = null,
                                    tint = MahalatkTheme.primary,
                                )
                            }
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
                        viewModel.changePassword()
                        showSuccess = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
        }
    }

    // Success dialog
    SuccessDialog(
        message = stringResource(Res.string.password_changed_success),
        visible = showSuccess,
        onDismiss = {
            showSuccess = false
            onBack()
        },
    )
}
