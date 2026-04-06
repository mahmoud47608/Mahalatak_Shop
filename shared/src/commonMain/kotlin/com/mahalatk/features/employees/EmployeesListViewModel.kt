package com.mahalatk.features.employees

import androidx.compose.runtime.Immutable
import com.mahalatk.base.SimpleViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Immutable
data class EmployeesListState(
    val employees: ImmutableList<Employee> = persistentListOf(),
)

class EmployeesListViewModel : SimpleViewModel<EmployeesListState, Nothing>(
    EmployeesListState(
        employees = persistentListOf(
            Employee("1", "أحمد محمد علي", "0512345678"),
            Employee("2", "خالد عبدالله", "0551234567"),
            Employee("3", "محمد سعيد", "0567891234"),
        ),
    ),
) {

    fun deleteEmployee(id: String) {
        updateState { copy(employees = employees.filter { it.id != id }.toImmutableList()) }
    }
}
