package com.mahalatk.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahalatk.domain.repository.PreferenceRepository
import com.mahalatk.theme.AppColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preferenceRepository: PreferenceRepository,
) : ViewModel() {

    private val _isDarkMode = MutableStateFlow(AppColor.isDark)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    init {
        viewModelScope.launch {
            preferenceRepository.getDarkMode().collect { _isDarkMode.value = it }
        }
    }

    fun toggleDarkMode() {
        val newValue = !_isDarkMode.value
        _isDarkMode.value = newValue
        // Instant visual switch — AppColor.isDark is mutableStateOf, triggers recomposition immediately
        AppColor.isDark = newValue
        // Persist for next app launch
        viewModelScope.launch { preferenceRepository.setDarkMode(newValue) }
    }
}
