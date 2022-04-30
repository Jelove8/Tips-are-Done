package com.example.tipsaredone.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tipsaredone.model.Employee

class EmployeeHoursViewModel : ViewModel() {

    private val _employeeList = MutableLiveData<MutableList<Employee>>()

    private val _employeeHours = MutableLiveData<MutableList<Double?>>(mutableListOf())

    private val _map = MutableLiveData<MutableMap<String,Double?>>(mutableMapOf())

    fun getEmployeesList(): MutableList<Employee> {
        return _employeeList.value!!
    }
    fun setEmployeesList(list: MutableList<Employee>) {
        _employeeList.value = list
        for (emp in _employeeList.value!!) {
            _map.value!![emp.id] = null
        }
    }
    fun getEmployeeHours(): MutableList<Double?> {
        return _employeeHours.value!!
    }
    fun setEmployeeHours(hours: Double, id: String) {
        _map.value!![id] = hours
    }

    fun getMap() : MutableMap<String,Double?> {
        return _map.value!!
    }

    fun saveInputHours(id:String,hours:Double) {
        val currentKeyExists = _map.value!!.containsKey(id)
        if (currentKeyExists) {
            _map.value!![id] = hours
        }
    }
}