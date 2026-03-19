package com.mahalatk.data.platform

import com.mahalatk.data.util.NetworkConstants
import com.mahalatk.data.util.TokenHeaderProvider
import com.mahalatk.domain.util.getPlatformLanguage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

expect fun createPlatformHttpClient(json: Json, baseUrl: String): HttpClient

fun HttpClient.installTokenInterceptor(tokenProvider: TokenHeaderProvider): HttpClient {
    plugin(HttpSend).intercept { request ->
        val token = tokenProvider.getToken()
        if (token.isNotEmpty()) {
            request.headers[NetworkConstants.AUTHORIZATION] = token
        }
        execute(request)
    }
    return this
}

fun io.ktor.client.HttpClientConfig<*>.installCommonPlugins(json: Json, baseUrl: String) {
    install(ContentNegotiation) { json(json) }
    install(Logging) { level = LogLevel.BODY }
    defaultRequest {
        url("$baseUrl/api/")
        header(NetworkConstants.LANGUAGE, getPlatformLanguage())
    }
}
