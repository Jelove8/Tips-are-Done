package com.example.tipsaredone.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tipsaredone.model.Employee
import kotlin.collections.set

class EmployeeHoursViewModel : ViewModel() {

    private val _employeeList = MutableLiveData<MutableList<Employee>>()

    private val _employeeHours = MutableLiveData<MutableMap<String,Double?>>(mutableMapOf())
    val employeeHours: LiveData<MutableMap<String,Double?>> = _employeeHours

    private val _startYear = MutableLiveData<Int>()
    private val _startMonth = MutableLiveData<Int>()
    private val _startDay = MutableLiveData<Int>()

    private val _endYear = MutableLiveData<Int>()
    private val _endMonth = MutableLiveData<Int>()
    private val _endDay = MutableLiveData<Int>()

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

    fun setDate(day: Int, month: Int, year: Int, selectStartDate: Boolean) {
        when (selectStartDate) {
            true -> {
                _startDay.value = day
                _startMonth.value = month
                _startYear.value = year
            }
            false -> {
                _endDay.value = day
                _endMonth.value = month
                _endYear.value = year
            }
        }
    }

    fun getDateString(selectStartDate: Boolean): String? {
        return if (!checkNullDates(selectStartDate)) {
            null
        } else {
            when (selectStartDate) {
                true -> {
                    getMonthString(_startMonth.value) + " ${_startDay.value}, ${_startYear.value}"
                }
                false -> {
                    getMonthString(_endMonth.value) + " ${_endDay.value}, ${_endYear.value}"
                }
            }
        }
    }
    private fun getMonthString(int: Int?): String? {
        return when (int) {
            0 -> {
                "January"
            }
            1 -> {
                "February"
            }
            2 -> {
                "March"
            }
            3 -> {
                "April"
            }
            4 -> {
                "May"
            }
            5 -> {
                "June"
            }
            6 -> {
                "July"
            }
            7 -> {
                "August"
            }
            8 -> {
                "September"
            }
            9 -> {
                "October"
            }
            10 -> {
                "November"
            }
            11 -> {
                "December"
            }
            else -> {
                null
            }
        }
    }
    private fun checkNullDates(selectStartDate: Boolean): Boolean {
        return when (selectStartDate) {
            true -> {
                !(_startDay.value == null || _startMonth.value == null || _startYear.value == null)
            }
            false -> {
                !(_endDay.value == null || _endMonth.value == null || _endYear.value == null)
            }
        }
    }

}