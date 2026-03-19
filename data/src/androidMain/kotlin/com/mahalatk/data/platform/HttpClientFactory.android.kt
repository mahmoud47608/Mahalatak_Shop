package com.mahalatk.data.platform

import com.mahalatk.data.util.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit

actual fun createPlatformHttpClient(json: Json, baseUrl: String): HttpClient {
    return HttpClient(OkHttp) {
        engine {
            config {
                connectTimeout(NetworkConstants.NETWORK_TIMEOUT, TimeUnit.MILLISECONDS)
                readTimeout(NetworkConstants.NETWORK_TIMEOUT, TimeUnit.MILLISECONDS)
            }
        }
        installCommonPlugins(json, baseUrl)
    }
}
