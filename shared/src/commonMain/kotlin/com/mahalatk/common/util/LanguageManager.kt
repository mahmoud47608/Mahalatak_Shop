package com.mahalatk.common.util

import androidx.compose.runtime.Composable

/**
 * Returns the current app language code ("ar" or "en").
 */
expect fun getCurrentLanguageCode(): String

/**
 * Remembers a language changer that can switch the app locale.
 * Returns a lambda: pass "ar" or "en" to change language.
 */
@Composable
expect fun rememberLanguageChanger(): (String) -> Unit
