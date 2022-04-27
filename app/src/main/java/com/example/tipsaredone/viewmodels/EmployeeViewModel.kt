package com.example.tipsaredone.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tipsaredone.model.Employee
import com.example.tipsaredone.model.MockData

class EmployeeViewModel: ViewModel() {

    companion object {
        const val EMPLOYEE_VM = "empVM"
    }

    private val _employeesList = MutableLiveData(MockData().getMockEmployees())
    val employeesList: LiveData<MutableList<Employee>> = _employeesList

    private fun getRandom8CharString() : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..8)
            .map { allowedChars.random() }
            .joinToString("")
    }
    private fun generateUniqueID(): String {
        var newID = getRandom8CharString()
        for (employee in _employeesList.value!!) {
            if (employee.id.contains(newID)) {
                newID = generateUniqueID()
            }
        }
        return "employeeID_$newID"
    }
    fun addNewEmployee(newName: String) {
        val uniqueID = generateUniqueID()
        _employeesList.value!!.add(
            Employee(uniqueID,newName)
        )

        _employeesList.value!!.sortBy { it.name }

        Log.d(EMPLOYEE_VM,"New Employee Added: $newName, $uniqueID")
    }

    val selectedEmployeeIndex = MutableLiveData<Int>()
    fun getSelectedEmployee(): Employee {
        return _employeesList.value!![selectedEmployeeIndex.value!!]
    }
    fun deleteSelectedEmployee() {
        Log.d(EMPLOYEE_VM, "Deleted Employee: ${getSelectedEmployee().name}, ${getSelectedEmployee().id}")
        _employeesList.value!!.remove(getSelectedEmployee())

    }
    fun confirmEdits(editedName: String) {
        _employeesList.value!![selectedEmployeeIndex.value!!].name = editedName
    }
}
