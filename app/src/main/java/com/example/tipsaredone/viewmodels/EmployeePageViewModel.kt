package com.example.tipsaredone.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tipsaredone.model.Employee

class EmployeePageViewModel : ViewModel() {

    private var selectedEmployee: Employee? = null

    fun setEmployee(employee: Employee?) {
        selectedEmployee = employee
    }
    fun getEmployee(): Employee? {
        return selectedEmployee
    }

}