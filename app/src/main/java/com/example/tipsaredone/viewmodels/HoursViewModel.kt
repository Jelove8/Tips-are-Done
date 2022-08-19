package com.example.tipsaredone.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tipsaredone.model.Employee
import com.example.tipsaredone.model.EmployeeHours
import com.example.tipsaredone.model.IndividualTipReport

class HoursViewModel: ViewModel() {

    private val _employeeHoursList = MutableLiveData<MutableList<EmployeeHours>>(mutableListOf())
    val employeeHoursList: LiveData<MutableList<EmployeeHours>> = _employeeHoursList


    fun initializeTipReports(data: MutableList<Employee>) {
        data.forEach {
            _employeeHoursList.value!!.add(EmployeeHours(it.name,it.id,null))
        }
        _employeeHoursList.value!!.sortBy { it.name }
    }

    // Input Validity Checks


}


