package com.aait.cycles.splash

import com.aait.domain.repository.PreferenceRepository
import com.aait.domain.util.TokenCacheManager
import com.aait.ui.UIRepo
import com.aait.ui.base.BaseViewModel

class SplashViewModel(
    uiRepo: UIRepo,
    preferenceRepository: PreferenceRepository,
    tokenCacheManager: TokenCacheManager
) : BaseViewModel<Unit>(Unit, uiRepo, preferenceRepository, tokenCacheManager)
