package com.example.tipsaredone.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tipsaredone.model.Employee
import com.example.tipsaredone.model.MockData
import com.example.tipsaredone.model.MyEmployees
import com.example.tipsaredone.views.MainActivity

class EmployeesViewModel: ViewModel() {

    companion object {
        const val INTERNAL_STORAGE = "internal_storage"
        const val EMPLOYEE_VM = "empVM"
    }


    private val _employees = MutableLiveData<MutableList<Employee>>(MockData().getMockEmployees())
    val employees: LiveData<MutableList<Employee>> = _employees

    private val _sumHours = MutableLiveData(0.00)
    val sumHours: LiveData<Double> = _sumHours

    private var selectedIndex: Int = 0



    // Adding a new employee
    fun addNewEmployee(newName: String) {

        if (newName[0].isLowerCase()) {
            newName[0].uppercase()
        }

        _employees.value!!.add(
            Employee(newName)
        )

        _employees.value!!.sortBy { it.name }

        Log.d(EMPLOYEE_VM,"New Employee Added: $newName")
    }

    // Editing or Deleting an existing employee
    fun selectEmployee(index: Int) {
        selectedIndex = index
    }
    fun deleteSelectedEmployee() {
        _employees.value!!.removeAt(selectedIndex)
    }
    fun confirmSelectedEmployee(editedName: String) {
        _employees.value!![selectedIndex].name = editedName
        _employees.value!!.sortBy { it.name }
    }

    // Employee Hours
    fun setSumHours(double: Double) {
        _sumHours.value = double
    }

    // Clearing inputted hours & tips
    fun clearEmployeeHoursAndDistributedTips() {
        for (emp in _employees.value!!) {
            emp.distributedTips = 0.0
            emp.tippableHours = null
        }
    }

    // Internal Storage

    
}
