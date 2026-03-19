package com.aait.di

import com.aait.fcm.FcmEventHandler
import com.aait.features.auth.login.LoginViewModel
import com.aait.features.splash.SplashViewModel
import com.aait.ui.managers.LoadingManager
import com.aait.ui.managers.MessageManager
import com.aait.ui.managers.SessionManager
import com.aait.ui.navigation.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val sharedModule = module {
    single { LoadingManager() }
    single { MessageManager() }
    single { SessionManager() }
    single { FcmEventHandler(get(), get()) }

    viewModel { MainViewModel(get(), get(), get()) }
    viewModel { SplashViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get(), get(), get(), get(), get()) }
}
