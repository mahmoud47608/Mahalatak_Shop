package com.aait.base.common.screens.general

import androidx.compose.runtime.Immutable

@Immutable
data class GeneralState(
    val image: String? = "",
    val content: String? = "",
)

