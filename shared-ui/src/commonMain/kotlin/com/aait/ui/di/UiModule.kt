package com.aait.ui.di

import com.aait.data.di.dataModules
import com.aait.domain.di.domainModules
import com.aait.ui.ui.UIRepo
import com.aait.ui.screens.auth.login.LoginViewModel
import com.aait.ui.screens.general.terms.TermsViewModel
import com.aait.ui.screens.splash.SplashViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    single { UIRepo() }
}

val viewModelModule = module {
    viewModel { LoginViewModel(get(), get(), get(), get(), get()) }
    viewModel { SplashViewModel(get(), get(), get()) }
    viewModel { TermsViewModel(get(), get(), get()) }
}

val uiModules: List<Module> = listOf(uiModule, viewModelModule)

val allSharedModules: List<Module> = domainModules + dataModules + uiModules
