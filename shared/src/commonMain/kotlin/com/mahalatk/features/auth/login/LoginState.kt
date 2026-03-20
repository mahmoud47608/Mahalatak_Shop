package com.mahalatk.features.auth.login

import androidx.compose.runtime.Immutable
import org.jetbrains.compose.resources.StringResource

@Immutable
data class LoginState(
    val mobile: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val mobileError: StringResource? = null,
    val passwordError: StringResource? = null
)
