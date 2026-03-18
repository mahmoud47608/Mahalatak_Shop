package com.aait.data.di

import com.aait.data.datasource.PreferenceDataSource
import com.aait.data.datasource.PreferenceDataSourceImpl
import com.aait.data.datasource.SecureStorage
import com.aait.data.remote.ChatEndPoint
import com.aait.data.remote.HomeEndPoint
import com.aait.data.repo_impl.ChatRepositoryImpl
import com.aait.data.repo_impl.HomeRepositoryImpl
import com.aait.data.repo_impl.PreferenceRepositoryImpl
import com.aait.data.repo_impl.SocketRepositoryImpl
import com.aait.data.util.NetworkConstants
import com.aait.data.util.TokenHeaderProvider
import com.aait.domain.repository.ChatRepository
import com.aait.domain.repository.HomeRepository
import com.aait.domain.repository.PreferenceRepository
import com.aait.domain.repository.SocketRepository
import com.aait.domain.util.TokenCacheManager
import com.mahalatak.data.BuildConfig
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
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
    single(named("socketPort")) { BuildConfig.SOCKET_PORT }
    single(named("socketBaseUrl")) { "${get<String>(named("baseUrl"))}:${get<String>(named("socketPort"))}" }
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
                header(NetworkConstants.AUTHORIZATION, get<TokenHeaderProvider>().getToken())
            }
        }
    }
}

val endpointModule = module {
    single { HomeEndPoint(get()) }
    single { ChatEndPoint(get()) }
}

val repositoryModule = module {
    single<HomeRepository> { HomeRepositoryImpl(get()) }
    single<ChatRepository> { ChatRepositoryImpl(get()) }
    single<SocketRepository> { SocketRepositoryImpl(get(named("socketBaseUrl")), get()) }
}
