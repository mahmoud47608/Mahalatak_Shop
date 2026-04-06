package com.mahalatk.features.auth.activation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.mahalatk.base.SimpleViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Immutable
data class ActivationState(
    val code: ImmutableList<String> = persistentListOf("", "", "", ""),
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

class ActivationViewModel : SimpleViewModel<ActivationState, Nothing>(ActivationState()) {

    private var timerJob: Job? = null

    fun setPhoneNumber(phone: String) {
        updateState { copy(phoneNumber = phone) }
        startTimer()
    }

    fun onDigitEntered(index: Int, digit: String) {
        if (digit.length > 1) return
        val newCode = uiState.value.code.toMutableList()
        newCode[index] = digit
        updateState { copy(code = newCode.toImmutableList()) }
    }

    fun verify() {
        if (!uiState.value.isCodeComplete) return
        updateState { copy(isVerifying = true) }
    }

    fun resendCode() {
        if (!uiState.value.canResend) return
        updateState {
            copy(
                code = persistentListOf("", "", "", ""),
                timerSeconds = 60,
                canResend = false,
            )
        }
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (uiState.value.timerSeconds > 0) {
                delay(1000)
                updateState { copy(timerSeconds = timerSeconds - 1) }
            }
            updateState { copy(canResend = true) }
        }
    }
}
