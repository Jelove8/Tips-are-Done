package com.example.tipsaredone.viewmodels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tipsaredone.model.Employee
import com.example.tipsaredone.model.EmployeeHours

class HoursViewModel : ViewModel() {

    private val _employeeHours = MutableLiveData<MutableList<EmployeeHours>>(mutableListOf())
    val employeeHours: LiveData<MutableList<EmployeeHours>> = _employeeHours

    var datePickerDialogShowing = false


    fun addNewEmployee(employee: Employee) {
        var check = true
        _employeeHours.value!!.forEach {
            if (it.id == employee.id) {
                check = false
            }
        }
        if (check) {
            _employeeHours.value!!.add(EmployeeHours(employee.id,employee.name,null))
        }
    }
}