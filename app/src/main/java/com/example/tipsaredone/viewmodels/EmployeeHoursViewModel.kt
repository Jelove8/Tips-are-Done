package com.example.tipsaredone.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tipsaredone.model.Employee

class EmployeeHoursViewModel : ViewModel() {

    private val _employeeList = MutableLiveData<MutableList<Employee>>()

    private val _employeeHours = MutableLiveData<MutableList<Double>>(mutableListOf())

    fun setEmployeesList(list: MutableList<Employee>) {
        _employeeList.value = list
        for (i in 0 until _employeeList.value!!.size) {
            _employeeHours.value!!.add(0.00)
        }
    }
    fun setEmployeeHours(hours: Double, index: Int) {
        _employeeHours.value!![index] = hours
    }

}