package com.mahalatk.data.di

import com.mahalatk.data.datasource.PreferenceDataSourceImpl
import com.mahalatk.data.platform.AppConfig
import com.mahalatk.data.platform.createPlatformHttpClient
import com.mahalatk.data.platform.installTokenInterceptor
import com.mahalatk.data.remote.AuthEndPoint
import com.mahalatk.data.remote.CommonEndPoint
import com.mahalatk.data.remote.PackageEndPoint
import com.mahalatk.data.repository.AuthRepositoryImpl
import com.mahalatk.data.repository.CommonRepositoryImpl
import com.mahalatk.data.repository.PackageRepositoryImpl
import com.mahalatk.data.repository.PreferenceRepositoryImpl
import com.mahalatk.data.util.TokenHeaderProvider
import com.mahalatk.domain.datasource.PreferenceDataSource
import com.mahalatk.domain.repository.AuthRepository
import com.mahalatk.domain.repository.CommonRepository
import com.mahalatk.domain.repository.PackageRepository
import com.mahalatk.domain.repository.PreferenceRepository
import com.mahalatk.domain.util.TokenCacheManager
import com.russhwolf.settings.Settings
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformDataModule: Module

val commonDataModule = module {
    // Json
    single { Json { ignoreUnknownKeys = true; isLenient = true } }

    // Settings
    single<Settings> { Settings() }

    // Preferences
    single<PreferenceDataSource> { PreferenceDataSourceImpl(get(), get()) }
    single<PreferenceRepository> { PreferenceRepositoryImpl(get()) }

    // Token
    single { TokenHeaderProvider(get()) }
    single<TokenCacheManager> { get<TokenHeaderProvider>() }

    // Network
    single {
        val tokenProvider = get<TokenHeaderProvider>()
        createPlatformHttpClient(get(), AppConfig.baseUrl)
            .installTokenInterceptor(tokenProvider)
    }

    // Endpoints & Repositories
    single { AuthEndPoint(get(), get()) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }

    single { CommonEndPoint(get(), get()) }
    single<CommonRepository> { CommonRepositoryImpl(get()) }

    single { PackageEndPoint(get(), get()) }
    single<PackageRepository> { PackageRepositoryImpl(get()) }
}
