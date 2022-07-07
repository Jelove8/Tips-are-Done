package com.example.tipsaredone.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tipsaredone.model.Employee
import com.example.tipsaredone.model.MockData
import com.example.tipsaredone.model.MyEmployees
import com.example.tipsaredone.views.MainActivity
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

class EmployeesViewModel: ViewModel() {

    init {
        Log.d("Initial","ViewModel initialized.")
    }

    companion object {
        const val EMPLOYEE_VM = "empVM"
    }

    private val _employees = MutableLiveData<MutableList<Employee>>(mutableListOf())
    val employees: LiveData<MutableList<Employee>> = _employees

    private val _sumHours = MutableLiveData(0.00)
    val sumHours: LiveData<Double> = _sumHours

    private var selectedEmployeePosition: Int = 0

    private var editingEmployee: Boolean = false    // false = adding an employee, true = editing an employee

    init {
        // Loading names from internal storage
        // Get json file -> mutableListOf<String>
        // for (name in mutableList) {
        //      addNewEmployee(name) }
    }

    // Adding a new employee.
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

    // Editing or Deleting an existing employee.
    fun getSelectedPosition(): Int {
        return selectedEmployeePosition
    }
    fun selectEmployee(index: Int) {
        selectedEmployeePosition = index
        Log.d("EmployeeList", "Selected Employee in VM @position: $selectedEmployeePosition")
    }
    fun deleteSelectedEmployee() {
        _employees.value!!.removeAt(selectedEmployeePosition)
        Log.d("debug","viewmodel function")

        Log.d("EmployeeList", "Deleted Employee in VM @position: $selectedEmployeePosition")
        Log.d("EmployeeList", "New Employee Order in VM")
        for ((i,emp) in employees.value!!.withIndex()) {
            Log.d("EmployeesList","$i ${emp.name}, ${emp.distributedTips} hrs")
        }
    }
    fun confirmSelectedEmployee(editedName: String) {
        _employees.value!![selectedEmployeePosition].name = editedName
        _employees.value!!.sortBy { it.name }

        Log.d("EmployeeList", "Edited Employee in VM: $selectedEmployeePosition")
        Log.d("EmployeeList", "New Employee Order in VM")
        for ((i,emp) in employees.value!!.withIndex()) {
            Log.d("EmployeesList","$i ${emp.name}, ${emp.distributedTips} hrs")
        }
    }

    // Sum of Hours
    fun setSumHours(double: Double) {
        _sumHours.value = double
        Log.d("EmployeesList","New Hours set in VM: $double")
    }

    // Clearing inputted hours & tips
    fun clearEmployeeHoursAndDistributedTips() {
        for (emp in _employees.value!!) {
            emp.distributedTips = 0.0
            emp.tippableHours = null
            setSumHours(0.00)
        }
    }

    // Discerning between editing vs adding an employee (both occur on the same dialog view).
    fun setEditingEmployeeBool(boolean: Boolean) {
        editingEmployee = boolean
        if (boolean) {
            Log.d("EmployeesList","Editing an Employee")
        }
        else {
            Log.d("EmployeesList","Adding an Employee")
        }

    }
    fun getEditingEmployeeBool(): Boolean {
        return editingEmployee
    }

    fun getSumHours(): Double {
        var output = 0.00
        for (emp in _employees.value!!) {
            if (emp.tippableHours != null) {
                output += emp.tippableHours.toString().toDouble() * 100
            }
        }
        return output / 100
    }

    // Internal Storage
    fun initializeEmployees(data: MutableList<Employee>) {
        _employees.value = data
        Log.d(MyEmployees.INTERNAL_STORAGE, "Employees loaded into ViewModel: $data")
    }


}
