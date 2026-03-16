package com.aait.data.di

import com.aait.data.datasource.PreferenceDataSource
import com.aait.data.datasource.PreferenceDataSourceImpl
import com.aait.data.remote.ChatEndPoint
import com.aait.data.remote.HomeEndPoint
import com.aait.data.repo_impl.ChatRepositoryImpl
import com.aait.data.repo_impl.HomeRepositoryImpl
import com.aait.data.repo_impl.PreferenceRepositoryImpl
import com.aait.data.repo_impl.createSocketRepository
import com.aait.data.util.NetworkConstants
import com.aait.data.util.TokenHeaderProvider
import com.aait.domain.repository.ChatRepository
import com.aait.domain.repository.HomeRepository
import com.aait.domain.repository.PreferenceRepository
import com.aait.domain.repository.SocketRepository
import com.aait.domain.usecase.auth.ActivateUseCase
import com.aait.domain.usecase.auth.ForgetPasswordUseCase
import com.aait.domain.usecase.auth.LoginUseCase
import com.aait.domain.usecase.auth.RegisterUseCase
import com.aait.domain.usecase.auth.ResendCodeUseCase
import com.aait.domain.usecase.auth.ResetPasswordUseCase
import com.aait.domain.usecase.chat.MessagesUseCase
import com.aait.domain.usecase.chat.UploadMessageFileUseCase
import com.aait.domain.usecase.general.CountriesUseCase
import com.aait.domain.usecase.socket.ConnectSocketUseCase
import com.aait.domain.usecase.socket.DisconnectSocketUseCase
import com.aait.domain.usecase.socket.EmitSocketUseCase
import com.aait.domain.usecase.socket.OpenChannelSocketUseCase
import com.aait.ui.UIRepo
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module

val networkModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }

    single {
        TokenHeaderProvider(get())
    }

    single {
        val config = get<AppConfig>()
        val tokenHeaderProvider = get<TokenHeaderProvider>()

        HttpClient {
            install(ContentNegotiation) {
                json(get())
            }
            install(Logging) {
                level = LogLevel.HEADERS
            }
            defaultRequest {
                url(config.remoteBaseUrl)
                headers.append(NetworkConstants.AUTHORIZATION, tokenHeaderProvider.getToken())
                headers.append(NetworkConstants.LANGUAGE, "en")
            }
        }
    }

    single { HomeEndPoint(get()) }
    single { ChatEndPoint(get()) }
}

val repositoryModule = module {
    single<HomeRepository> { HomeRepositoryImpl(get()) }
    single<ChatRepository> { ChatRepositoryImpl(get()) }
    single<PreferenceRepository> { PreferenceRepositoryImpl(get()) }
    single<SocketRepository> {
        val config = get<AppConfig>()
        createSocketRepository(config.socketBaseUrl, get())
    }
}

val dataSourceModule = module {
    single { Settings() }
    single<PreferenceDataSource> {
        PreferenceDataSourceImpl(
            settings = get(),
            secureStorage = get()
        )
    }
}

val useCaseModule = module {
    factory { LoginUseCase(get()) }
    factory { RegisterUseCase(get()) }
    factory { ActivateUseCase(get()) }
    factory { ForgetPasswordUseCase(get()) }
    factory { ResetPasswordUseCase(get()) }
    factory { ResendCodeUseCase(get()) }
    factory { CountriesUseCase(get()) }
    factory { MessagesUseCase(get()) }
    factory { UploadMessageFileUseCase(get()) }
    factory { ConnectSocketUseCase(get()) }
    factory { DisconnectSocketUseCase(get()) }
    factory { EmitSocketUseCase(get()) }
    factory { OpenChannelSocketUseCase(get()) }
}

val uiModule = module {
    single { UIRepo() }
}

val sharedModules =
    listOf(networkModule, repositoryModule, dataSourceModule, useCaseModule, uiModule)

expect fun platformModule(): Module
