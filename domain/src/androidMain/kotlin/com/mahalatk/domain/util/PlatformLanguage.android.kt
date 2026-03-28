package com.mahalatk.domain.util

import java.util.Locale

actual fun getPlatformLanguage(): String {
    // On Android, the locale is updated by AppCompatDelegate/LocaleManager
    // which updates Locale.getDefault() for the app process
    return Locale.getDefault().language
}
