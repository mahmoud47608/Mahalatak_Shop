package com.mahalatk.di

import com.mahalatk.domain.usecase.auth.LoginUseCase
import com.mahalatk.domain.usecase.product.AddProductUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { LoginUseCase(get()) }
    factory { AddProductUseCase(get()) }
}
