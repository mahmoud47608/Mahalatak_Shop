package com.mahalatk.features.auth.login

import androidx.compose.runtime.Immutable

@Immutable
data class LoginState(
    val mobile: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false
)
