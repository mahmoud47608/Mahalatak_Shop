package com.mahalatk.features.more

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahalatk.domain.entity.AuthData
import com.mahalatk.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@Immutable
data class MoreState(
    val userName: String = "",
    val userImage: String = "",
)

class MoreViewModel(
    private val preferenceRepository: PreferenceRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MoreState())
    val uiState: StateFlow<MoreState> = _uiState.asStateFlow()

    private val json = Json { ignoreUnknownKeys = true }

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            val userData = preferenceRepository.getUserData().firstOrNull()
            if (!userData.isNullOrEmpty()) {
                try {
                    val authData = json.decodeFromString<AuthData>(userData)
                    val user = authData.user
                    _uiState.update {
                        it.copy(
                            userName = "${user?.firstName.orEmpty()} ${user?.lastName.orEmpty()}".trim(),
                            userImage = user?.image.orEmpty(),
                        )
                    }
                } catch (_: Exception) {
                    // ignore parse errors
                }
            }
        }
    }
}
