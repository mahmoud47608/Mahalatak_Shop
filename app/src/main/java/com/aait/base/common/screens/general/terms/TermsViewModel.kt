package com.aait.base.common.screens.general.terms

import androidx.lifecycle.viewModelScope
import com.aait.base.base.BaseViewModel
import com.aait.base.common.screens.general.GeneralState
import kotlinx.coroutines.launch

class TermsViewModel : BaseViewModel<GeneralState>(GeneralState()) {

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {


        }
    }
}
