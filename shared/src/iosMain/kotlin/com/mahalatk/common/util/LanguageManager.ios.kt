package com.mahalatk.common.util

import androidx.compose.runtime.Composable
import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

private const val USER_LANGUAGE_KEY = "user_selected_language"

actual fun getCurrentLanguageCode(): String {
    val defaults = NSUserDefaults.standardUserDefaults
    // Read from explicit user choice first
    val userChoice = defaults.stringForKey(USER_LANGUAGE_KEY)
    if (userChoice != null) return userChoice
    // No explicit choice — follow device language
    return NSLocale.currentLocale.languageCode
}

/**
 * Call once on app startup to sync AppleLanguages with the user's choice.
 * If the user never chose a language, removes any stale AppleLanguages override
 * so the app follows the device language.
 */
fun initializeLanguage() {
    val defaults = NSUserDefaults.standardUserDefaults
    val userChoice = defaults.stringForKey(USER_LANGUAGE_KEY)
    if (userChoice != null) {
        // User explicitly picked a language — apply it
        defaults.setObject(listOf(userChoice), forKey = "AppleLanguages")
    } else {
        // No explicit choice — remove override so Compose Resources follows device locale
        defaults.removeObjectForKey("AppleLanguages")
    }
    defaults.synchronize()
}

@Composable
actual fun rememberLanguageChanger(): (String) -> Unit {
    return { languageCode: String ->
        val defaults = NSUserDefaults.standardUserDefaults
        // Store explicit user choice
        defaults.setObject(languageCode, forKey = USER_LANGUAGE_KEY)
        // Apply to AppleLanguages for Compose Resources
        defaults.setObject(listOf(languageCode), forKey = "AppleLanguages")
        defaults.synchronize()
        // Note: On iOS, language change requires app restart to take full effect.
    }
}
