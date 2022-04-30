package com.example.tipsaredone.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tipsaredone.model.Employee

class EmployeeHoursViewModel : ViewModel() {

    private val _employeeList = MutableLiveData<MutableList<Employee>>()

    private val _employeeHours = MutableLiveData<MutableMap<String,Double?>>(mutableMapOf())
    val employeeHours: LiveData<MutableMap<String,Double?>> = _employeeHours

    fun getEmployeesList(): MutableList<Employee> {
        return _employeeList.value!!
    }
    fun setEmployeesList(list: MutableList<Employee>) {
        _employeeList.value = list

        // Setting map-values to null if there is no data
        if (_employeeHours.value!!.isNullOrEmpty()) {
            for (emp in _employeeList.value!!) {
                _employeeHours.value!![emp.id] = null
            }
        }
    }

    fun setEmployeeHours(hours: Double, id: String) {
        _employeeHours.value!![id] = hours
    }
}