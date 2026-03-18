package com.aait.base.di

import com.aait.base.MainActivity
import com.aait.fcm.NotificationHandler
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single {
        NotificationHandler(
            context = androidContext(),
            preferenceRepository = get(),
            uiRepo = get(),
            notificationActivityClass = MainActivity::class.java
        )
    }
}
