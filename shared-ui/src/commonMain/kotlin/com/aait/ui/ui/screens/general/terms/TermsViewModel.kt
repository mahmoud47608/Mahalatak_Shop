package com.aait.ui.screens.general.terms

import androidx.lifecycle.viewModelScope
import com.aait.domain.repository.PreferenceRepository
import com.aait.domain.util.TokenManager
import com.aait.ui.ui.UIRepo
import com.aait.ui.base.BaseViewModel
import com.aait.ui.screens.general.GeneralState
import kotlinx.coroutines.launch

class TermsViewModel(
    uiRepo: UIRepo,
    preferenceRepository: PreferenceRepository,
    tokenManager: TokenManager
) : BaseViewModel<GeneralState>(GeneralState(), uiRepo, preferenceRepository, tokenManager) {

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {

        }
    }
}
