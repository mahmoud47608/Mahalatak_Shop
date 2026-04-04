package com.mahalatk.features.auth.activation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahalatk.common.component.bottomsheet.SuccessBottomSheet
import com.mahalatk.common.component.button.DefaultButton
import com.mahalatk.common.component.header.ScreenHeader
import com.mahalatk.common.component.utilis.noRippleClickable
import com.mahalatk.theme.AppColor
import com.mahalatk.theme.MahalatkTheme
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.activation_code
import mahalatk.shared.generated.resources.activation_subtitle
import mahalatk.shared.generated.resources.app_icon
import mahalatk.shared.generated.resources.didnt_receive_code
import mahalatk.shared.generated.resources.resend_code
import mahalatk.shared.generated.resources.verify
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ActivationScreen(
    phoneNumber: String,
    showSuccessOnVerify: Boolean = false,
    successMessage: String = "",
    headerTitle: String? = null,
    onBack: (() -> Unit)? = null,
    onVerified: () -> Unit,
    viewModel: ActivationViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    val focusRequester = remember { FocusRequester() }
    var showSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(phoneNumber) {
        viewModel.setPhoneNumber(phoneNumber)
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    // When verification completes
    LaunchedEffect(state.isVerifying) {
        if (state.isVerifying) {
            if (showSuccessOnVerify) {
                showSuccess = true
            } else {
                onVerified()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (headerTitle != null) AppColor.ScreenBackground else Color.Transparent),
    ) {
        if (headerTitle != null) {
            ScreenHeader(
                title = headerTitle,
                onBackClick = onBack,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = if (headerTitle != null) 24.dp else 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (headerTitle == null) {
                Spacer(modifier = Modifier.height(40.dp))

                // App Logo
                Image(
                    painter = painterResource(Res.drawable.app_icon),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                )

                Spacer(modifier = Modifier.height(40.dp))
            }

            // Title
            Text(
                text = stringResource(Res.string.activation_code),
                style = MahalatkTheme.headlineSmall,
                color = MahalatkTheme.primary,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle with phone number
            Text(
                text = "${stringResource(Res.string.activation_subtitle)} +${state.phoneNumber.ifEmpty { phoneNumber }}",
                style = MahalatkTheme.bodyMedium,
                color = MahalatkTheme.hint,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(40.dp))

            // OTP Input - single hidden field + visual boxes
            OtpInput(
                code = state.fullCode,
                codeLength = 4,
                focusRequester = focusRequester,
                onCodeChanged = { newCode ->
                    val digits = newCode.filter { it.isDigit() }.take(4)
                    digits.forEachIndexed { i, c -> viewModel.onDigitEntered(i, c.toString()) }
                    // Clear remaining
                    for (i in digits.length until 4) viewModel.onDigitEntered(i, "")
                },
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Timer
            Text(
                text = state.timerText,
                style = MahalatkTheme.titleMedium,
                color = MahalatkTheme.hint,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Resend code
            val resendText = buildAnnotatedString {
                append("${stringResource(Res.string.didnt_receive_code)} ")
                withStyle(
                    SpanStyle(
                        color = if (state.canResend) AppColor.Primary else AppColor.TextHint,
                        fontWeight = FontWeight.Bold,
                    )
                ) {
                    append(stringResource(Res.string.resend_code))
                }
            }
            Text(
                text = resendText,
                style = MahalatkTheme.bodyMedium,
                color = MahalatkTheme.hint,
                modifier = Modifier.noRippleClickable(enabled = state.canResend) {
                    viewModel.resendCode()
                },
            )

            Spacer(modifier = Modifier.weight(1f))

            // Verify button
            DefaultButton(
                text = stringResource(Res.string.verify),
                enabled = state.isCodeComplete && !state.isVerifying,
                onClick = { viewModel.verify() },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }

    // Success bottom sheet (for registration flow)
    if (showSuccessOnVerify) {
        SuccessBottomSheet(
            message = successMessage,
            visible = showSuccess,
            onDismiss = {
                showSuccess = false
                onVerified()
            },
        )
    }
}

@Composable
private fun OtpInput(
    code: String,
    codeLength: Int,
    focusRequester: FocusRequester,
    onCodeChanged: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(contentAlignment = Alignment.Center) {
        // Hidden text field that captures all keyboard input
        BasicTextField(
            value = code,
            onValueChange = { onCodeChanged(it.filter { c -> c.isDigit() }.take(codeLength)) },
            modifier = Modifier
                .size(1.dp) // invisible
                .focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            cursorBrush = SolidColor(Color.Transparent),
        )

        // Visual digit boxes (always LTR)
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().noRippleClickable {
                    focusRequester.requestFocus()
                    keyboardController?.show()
                },
            ) {
                for (i in 0 until codeLength) {
                    if (i > 0) Spacer(modifier = Modifier.width(14.dp))
                    val digit = code.getOrNull(i)?.toString() ?: ""
                    val isCurrent = i == code.length && code.length < codeLength
                    val borderColor = when {
                        digit.isNotEmpty() -> AppColor.Primary
                        isCurrent -> AppColor.Primary.copy(alpha = 0.5f)
                        else -> AppColor.Border
                    }
                    val bgColor =
                        if (digit.isNotEmpty()) AppColor.Primary.copy(alpha = 0.05f) else AppColor.Surface

                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(bgColor)
                            .border(1.5.dp, borderColor, RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (digit.isNotEmpty()) {
                            Text(
                                text = digit,
                                style = MahalatkTheme.headlineSmall,
                                color = AppColor.TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                textAlign = TextAlign.Center,
                            )
                        } else {
                            Text(
                                text = "—",
                                style = MahalatkTheme.titleLarge,
                                color = AppColor.TextHint.copy(alpha = 0.4f),
                            )
                        }
                    }
                }
            }
        } // end CompositionLocalProvider
    }
}
