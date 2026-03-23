package com.mahalatk.data.platform

import com.mahalatk.data.util.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import kotlinx.serialization.json.Json

actual fun createPlatformHttpClient(json: Json, baseUrl: String): HttpClient {
    return HttpClient(Darwin) {
        engine {
            configureRequest {
                setTimeoutInterval(NetworkConstants.NETWORK_TIMEOUT / 1000.0)
            }
        }
        installCommonPlugins(json, baseUrl)
    }
}
