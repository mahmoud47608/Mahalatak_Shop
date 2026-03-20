package com.mahalatk.features.auth.register

import androidx.compose.runtime.Immutable
import org.jetbrains.compose.resources.StringResource

@Immutable
data class RegisterState(
    val name: String = "",
    val mobile: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordVisible: Boolean = false,
    val confirmPasswordVisible: Boolean = false,
    val nameError: StringResource? = null,
    val mobileError: StringResource? = null,
    val emailError: StringResource? = null,
    val passwordError: StringResource? = null,
    val confirmPasswordError: StringResource? = null,
    val profileImage: ByteArray? = null,
)
