package com.mahalatk.di

import com.mahalatk.fcm.FcmEventHandler
import com.mahalatk.features.auth.login.LoginViewModel
import com.mahalatk.features.splash.SplashViewModel
import com.mahalatk.ui.managers.LoadingManager
import com.mahalatk.ui.managers.MessageManager
import com.mahalatk.ui.managers.SessionManager
import com.mahalatk.ui.navigation.MainViewModel
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
