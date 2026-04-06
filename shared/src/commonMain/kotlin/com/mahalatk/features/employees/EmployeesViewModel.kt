package com.mahalatk.features.employees

import androidx.compose.runtime.Immutable
import com.mahalatk.base.SimpleViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

class EmployeesViewModel : SimpleViewModel<EmployeesState, Nothing>(
    EmployeesState(
        employees = persistentListOf(
            Employee("1", "أحمد محمد علي", "0512345678"),
            Employee("2", "خالد عبدالله", "0551234567"),
            Employee("3", "محمد سعيد", "0567891234"),
            Employee("4", "عمر حسن", "0534567890"),
            Employee("5", "فهد ابراهيم", "0598765432"),
        ),
    ),
) {

    fun acceptEmployee(id: String) {
        updateState { copy(employees = employees.filter { it.id != id }.toImmutableList()) }
    }

    fun rejectEmployee(id: String) {
        updateState { copy(employees = employees.filter { it.id != id }.toImmutableList()) }
    }
}

@Immutable
data class EmployeesState(
    val employees: ImmutableList<Employee> = persistentListOf(),
)
