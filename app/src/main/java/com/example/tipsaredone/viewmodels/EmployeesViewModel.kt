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

    private var initialUse = true

    private val _employees = MutableLiveData<MutableList<Employee>>(MockData().getMockEmployees())
    val employees: LiveData<MutableList<Employee>> = _employees

    private val _sumHours = MutableLiveData(0.00)
    val sumHours: LiveData<Double> = _sumHours


    private var selectedIndex: Int = 0

    // Adding a new employee
    private fun getRandom8CharString() : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..8)
            .map { allowedChars.random() }
            .joinToString("")
    }
    private fun generateUniqueID(): String {
        var newID = getRandom8CharString()
        for (employee in _employees.value!!) {
            if (employee.id.contains(newID)) {
                newID = generateUniqueID()
            }
        }
        return "employeeID_$newID"
    }
    fun addNewEmployee(newName: String) {

        if (newName[0].isLowerCase()) {
            newName[0].uppercase()
        }

        val uniqueID = generateUniqueID()
        _employees.value!!.add(
            Employee(uniqueID,newName)
        )

        _employees.value!!.sortBy { it.name }

        Log.d(EMPLOYEE_VM,"New Employee Added: $newName, $uniqueID")
    }

    // Editing or Deleting an existing employee
    fun selectEmployee(index: Int) {
        selectedIndex = index
    }
    fun getSelectedEmployee(): Employee {
        return _employees.value!![selectedIndex]
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

    // Initial Use
    fun setInitialUse(bool: Boolean) {
        initialUse = bool
    }
    fun getInitialUse(): Boolean {
        return initialUse
    }


    // Internal Storage
    fun loadDataFromInternalStorage(mainActivity: MainActivity) {

        // Initializes _employees with data within internal storage
        val employeesFromIntStorage: MutableList<Employee> = MyEmployees().loadEmployeesAsList(mainActivity)

        if (employeesFromIntStorage.isNullOrEmpty()) {
            _employees.value = mutableListOf()
            Log.d(INTERNAL_STORAGE, "EmployeesViewModel did not receive data from internal storage.")
        }
        else {
            _employees.value = employeesFromIntStorage
            Log.d(INTERNAL_STORAGE, "EmployeesViewModel receives data from internal storage.")
        }

    }
    
}
