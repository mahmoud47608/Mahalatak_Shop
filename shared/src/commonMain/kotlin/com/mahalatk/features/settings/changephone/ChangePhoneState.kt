package com.mahalatk.features.settings.changephone

import androidx.compose.runtime.Immutable
import org.jetbrains.compose.resources.StringResource

@Immutable
data class ChangePhoneState(
    val phone: String = "",
    val phoneError: StringResource? = null,
)
