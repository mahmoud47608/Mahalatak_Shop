package com.mahalatk

import androidx.compose.ui.window.ComposeUIViewController
import com.mahalatk.common.util.initializeLanguage
import com.mahalatk.fcm.FcmEventHandler
import com.mahalatk.fcm.IosFcmHandler
import com.mahalatk.features.main.App
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

fun MainViewController(): platform.UIKit.UIViewController {
    initializeLanguage()
    return ComposeUIViewController { App() }
}

object IosKoinHelper : KoinComponent {
    fun getFcmHandler(): IosFcmHandler = IosFcmHandler(get<FcmEventHandler>())
}
