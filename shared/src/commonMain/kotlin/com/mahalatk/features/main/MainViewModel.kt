package com.mahalatk.features.main

import androidx.lifecycle.ViewModel
import com.mahalatk.base.managers.LoadingManager
import com.mahalatk.base.managers.MessageManager
import com.mahalatk.domain.util.TokenCacheManager

class MainViewModel(
    loadingManager: LoadingManager,
    messageManager: MessageManager,
    tokenCacheManager: TokenCacheManager,
) : ViewModel() {
    init {
        tokenCacheManager.refreshTokenCache()
    }

    val isLoading = loadingManager.isLoading
    val uiMessages = messageManager.messages
}