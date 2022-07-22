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

    private var initializeEmployeesBool = true
    private var confirmButtonEnabled = false

    private val _employees = MutableLiveData<MutableList<Employee>>(mutableListOf())
    val employees: LiveData<MutableList<Employee>> = _employees

    private var selectedEmployeePosition: Int = 0

    private var editingEmployee: Boolean = false    // false = adding a new employee, true = editing an employee


    // Editing or Deleting an existing employee.
    fun getSelectedPosition(): Int {
        return selectedEmployeePosition
    }
    fun selectEmployee(index: Int) {
        selectedEmployeePosition = index
    }


    // Clearing inputted hours & tips
    fun clearEmployeeHoursAndDistributedTips() {
        for (emp in _employees.value!!) {
            emp.distributedTips = 0.0
            emp.tippableHours = 0.0
        }
    }

    // Discerning between editing vs adding an employee (both occur on the same dialog view).
    fun setEditingEmployeeBool(boolean: Boolean) {
        editingEmployee = boolean
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
        if (initializeEmployeesBool) {
            _employees.value = data
            Log.d("Initial", "Employees loaded into ViewModel: $data")
            initializeEmployeesBool = false
        }
    }

    fun getConfirmButtonBool(): Boolean {
        return confirmButtonEnabled
    }
    fun setConfirmButtonBool(bool: Boolean) {
        confirmButtonEnabled = bool
    }

    fun checkForValidInputs(): String {

        confirmButtonEnabled = !(_employees.value!!.size < 2 || !checkForNullHours())

        return if (_employees.value!!.size < 2) {
            "At least two employees must be entered."
        }
        else if (!checkForNullHours()) {
            "All hours must be filled."
        }
        else {
            "Error"
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




}
