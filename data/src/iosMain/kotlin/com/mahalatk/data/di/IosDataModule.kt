package com.mahalatk.data.di

import com.mahalatk.data.datasource.IosSecureStorage
import com.mahalatk.domain.datasource.SecureStorage
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformDataModule: Module = module {
    single<SecureStorage> { IosSecureStorage() }
}
