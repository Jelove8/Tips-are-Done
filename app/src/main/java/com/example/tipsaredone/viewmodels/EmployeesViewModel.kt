package com.example.tipsaredone.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tipsaredone.model.Employee

class EmployeesViewModel: ViewModel() {

    private var initializeEmployeesFromInternalStorage = true
    private val _employees = MutableLiveData<MutableList<Employee>>(mutableListOf())
    val employees: LiveData<MutableList<Employee>> = _employees

    // Internal Storage
    fun loadDataFromInternalStorage(data: MutableList<Employee>) {
        if (initializeEmployeesFromInternalStorage) {
            _employees.value = data
            Log.d("Initial", "Employees loaded into ViewModel: $data")
            initializeEmployeesFromInternalStorage = false
        }
    }

    // Input Validity Checks
    fun checkForValidInputs(): Boolean {
        return if (_employees.value!!.size < 2) {
            false
        }
        else checkForNullHours()

    }
    fun getValidityString(): String {
        return if (_employees.value!!.size < 2) {
            "At least two employees must be entered."
        }
        else if (!checkForNullHours()) {
            "All hours must be filled."
        }
        else {
            "An error has occurred"
        }
    }
    private fun checkForNullHours(): Boolean {
        var bool = true
        for (emp in employees.value!!) {
            if (emp.tippableHours == null) {
                bool = false
                break
            }
        }
        return bool
    }

    // Other
    fun getSumHours(): Double {
        var output = 0.00
        for (emp in _employees.value!!) {
            if (emp.tippableHours != null) {
                output += emp.tippableHours.toString().toDouble() * 1000
            }
        }
        return output / 1000
    }
    fun clearEmployeeHoursAndDistributedTips() {
        for (emp in _employees.value!!) {
            emp.tips = 0.0
            emp.tippableHours = null
        }
    }
}
