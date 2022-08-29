package com.example.tipsaredone.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tipsaredone.model.Employee
import com.example.tipsaredone.model.EmployeeHours
import com.example.tipsaredone.model.IndividualReport
import java.util.*

class EmployeesViewModel: ViewModel() {

    private val _employees = MutableLiveData<MutableList<Employee>>(mutableListOf())
    val employees: LiveData<MutableList<Employee>> = _employees

    private val _employeeHours = MutableLiveData<MutableList<EmployeeHours>>(mutableListOf())
    val employeeHours: LiveData<MutableList<EmployeeHours>> = _employeeHours


    private val _selectedEmployee = MutableLiveData<Employee?>(null)
    val selectedEmployee: LiveData<Employee?> = _selectedEmployee

    private val _newEmployeeDialogShowing = MutableLiveData(false)
    val newEmployeeDialogShowing: LiveData<Boolean> = _newEmployeeDialogShowing

    private val _deleteEmployeeDialogShowing = MutableLiveData(false)
    val deleteEmployeeDialogShowing: LiveData<Boolean> = _deleteEmployeeDialogShowing

    private val _confirmEmployeesButtonShowing = MutableLiveData(false)
    val confirmEmployeesButtonShowing: LiveData<Boolean> = _confirmEmployeesButtonShowing


    // Employee Hours
    fun initializeEmployeeHoursObjects() {
        if (_employees.value!!.isNotEmpty()) {
            _employees.value!!.forEach {
                _employeeHours.value!!.add(EmployeeHours(it.id,it.name,null))
            }
            _employeeHours.value!!.sortBy { it.name }
        }
    }

    fun selectEmployee(index: Int?) {
        _selectedEmployee.value =
            if (index == null) {
                null
            }
            else {
                _employees.value!![index]
            }
    }
    fun deleteSelectedEmployee() {
        _employees.value!!.remove(_selectedEmployee.value!!)
        _employeeHours.value!!.forEach {
            if (it.id == selectedEmployee.value!!.id) {
                _employeeHours.value!!.remove(it)
            }
        }
    }
    fun updateSelectedEmployeeName(updatedName: String) {
        _employees.value!!.forEach {
            if (it.id == _selectedEmployee.value!!.id) {
                it.name = updatedName
            }
        }
        _employeeHours.value!!.forEach {
            if (it.id == selectedEmployee.value!!.id) {
                it.name = updatedName
            }
        }
    }

    fun generateUniqueID(): String {
        var uniqueID = UUID.randomUUID().toString()

        _employees.value!!.forEach {
            if (uniqueID == it.id) {
                uniqueID = generateUniqueID()
            }
        }

        return uniqueID
    }


    fun setNewEmployeeDialogShowing(boolean: Boolean) {
        _newEmployeeDialogShowing.value = boolean
    }
    fun setDeleteEmployeeDialogShowing(boolean: Boolean) {
        _deleteEmployeeDialogShowing.value = boolean
    }
    fun setConfirmEmployeesButtonShowing(boolean: Boolean) {
        _confirmEmployeesButtonShowing.value = boolean
    }






}
