package com.mahalatk.domain.util

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

actual fun getPlatformLanguage(): String = NSLocale.currentLocale.languageCode
