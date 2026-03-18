package com.aait.di

import com.aait.cycles.auth.login.LoginViewModel
import com.aait.cycles.splash.SplashViewModel
import com.aait.ui.UIRepo
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val sharedModule = module {
    single { UIRepo() }
    viewModel { LoginViewModel(get(), get(), get(), get()) }
    viewModel { SplashViewModel(get(), get(), get()) }
}
