package com.example.tipsaredone.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tipsaredone.model.Employee
import java.time.LocalDate

class EmployeesViewModel: ViewModel() {

    private val _employees = MutableLiveData<MutableList<Employee>>(mutableListOf())
    val employees: LiveData<MutableList<Employee>> = _employees

    private val _startDate = MutableLiveData(LocalDate.now())
    val startDate: LiveData<LocalDate> = _startDate

    private val _endDate = MutableLiveData(LocalDate.now())
    val endDate: LiveData<LocalDate> = _endDate


    var newEmployeeDialogShowing: Boolean = false
    var editEmployeeDialogShowing: Boolean = false

    var datePickerDialogShowing: Boolean = false

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


    fun setStartDate(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        _startDate.value = LocalDate.of(year,monthOfYear,dayOfMonth)
    }
    fun setEndDate(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        _endDate.value = LocalDate.of(year,monthOfYear,dayOfMonth)
    }






}
