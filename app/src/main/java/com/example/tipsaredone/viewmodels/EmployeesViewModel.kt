package com.example.tipsaredone.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tipsaredone.model.Employee
import com.example.tipsaredone.model.MockData

class EmployeesViewModel: ViewModel() {

    companion object {
        const val INTERNAL_STORAGE = "internal_storage"
        const val EMPLOYEE_VM = "empVM"
    }

    private val _employees = MutableLiveData<MutableList<Employee>>(MockData().getMockEmployees())
    val employees: LiveData<MutableList<Employee>> = _employees

    private val _sumHours = MutableLiveData(0.00)
    val sumHours: LiveData<Double> = _sumHours

    private var selectedEmployeePosition: Int = 0

    private var editingEmployee: Boolean = false    // false = adding an employee, true = editing an employee

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
    fun selectEmployee(index: Int) {
        selectedEmployeePosition = index
    }
    fun deleteSelectedEmployee() {
        _employees.value!!.removeAt(selectedEmployeePosition)
    }
    fun confirmSelectedEmployee(editedName: String) {
        _employees.value!![selectedEmployeePosition].name = editedName
        _employees.value!!.sortBy { it.name }
    }

    // Sum of Hours
    fun setSumHours(double: Double) {
        _sumHours.value = double
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
    }
    fun getEditingEmployeeBool(): Boolean {
        return editingEmployee
    }
}
