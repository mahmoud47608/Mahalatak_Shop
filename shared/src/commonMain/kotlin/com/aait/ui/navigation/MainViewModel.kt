package com.aait.ui.navigation

import androidx.lifecycle.ViewModel
import com.aait.domain.util.TokenCacheManager
import com.aait.ui.managers.LoadingManager
import com.aait.ui.managers.MessageManager

class MainViewModel(
    loadingManager: LoadingManager,
    messageManager: MessageManager,
    private val tokenCacheManager: TokenCacheManager,
) : ViewModel() {

    init {
        tokenCacheManager.refreshTokenCache()
    }

    val isLoading = loadingManager.isLoading
    val uiMessages = messageManager.messages
}
