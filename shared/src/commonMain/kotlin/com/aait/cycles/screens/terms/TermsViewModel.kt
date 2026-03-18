package com.aait.cycles.screens.terms

import androidx.lifecycle.viewModelScope
import com.aait.cycles.screens.general.GeneralState
import com.aait.domain.repository.PreferenceRepository
import com.aait.domain.util.TokenCacheManager
import com.aait.ui.UIRepo
import com.aait.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class TermsViewModel(
    uiRepo: UIRepo,
    preferenceRepository: PreferenceRepository,
    tokenCacheManager: TokenCacheManager
) : BaseViewModel<GeneralState>(GeneralState(), uiRepo, preferenceRepository, tokenCacheManager) {

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {


        }
    }
}
