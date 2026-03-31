package com.mahalatk.features.employees

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Immutable
data class EmployeesListState(
    val employees: List<Employee> = emptyList(),
)

class EmployeesListViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        EmployeesListState(
            employees = listOf(
                Employee("1", "أحمد محمد علي", "0512345678"),
                Employee("2", "خالد عبدالله", "0551234567"),
                Employee("3", "محمد سعيد", "0567891234"),
            ),
        ),
    )
    val uiState: StateFlow<EmployeesListState> = _uiState.asStateFlow()

    fun deleteEmployee(id: String) {
        _uiState.update { state ->
            state.copy(employees = state.employees.filter { it.id != id })
        }
    }
}
