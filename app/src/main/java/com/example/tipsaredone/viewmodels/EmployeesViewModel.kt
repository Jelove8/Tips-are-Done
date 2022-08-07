package com.example.tipsaredone.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tipsaredone.model.Employee
import java.util.*

class EmployeesViewModel: ViewModel() {

    private val _employees = MutableLiveData<MutableList<Employee>>(mutableListOf())
    val employees: LiveData<MutableList<Employee>> = _employees

    private val _selectedEmployee = MutableLiveData<Employee?>(null)
    val selectedEmployee: LiveData<Employee?> = _selectedEmployee

    private val _newEmployeeDialogShowing = MutableLiveData(false)
    val newEmployeeDialogShowing: LiveData<Boolean> = _newEmployeeDialogShowing

    private val _deleteEmployeeDialogShowing = MutableLiveData(false)
    val deleteEmployeeDialogShowing: LiveData<Boolean> = _deleteEmployeeDialogShowing


    fun initializeEmployees(data: MutableList<Employee>) {
        _employees.value = data
    }

    fun setNewEmployeeDialogShowing(boolean: Boolean) {
        _newEmployeeDialogShowing.value = boolean
    }

    fun setDeleteEmployeeDialogShowing(boolean: Boolean) {
        _deleteEmployeeDialogShowing.value = boolean
    }

    fun selectEmployee(index: Int?) {
        _selectedEmployee.value =
            if (index == null) {
                null
            }
        else {
            _employees.value!![index]
            }
    }

    fun generateUniqueID(): String {
        var uniqueID = UUID.randomUUID().toString()

        _employees.value!!.forEach {
            if (uniqueID == it.id) {
                uniqueID = generateUniqueID()
            }
        }

        return uniqueID

    }

}
