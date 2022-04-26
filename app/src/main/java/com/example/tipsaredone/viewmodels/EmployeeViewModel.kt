package com.example.tipsaredone.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tipsaredone.model.Employee
import com.example.tipsaredone.model.MockData

class EmployeeViewModel: ViewModel() {

    private val _employeesList = MutableLiveData<MutableList<Employee>>(mutableListOf())

    val employeesList: LiveData<MutableList<Employee>>
        get() = _employeesList

   init {
       _employeesList.value!!.addAll(MockData().getMockEmployees())
   }
}
