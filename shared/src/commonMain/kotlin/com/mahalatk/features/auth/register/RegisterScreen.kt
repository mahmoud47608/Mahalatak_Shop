package com.mahalatk.features.auth.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.BitmapPainter
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
import com.mahalatk.common.component.imagepicker.rememberImagePickerLauncher
import com.mahalatk.common.component.imagepicker.toImageBitmap
import com.mahalatk.common.component.inputs.DefaultTextField
import com.mahalatk.ui.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.already_have_account
import mahalatk.shared.generated.resources.confirm_password
import mahalatk.shared.generated.resources.email
import mahalatk.shared.generated.resources.full_name
import mahalatk.shared.generated.resources.password
import mahalatk.shared.generated.resources.passwords_do_not_match
import mahalatk.shared.generated.resources.phone
import mahalatk.shared.generated.resources.please_enter_confirm_password
import mahalatk.shared.generated.resources.please_enter_email
import mahalatk.shared.generated.resources.please_enter_name
import mahalatk.shared.generated.resources.please_enter_password
import mahalatk.shared.generated.resources.please_enter_phone_number
import mahalatk.shared.generated.resources.please_enter_valid_email
import mahalatk.shared.generated.resources.register
import mahalatk.shared.generated.resources.sign_in
import mahalatk.shared.generated.resources.upload_photo
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = koinViewModel(),
    onNavigateToLogin: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    // Image picker
    val pickImage = rememberImagePickerLauncher { bytes ->
        viewModel.updateState { copy(profileImage = bytes) }
    }

    // Map error keys to localized strings
    val nameError = uiState.nameError?.toLocalizedError()
    val mobileError = uiState.mobileError?.toLocalizedError()
    val emailError = uiState.emailError?.toLocalizedError()
    val passwordError = uiState.passwordError?.toLocalizedError()
    val confirmPasswordError = uiState.confirmPasswordError?.toLocalizedError()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MahalatkTheme.white)
            .verticalScroll(rememberScrollState())
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Back Button - auto mirrors for RTL via AutoMirrored
        IconButton(
            onClick = onNavigateToLogin,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 8.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = MahalatkTheme.black
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Profile Photo Circle
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MahalatkTheme.iconBackground)
                .border(2.dp, MahalatkTheme.border, CircleShape)
                .clickable { pickImage() },
            contentAlignment = Alignment.Center
        ) {
            if (uiState.profileImage != null) {
                val bitmap = uiState.profileImage?.toImageBitmap()
                if (bitmap != null) {
                    Image(
                        painter = BitmapPainter(bitmap),
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Filled.CameraAlt,
                        contentDescription = null,
                        tint = MahalatkTheme.hint,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(Res.string.upload_photo),
                        color = MahalatkTheme.hint,
                        fontSize = 10.sp,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Name field
            DefaultTextField(
                value = uiState.name,
                onValueChanged = { viewModel.updateState { copy(name = it, nameError = null) } },
                placeholderText = stringResource(Res.string.full_name),
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                errorText = nameError,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null,
                        tint = MahalatkTheme.hint
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Phone field
            DefaultTextField(
                value = uiState.mobile,
                onValueChanged = {
                    viewModel.updateState { copy(mobile = it, mobileError = null) }
                },
                placeholderText = stringResource(Res.string.phone),
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next,
                errorText = mobileError,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Phone,
                        contentDescription = null,
                        tint = MahalatkTheme.hint
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email field
            DefaultTextField(
                value = uiState.email,
                onValueChanged = {
                    viewModel.updateState { copy(email = it, emailError = null) }
                },
                placeholderText = stringResource(Res.string.email),
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                errorText = emailError,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = null,
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
                    viewModel.updateState { copy(password = it, passwordError = null) }
                },
                placeholderText = stringResource(Res.string.password),
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next,
                errorText = passwordError,
                visualTransformation = if (uiState.passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = null,
                        tint = MahalatkTheme.hint
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
                            tint = MahalatkTheme.hint
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Password field
            DefaultTextField(
                value = uiState.confirmPassword,
                onValueChanged = {
                    viewModel.updateState {
                        copy(confirmPassword = it, confirmPasswordError = null)
                    }
                },
                placeholderText = stringResource(Res.string.confirm_password),
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                errorText = confirmPasswordError,
                visualTransformation = if (uiState.confirmPasswordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = null,
                        tint = MahalatkTheme.hint
                    )
                },
                trailingIcon = {
                    IconButton(onClick = {
                        viewModel.updateState {
                            copy(confirmPasswordVisible = !uiState.confirmPasswordVisible)
                        }
                    }) {
                        Icon(
                            imageVector = if (uiState.confirmPasswordVisible) Icons.Filled.Visibility
                            else Icons.Filled.VisibilityOff,
                            contentDescription = null,
                            tint = MahalatkTheme.hint
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Register Button
            DefaultButton(
                text = stringResource(Res.string.register),
                onClick = { viewModel.register() },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(32.dp))

            // Already have an account? Sign In
            val annotatedText = buildAnnotatedString {
                append(stringResource(Res.string.already_have_account))
                withStyle(
                    style = SpanStyle(
                        color = MahalatkTheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(stringResource(Res.string.sign_in))
                }
            }
            Text(
                text = annotatedText,
                fontSize = 14.sp,
                color = MahalatkTheme.black,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable { onNavigateToLogin() }
            )
        }
    }
}

@Composable
private fun String.toLocalizedError(): String {
    return when (this) {
        "please_enter_name" -> stringResource(Res.string.please_enter_name)
        "please_enter_phone_number" -> stringResource(Res.string.please_enter_phone_number)
        "please_enter_email" -> stringResource(Res.string.please_enter_email)
        "please_enter_valid_email" -> stringResource(Res.string.please_enter_valid_email)
        "please_enter_password" -> stringResource(Res.string.please_enter_password)
        "please_enter_confirm_password" -> stringResource(Res.string.please_enter_confirm_password)
        "passwords_do_not_match" -> stringResource(Res.string.passwords_do_not_match)
        else -> this
    }
}
