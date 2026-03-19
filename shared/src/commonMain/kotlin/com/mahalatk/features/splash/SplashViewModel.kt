package com.mahalatk.features.splash

import com.mahalatk.ui.base.BaseViewModel
import com.mahalatk.ui.managers.LoadingManager
import com.mahalatk.ui.managers.MessageManager

class SplashViewModel(
    loadingManager: LoadingManager,
    messageManager: MessageManager,
) : BaseViewModel<Unit>(Unit, loadingManager, messageManager)
