package com.mahalatk.data.platform

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import kotlinx.serialization.json.Json

actual fun createPlatformHttpClient(json: Json, baseUrl: String): HttpClient {
    return HttpClient(Darwin) {
        engine {
            configureRequest {
                setTimeoutInterval(120.0)
            }
        }
        installCommonPlugins(json, baseUrl)
    }
}
