package com.mahalatk.common.util

import androidx.compose.runtime.Composable
import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

actual fun getCurrentLanguageCode(): String {
    return NSLocale.currentLocale.languageCode
}

@Composable
actual fun rememberLanguageChanger(): (String) -> Unit {
    return { languageCode: String ->
        NSUserDefaults.standardUserDefaults.setObject(
            listOf(languageCode),
            forKey = "AppleLanguages"
        )
        NSUserDefaults.standardUserDefaults.synchronize()
        // Note: On iOS, language change requires app restart to take full effect.
        // The app will use the new language on next launch.
    }
}
