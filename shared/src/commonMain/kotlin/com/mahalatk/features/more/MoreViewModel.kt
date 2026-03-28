package com.mahalatk.features.more

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahalatk.base.UserDataProvider
import com.mahalatk.domain.repository.PreferenceRepository
import com.mahalatk.domain.util.TokenCacheManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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
    private val preferenceRepository: PreferenceRepository,
    private val tokenCacheManager: TokenCacheManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MoreState())
    val uiState: StateFlow<MoreState> = _uiState.asStateFlow()

    private val _loggedOut = MutableSharedFlow<Unit>()
    val loggedOut: SharedFlow<Unit> = _loggedOut.asSharedFlow()

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

    fun logout() {
        viewModelScope.launch {
            preferenceRepository.setUserData("")
            preferenceRepository.setToken("")
            tokenCacheManager.removeToken()
            preferenceRepository.setIsLogin(false)
            _loggedOut.emit(Unit)
        }
    }
}
