package com.mahalatk.features.settings.changepassword

import androidx.compose.runtime.Immutable
import org.jetbrains.compose.resources.StringResource

@Immutable
data class ChangePasswordState(
    val oldPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val oldPasswordVisible: Boolean = false,
    val newPasswordVisible: Boolean = false,
    val confirmPasswordVisible: Boolean = false,

    val oldPasswordError: StringResource? = null,
    val newPasswordError: StringResource? = null,
    val confirmPasswordError: StringResource? = null,
)
