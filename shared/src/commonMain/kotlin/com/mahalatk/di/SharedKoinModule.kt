package com.mahalatk.di

import com.mahalatk.base.managers.LoadingManager
import com.mahalatk.base.managers.MessageManager
import com.mahalatk.base.managers.SessionManager
import com.mahalatk.fcm.FcmEventHandler
import com.mahalatk.features.auth.activation.ActivationViewModel
import com.mahalatk.features.auth.login.LoginViewModel
import com.mahalatk.features.auth.register.RegisterViewModel
import com.mahalatk.features.chat.ChatDetailViewModel
import com.mahalatk.features.chat.ChatViewModel
import com.mahalatk.features.home.HomeViewModel
import com.mahalatk.features.main.MainViewModel
import com.mahalatk.features.more.MoreViewModel
import com.mahalatk.features.notifications.NotificationsViewModel
import com.mahalatk.features.orders.OrdersViewModel
import com.mahalatk.features.products.ProductsViewModel
import com.mahalatk.features.splash.SplashViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val sharedModule = module {
    single { LoadingManager() }
    single { MessageManager() }
    single { SessionManager() }
    single { FcmEventHandler(get(), get()) }

    viewModel { MainViewModel(get(), get(), get()) }
    viewModel { SplashViewModel(get(), get(), get()) }
    viewModel { LoginViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { RegisterViewModel(get(), get()) }
    viewModel { MoreViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { ProductsViewModel() }
    viewModel { OrdersViewModel() }
    viewModel { ChatViewModel() }
    viewModel { ChatDetailViewModel() }
    viewModel { NotificationsViewModel() }
    viewModel { ActivationViewModel() }
}
