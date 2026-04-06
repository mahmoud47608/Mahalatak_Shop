package com.mahalatk.features.settings

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.mahalatk.base.SimpleViewModel
import com.mahalatk.domain.repository.PreferenceRepository
import com.mahalatk.theme.AppColor
import kotlinx.coroutines.launch

@Immutable
data class SettingsState(
    val isDarkMode: Boolean = AppColor.isDark,
)

class SettingsViewModel(
    private val preferenceRepository: PreferenceRepository,
) : SimpleViewModel<SettingsState, Nothing>(SettingsState()) {

    init {
        viewModelScope.launch {
            preferenceRepository.getDarkMode().collect { dark ->
                updateState { copy(isDarkMode = dark) }
            }
        }
    }

    fun toggleDarkMode() {
        val newValue = !uiState.value.isDarkMode
        updateState { copy(isDarkMode = newValue) }
        AppColor.isDark = newValue
        viewModelScope.launch { preferenceRepository.setDarkMode(newValue) }
    }
}
