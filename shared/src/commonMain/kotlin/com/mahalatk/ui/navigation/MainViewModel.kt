package com.mahalatk.ui.navigation

import androidx.lifecycle.ViewModel
import com.mahalatk.domain.util.TokenCacheManager
import com.mahalatk.ui.managers.LoadingManager
import com.mahalatk.ui.managers.MessageManager

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
