package com.aait.di

import com.aait.ui.navigation.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val androidSharedModule = module {
    viewModel { MainViewModel(get(), get()) }
}
