package com.mahalatk.data.di

import com.mahalatk.data.BuildConfig
import com.mahalatk.data.datasource.PreferenceDataSource
import com.mahalatk.data.datasource.PreferenceDataSourceImpl
import com.mahalatk.data.datasource.SecureStorage
import com.mahalatk.data.remote.HomeEndPoint
import com.mahalatk.data.repository.HomeRepositoryImpl
import com.mahalatk.data.repository.PreferenceRepositoryImpl
import com.mahalatk.data.util.NetworkConstants
import com.mahalatk.data.util.TokenHeaderProvider
import com.mahalatk.domain.repository.HomeRepository
import com.mahalatk.domain.repository.PreferenceRepository
import com.mahalatk.domain.util.TokenCacheManager
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

val stringsModule = module {
    single(named("baseUrl")) { BuildConfig.REMOTE_URL }
    single(named("remoteBaseUrl")) { "${get<String>(named("baseUrl"))}/api/" }
}

val dataStoreModule = module {
    single<Settings> { Settings() }
    single { SecureStorage(androidContext()) }
    single<PreferenceDataSource> { PreferenceDataSourceImpl(get(), get()) }
    single<PreferenceRepository> { PreferenceRepositoryImpl(get()) }
}

val networkModule = module {
    single {
        Json { ignoreUnknownKeys = true; isLenient = true }
    }
    single { TokenHeaderProvider(get()) }
    single<TokenCacheManager> { get<TokenHeaderProvider>() }
    single<CoroutineContext> { Dispatchers.IO }
    single {
        val tokenProvider = get<TokenHeaderProvider>()
        HttpClient(OkHttp) {
            engine {
                config {
                    connectTimeout(NetworkConstants.NETWORK_TIMEOUT, TimeUnit.MILLISECONDS)
                    readTimeout(NetworkConstants.NETWORK_TIMEOUT, TimeUnit.MILLISECONDS)
                }
            }
            install(ContentNegotiation) { json(get()) }
            install(Logging) { level = LogLevel.BODY }
            defaultRequest {
                url(get<String>(named("remoteBaseUrl")))
                header(NetworkConstants.LANGUAGE, Locale.getDefault().language)
            }
        }.also { client ->
            client.plugin(HttpSend).intercept { request ->
                val token = tokenProvider.getToken()
                if (token.isNotEmpty()) {
                    request.headers[NetworkConstants.AUTHORIZATION] = token
                }
                execute(request)
            }
        }
    }
}

val endpointModule = module {
    single { HomeEndPoint(get()) }
}

val repositoryModule = module {
    single<HomeRepository> { HomeRepositoryImpl(get()) }
}
