package com.mahalatk.data.platform

import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.mahalatk.data.util.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import java.util.concurrent.TimeUnit

actual fun createPlatformHttpClient(json: Json, baseUrl: String): HttpClient {
    return HttpClient(OkHttp) {
        engine {
            config {
                connectTimeout(NetworkConstants.NETWORK_TIMEOUT, TimeUnit.MILLISECONDS)
                readTimeout(NetworkConstants.NETWORK_TIMEOUT, TimeUnit.MILLISECONDS)
            }

            // Fix malformed Content-Type from server BEFORE Ktor sees it
            addNetworkInterceptor(Interceptor { chain ->
                val response = chain.proceed(chain.request())
                val contentType = response.header("Content-Type")
                if (contentType != null && contentType.contains(",")) {
                    response.newBuilder()
                        .removeHeader("Content-Type")
                        .addHeader("Content-Type", "application/json; charset=utf-8")
                        .build()
                } else {
                    response
                }
            })

            addInterceptor(
                LoggingInterceptor.Builder()
                    .setLevel(Level.BASIC)
                    .log(okhttp3.internal.platform.Platform.INFO)
                    .request("Request")
                    .response("Response")
                    .build()
            )
        }
        installCommonPlugins(json, baseUrl)
    }
}
