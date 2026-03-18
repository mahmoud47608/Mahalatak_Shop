package com.aait.ui.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.aait.domain.util.TokenCacheManager
import com.aait.ui.UIRepo

class MainViewModel(
    uiRepo: UIRepo,
    private val tokenCacheManager: TokenCacheManager,
) : ViewModel() {

    init {
        refreshTokenCache()
    }

    val navigationStack = mutableStateListOf<NavScreen>(SplashNavKey)

    val isLoading = uiRepo.isLoading
    val uiMessages = uiRepo.uiMessage


    fun refreshTokenCache() {
        tokenCacheManager.refreshTokenCache()
    }
}
