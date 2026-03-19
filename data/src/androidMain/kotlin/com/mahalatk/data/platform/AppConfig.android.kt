package com.mahalatk.data.platform

import com.mahalatk.data.BuildConfig

actual object AppConfig {
    actual val baseUrl: String = BuildConfig.REMOTE_URL
    actual val apiKey: String = BuildConfig.API_KEY
    actual val socketPort: String = BuildConfig.SOCKET_PORT
}
