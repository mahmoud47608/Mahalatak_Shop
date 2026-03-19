package com.mahalatk.domain.util

import java.util.Locale

actual fun getPlatformLanguage(): String = Locale.getDefault().language
