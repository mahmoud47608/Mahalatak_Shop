package com.aait.data.di

import com.aait.data.datasource.SecureStorage
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single { SecureStorage(get()) }
}
