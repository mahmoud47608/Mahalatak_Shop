package com.aait

import androidx.compose.ui.window.ComposeUIViewController
import com.aait.fcm.FcmEventHandler
import com.aait.fcm.IosFcmHandler
import com.aait.ui.navigation.App
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

fun MainViewController() = ComposeUIViewController { App() }

object IosKoinHelper : KoinComponent {
    fun getFcmHandler(): IosFcmHandler = IosFcmHandler(get<FcmEventHandler>())
}
