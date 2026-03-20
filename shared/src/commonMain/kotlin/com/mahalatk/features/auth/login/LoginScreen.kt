package com.mahalatk.features.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.mahalatk.common.component.inputs.DefaultTextField
import kotlinx.coroutines.flow.collectLatest
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.app_icon
import mahalatk.shared.generated.resources.create_account_prefix
import mahalatk.shared.generated.resources.forgot_password
import mahalatk.shared.generated.resources.login
import mahalatk.shared.generated.resources.password
import mahalatk.shared.generated.resources.please_enter_password
import mahalatk.shared.generated.resources.please_enter_phone_number
import mahalatk.shared.generated.resources.sign_up
import mahalatk.shared.generated.resources.username_or_email
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

private val Teal = Color(0xFF1B8A7A)

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    onNavigateToHome: () -> Unit = {},
    onNavigateToSignUp: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    // Map error keys to localized strings
    val mobileError = when (uiState.mobileError) {
        "please_enter_phone_number" -> stringResource(Res.string.please_enter_phone_number)
        else -> uiState.mobileError
    }
    val passwordError = when (uiState.passwordError) {
        "please_enter_password" -> stringResource(Res.string.please_enter_password)
        else -> uiState.passwordError
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Logo
        Image(
            painter = painterResource(Res.drawable.app_icon),
            contentDescription = null,
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Phone field
        DefaultTextField(
            value = uiState.mobile,
            onValueChanged = { viewModel.updateState { copy(mobile = it, mobileError = null) } },
            placeholderText = stringResource(Res.string.username_or_email),
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Next,
            errorText = mobileError,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    tint = Color.Gray
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
            errorText = passwordError,
            visualTransformation = if (uiState.passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = null,
                    tint = Color.Gray
                )
            },
            trailingIcon = {
                IconButton(onClick = {
                    viewModel.updateState { copy(passwordVisible = !uiState.passwordVisible) }
                }) {
                    Icon(
                        imageVector = if (uiState.passwordVisible) Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Forgot Password
        Text(
            text = stringResource(Res.string.forgot_password),
            color = Teal,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .align(Alignment.End)
                .clickable { }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Login Button
        Button(
            onClick = { viewModel.login() },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Teal)
        ) {
            Text(
                text = stringResource(Res.string.login),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(32.dp))

        // Create An Account / Sign Up
        val annotatedText = buildAnnotatedString {
            append(stringResource(Res.string.create_account_prefix))
            withStyle(style = SpanStyle(color = Teal, fontWeight = FontWeight.Bold)) {
                append(stringResource(Res.string.sign_up))
            }
        }
        Text(
            text = annotatedText,
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable { onNavigateToSignUp() }
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
