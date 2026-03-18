package com.aait.base

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.aait.base.ui.UIRepo
import com.aait.base.ui.navigation.NavScreen
import com.aait.base.ui.navigation.SplashNavKey
import com.aait.domain.util.TokenCacheManager

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
