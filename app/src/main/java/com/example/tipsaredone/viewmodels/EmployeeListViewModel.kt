package com.example.tipsaredone.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tipsaredone.model.Employee
import com.example.tipsaredone.model.MockData

class EmployeeListViewModel: ViewModel() {

    companion object {
        const val EMPLOYEE_VM = "empVM"
    }


    private val _employees = MutableLiveData<MutableList<Employee>>(MockData().getMockEmployees())  // Don't remove the type-argument
    val employees: LiveData<MutableList<Employee>> = _employees


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



}
