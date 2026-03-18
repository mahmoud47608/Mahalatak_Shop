package com.aait.di

import com.aait.domain.usecase.auth.LoginUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { LoginUseCase(get()) }
}
