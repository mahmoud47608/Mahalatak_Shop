package com.aait.base.di

import com.aait.base.MainViewModel
import com.aait.base.fcm.NotificationHandler
import com.aait.base.fcm.test.FCMTestViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { MainViewModel(get(), get(), get()) }
    viewModel { FCMTestViewModel(get()) }
    single { NotificationHandler(androidContext(), get(), get()) }
}
