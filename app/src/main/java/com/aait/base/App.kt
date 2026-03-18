package com.aait.base

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import com.aait.base.di.appModule
import com.aait.base.di.useCaseModule
import com.aait.base.util.ApplicationContextHolder
import com.aait.data.di.dataStoreModule
import com.aait.data.di.endpointModule
import com.aait.data.di.networkModule
import com.aait.data.di.repositoryModule
import com.aait.data.di.stringsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    private lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()

        ApplicationContextHolder.init(this)

        startKoin {
            androidContext(this@App)
            modules(
                stringsModule,
                dataStoreModule,
                networkModule,
                endpointModule,
                repositoryModule,
                useCaseModule,
                appModule
            )
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)

        notificationManager =
            base.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        createAppChannel()
    }

    private fun createAppChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )

            val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            notificationChannel.setSound(
                defaultSound,
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build()
            )

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        const val CHANNEL_ID = "app_channel_id"
        const val CHANNEL_NAME = "app_channel"
    }
}
