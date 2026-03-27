package com.mahalatk.features.auth.activation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Immutable
data class ActivationState(
    val code: List<String> = listOf("", "", "", ""),
    val timerSeconds: Int = 60,
    val canResend: Boolean = false,
    val isVerifying: Boolean = false,
    val phoneNumber: String = "",
) {
    val fullCode: String get() = code.joinToString("")
    val isCodeComplete: Boolean get() = code.all { it.isNotEmpty() }
    val timerText: String
        get() {
            val min = timerSeconds / 60
            val sec = timerSeconds % 60
            return "${min.toString().padStart(2, '0')}:${sec.toString().padStart(2, '0')}"
        }
}

class ActivationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ActivationState())
    val uiState: StateFlow<ActivationState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    fun setPhoneNumber(phone: String) {
        _uiState.update { it.copy(phoneNumber = phone) }
        startTimer()
    }

    fun onDigitEntered(index: Int, digit: String) {
        if (digit.length > 1) return
        val newCode = _uiState.value.code.toMutableList()
        newCode[index] = digit
        _uiState.update { it.copy(code = newCode) }
    }

    fun verify() {
        if (!_uiState.value.isCodeComplete) return
        _uiState.update { it.copy(isVerifying = true) }
        // actual verification would go here
    }

    fun resendCode() {
        if (!_uiState.value.canResend) return
        _uiState.update {
            it.copy(
                code = listOf("", "", "", ""),
                timerSeconds = 60,
                canResend = false,
            )
        }
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_uiState.value.timerSeconds > 0) {
                delay(1000)
                _uiState.update { it.copy(timerSeconds = it.timerSeconds - 1) }
            }
            _uiState.update { it.copy(canResend = true) }
        }
    }
}
