package com.aait.ui.screens.splash

import com.aait.domain.repository.PreferenceRepository
import com.aait.domain.util.TokenManager
import com.aait.ui.ui.UIRepo
import com.aait.ui.base.BaseViewModel

class SplashViewModel(
    uiRepo: UIRepo,
    preferenceRepository: PreferenceRepository,
    tokenManager: TokenManager
) : BaseViewModel<Unit>(Unit, uiRepo, preferenceRepository, tokenManager)
