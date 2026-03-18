package com.aait.cycles.common.language

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

class AppLocaleManager(private val context: Context) {

    fun setLocale(languageCode: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val localeManager = context.getSystemService(LocaleManager::class.java)
            localeManager.applicationLocales =
                android.os.LocaleList(Locale.forLanguageTag(languageCode))
        } else {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageCode))
        }
    }

    fun getCurrentLocale(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val localeManager = context.getSystemService(LocaleManager::class.java)
            val locales = localeManager.applicationLocales
            if (!locales.isEmpty) {
                locales[0].toLanguageTag()
            } else {
                // Return system default
                Locale.getDefault().toLanguageTag()
            }
        } else {
            val locales = AppCompatDelegate.getApplicationLocales()
            if (!locales.isEmpty) {
                locales[0]?.toLanguageTag() ?: Locale.getDefault().toLanguageTag()
            } else {
                // Return system default
                Locale.getDefault().toLanguageTag()
            }
        }
    }
}
