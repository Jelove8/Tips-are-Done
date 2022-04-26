package com.example.tipsaredone.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tipsaredone.model.Employee

class EmployeeViewModel: ViewModel() {

    private val _employeesList = MutableLiveData<MutableList<Employee>>()


}
