package com.aait.base.common.screens.general.terms

import androidx.lifecycle.viewModelScope
import com.aait.base.base.BaseViewModel
import com.aait.base.common.screens.general.GeneralState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TermsViewModel @Inject constructor(
) : BaseViewModel<GeneralState>(GeneralState()) {

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {


        }
    }
}