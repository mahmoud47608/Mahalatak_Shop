package com.mahalatk.di

import com.mahalatk.MainActivity
import com.mahalatk.fcm.NotificationHandler
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidSharedModule = module {
    // MainViewModel moved to sharedModule in commonMain
}

val appModule = module {
    single {
        NotificationHandler(
            context = androidContext(),
            fcmEventHandler = get(),
            notificationActivityClass = MainActivity::class.java,
        )
    }
}
