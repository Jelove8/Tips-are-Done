package com.example.tipsaredone.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tipsaredone.model.Employee
import java.util.*
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.isNullOrEmpty
import kotlin.collections.mutableMapOf
import kotlin.collections.set

class EmployeeHoursViewModel : ViewModel() {

    private val _employeeList = MutableLiveData<MutableList<Employee>>()

    private val _employeeHours = MutableLiveData<MutableMap<String,Double?>>(mutableMapOf())
    val employeeHours: LiveData<MutableMap<String,Double?>> = _employeeHours

    private val _startDate = MutableLiveData<String>()
    val startDate: LiveData<String> = _startDate

    private val _endDate = MutableLiveData<String>()
    val endDate: LiveData<String> = _endDate


    fun setEmployeesList(list: MutableList<Employee>) {
        _employeeList.value = list

        // Setting map-values to null if there is no data
        if (_employeeHours.value!!.isNullOrEmpty()) {
            for (emp in _employeeList.value!!) {
                _employeeHours.value!![emp.id] = null
            }
        }
    }
    fun setEmployeeHours(hours: Double, id: String) {
        _employeeHours.value!![id] = hours
    }

    fun setDate(day: Int, month: Int, year: Int, dateSelected: Boolean) {
        var dayString = day.toString()
        var monthString = month.toString()
        val yearString = year.toString()

        if (day < 10) {
            dayString = "0$day"
        }
        if (month < 10) {
            monthString = "0$month"
        }

        when (dateSelected) {
            false -> {
                _startDate.value = dayString + monthString + yearString
                Log.d("Meow", "Start Date: ${_startDate.value!!}")
            }
            true -> {
                _endDate.value = dayString + monthString + yearString
                Log.d("Meow", "End Date: ${_endDate.value!!}")
            }
        }
    }



}