package com.mahalatk.features.more

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahalatk.base.UserDataProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Immutable
data class MoreState(
    val userName: String = "",
    val userImage: String = "",
)

class MoreViewModel(
    private val userDataProvider: UserDataProvider,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MoreState())
    val uiState: StateFlow<MoreState> = _uiState.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    userName = userDataProvider.getUserName(),
                    userImage = userDataProvider.getUserImage(),
                )
            }
        }
    }
}
