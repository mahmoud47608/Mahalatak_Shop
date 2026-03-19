package com.mahalatk.di

import com.mahalatk.domain.usecase.auth.LoginUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { LoginUseCase(get()) }
}
