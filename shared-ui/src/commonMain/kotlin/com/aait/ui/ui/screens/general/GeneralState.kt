package com.aait.ui.screens.general

import androidx.compose.runtime.Immutable

@Immutable
data class GeneralState(
    val image: String? = "",
    val content: String? = "",
)
