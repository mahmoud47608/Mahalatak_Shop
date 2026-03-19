package com.mahalatk.di

import com.mahalatk.data.IosHomeRepository
import com.mahalatk.data.IosPreferenceRepository
import com.mahalatk.data.IosTokenCacheManager
import com.mahalatk.domain.repository.HomeRepository
import com.mahalatk.domain.repository.PreferenceRepository
import com.mahalatk.domain.util.TokenCacheManager
import org.koin.core.context.startKoin
import org.koin.dsl.module

private val iosDataModule = module {
    single<PreferenceRepository> { IosPreferenceRepository() }
    single<TokenCacheManager> { IosTokenCacheManager() }
    single<HomeRepository> { IosHomeRepository() }
}

object KoinInitHelper {
    fun doInitKoin() {
        startKoin {
            modules(
                iosDataModule,
                useCaseModule,
                sharedModule,
            )
        }
    }
}
