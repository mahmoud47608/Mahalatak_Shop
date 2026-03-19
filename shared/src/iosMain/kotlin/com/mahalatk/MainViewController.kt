package com.mahalatk

import androidx.compose.ui.window.ComposeUIViewController
import com.mahalatk.fcm.FcmEventHandler
import com.mahalatk.fcm.IosFcmHandler
import com.mahalatk.ui.navigation.App
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

fun MainViewController() = ComposeUIViewController { App() }

object IosKoinHelper : KoinComponent {
    fun getFcmHandler(): IosFcmHandler = IosFcmHandler(get<FcmEventHandler>())
}
