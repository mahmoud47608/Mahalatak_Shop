package com.mahalatk

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import com.mahalatk.data.di.commonDataModule
import com.mahalatk.data.di.platformDataModule
import com.mahalatk.di.androidSharedModule
import com.mahalatk.di.appModule
import com.mahalatk.di.sharedModule
import com.mahalatk.di.useCaseModule
import com.mahalatk.fcm.NotificationChannelConfig
import com.mahalatk.ui.util.ApplicationContextHolder
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
                platformDataModule,
                commonDataModule,
                useCaseModule,
                sharedModule,
                androidSharedModule,
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
                NotificationChannelConfig.CHANNEL_ID,
                NotificationChannelConfig.CHANNEL_NAME,
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
}
