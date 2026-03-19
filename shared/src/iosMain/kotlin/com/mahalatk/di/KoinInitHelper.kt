package com.mahalatk.di

import com.mahalatk.data.di.commonDataModule
import com.mahalatk.data.di.platformDataModule
import org.koin.core.context.startKoin

object KoinInitHelper {
    fun doInitKoin() {
        startKoin {
            modules(
                platformDataModule,
                commonDataModule,
                useCaseModule,
                sharedModule,
            )
        }
    }
}
