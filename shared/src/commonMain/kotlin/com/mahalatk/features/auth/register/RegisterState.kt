package com.mahalatk.features.auth.register

import androidx.compose.runtime.Immutable

@Immutable
data class RegisterState(
    val name: String = "",
    val mobile: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordVisible: Boolean = false,
    val confirmPasswordVisible: Boolean = false,
    val nameError: String? = null,
    val mobileError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val profileImage: ByteArray? = null,
)
