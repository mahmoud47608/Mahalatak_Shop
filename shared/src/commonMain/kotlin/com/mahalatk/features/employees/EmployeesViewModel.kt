package com.mahalatk.features.employees

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EmployeesViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        EmployeesState(
            employees = listOf(
                Employee("1", "أحمد محمد علي", "0512345678"),
                Employee("2", "خالد عبدالله", "0551234567"),
                Employee("3", "محمد سعيد", "0567891234"),
                Employee("4", "عمر حسن", "0534567890"),
                Employee("5", "فهد ابراهيم", "0598765432"),
            ),
        ),
    )
    val uiState: StateFlow<EmployeesState> = _uiState.asStateFlow()

    fun acceptEmployee(id: String) {
        _uiState.update { state ->
            state.copy(employees = state.employees.filter { it.id != id })
        }
    }

    fun rejectEmployee(id: String) {
        _uiState.update { state ->
            state.copy(employees = state.employees.filter { it.id != id })
        }
    }
}

@Immutable
data class EmployeesState(
    val employees: List<Employee> = emptyList(),
)
