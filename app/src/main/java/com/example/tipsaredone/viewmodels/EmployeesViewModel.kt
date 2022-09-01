package com.example.tipsaredone.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tipsaredone.model.Employee

class EmployeesViewModel: ViewModel() {

    private val _employees = MutableLiveData<MutableList<Employee>>(mutableListOf())
    val employees: LiveData<MutableList<Employee>> = _employees

    var newEmployeeDialogShowing: Boolean = false

    var deleteEmployeeDialogShowing: Boolean = false

    private val _selectedEmployee = MutableLiveData<Employee?>(null)
    val selectedEmployee: LiveData<Employee?> = _selectedEmployee

    fun selectEmployee(index: Int) {
        _selectedEmployee.value = _employees.value!![index]
    }

    fun addEmployee(newEmployee: Employee) {
        var idCheck = true
        _employees.value!!.forEach {
            if (it.id == newEmployee.id) {
                idCheck = false
            }
        }

        if (idCheck) {
            _employees.value!!.add(newEmployee)
            _employees.value!!.sortBy { it.name }
        }
    }

}
