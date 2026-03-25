package com.mahalatk.features.auth.login

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahalatk.common.component.button.DefaultButton
import com.mahalatk.common.component.button.LanguageButton
import com.mahalatk.common.component.inputs.DefaultTextField
import com.mahalatk.common.component.utilis.noRippleClickable
import com.mahalatk.theme.MahalatkTheme
import kotlinx.coroutines.flow.collectLatest
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.app_icon
import mahalatk.shared.generated.resources.create_account_prefix
import mahalatk.shared.generated.resources.forgot_password
import mahalatk.shared.generated.resources.ic_lock
import mahalatk.shared.generated.resources.ic_phone
import mahalatk.shared.generated.resources.login
import mahalatk.shared.generated.resources.password
import mahalatk.shared.generated.resources.phone
import mahalatk.shared.generated.resources.sign_up
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    onNavigateToHome: () -> Unit = {},
    onNavigateToSignUp: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Language Button - always on the left
        LanguageButton(
            modifier = Modifier
                .align(AbsoluteAlignment.Left)
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(38.dp))

        // Logo
        Image(
            painter = painterResource(Res.drawable.app_icon),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(40.dp))

        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            // Phone field
            DefaultTextField(
                value = uiState.mobile,
                onValueChanged = {
                    viewModel.updateState {
                        copy(
                            mobile = it,
                            mobileError = null
                        )
                    }
                },
                placeholderText = stringResource(Res.string.phone),
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next,
                errorText = uiState.mobileError?.let { stringResource(it) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.ic_phone),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MahalatkTheme.hint
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password field
            DefaultTextField(
                value = uiState.password,
                onValueChanged = {
                    viewModel.updateState {
                        copy(
                            password = it,
                            passwordError = null
                        )
                    }
                },
                placeholderText = stringResource(Res.string.password),
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                errorText = uiState.passwordError?.let { stringResource(it) },
                visualTransformation = if (uiState.passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.ic_lock),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MahalatkTheme.hint
                    )
                },
                trailingIcon = {
                    IconButton(onClick = {
                        viewModel.updateState { copy(passwordVisible = !passwordVisible) }
                    }) {
                        Icon(
                            imageVector = if (uiState.passwordVisible) Icons.Filled.Visibility
                            else Icons.Filled.VisibilityOff,
                            contentDescription = null,
                            tint = MahalatkTheme.hint
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Forgot Password
            Text(
                text = stringResource(Res.string.forgot_password),
                color = MahalatkTheme.primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.End)
                    .noRippleClickable { }
            )

            Spacer(modifier = Modifier.height(60.dp))

            // Login Button
            DefaultButton(
                text = stringResource(Res.string.login),
                onClick = { onNavigateToHome() },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Create An Account / Sign Up
        val annotatedText = buildAnnotatedString {
            append(stringResource(Res.string.create_account_prefix))
            withStyle(
                style = SpanStyle(
                    color = MahalatkTheme.primary,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append(stringResource(Res.string.sign_up))
            }
        }
        Text(
            text = annotatedText,
            fontSize = 14.sp,
            color = MahalatkTheme.black,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .noRippleClickable { onNavigateToSignUp() }
        )
    }

    LaunchedEffect(Unit) {
        viewModel.authData.collectLatest { authData ->
            if (authData != null) {
                onNavigateToHome()
            }
        }
    }
}
