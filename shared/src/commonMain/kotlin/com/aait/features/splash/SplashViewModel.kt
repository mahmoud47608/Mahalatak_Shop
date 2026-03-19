package com.aait.features.splash

import com.aait.ui.base.BaseViewModel
import com.aait.ui.managers.LoadingManager
import com.aait.ui.managers.MessageManager

class SplashViewModel(
    loadingManager: LoadingManager,
    messageManager: MessageManager,
) : BaseViewModel<Unit>(Unit, loadingManager, messageManager)
