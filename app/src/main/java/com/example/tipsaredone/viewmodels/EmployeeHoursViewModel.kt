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
    private val _endDate = MutableLiveData<String>()

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
                _startDate.value = yearString+ monthString + dayString
            }
            true -> {
                _endDate.value = yearString+ monthString + dayString
            }
        }
    }
    fun getDate(dateSelected: Boolean): String? {
        var output: String? = null
        var year: String
        var month: String
        var day: String

        when (dateSelected) {
            false -> {
                if (!_startDate.value.isNullOrBlank()) {

                    year = _startDate.value!![0].toString() + _startDate.value!![1].toString() + _startDate.value!![2].toString() + _startDate.value!![3].toString()
                    month = _startDate.value!![4].toString() + _startDate.value!![5].toString()
                    day = _startDate.value!![6].toString() + _startDate.value!![7].toString()

                    when (month) {
                        "01" -> {
                            month = "January"
                        }
                        "02" -> {
                            month = "February"
                        }
                        "03" -> {
                            month = "March"
                        }
                        "04" -> {
                            month = "April"
                        }
                        "05" -> {
                            month = "May"
                        }
                        "06" -> {
                            month = "June"
                        }
                        "07" -> {
                            month = "July"
                        }
                        "08" -> {
                            month = "August"
                        }
                        "09" -> {
                            month = "September"
                        }
                        "10" -> {
                            month = "October"
                        }
                        "11" -> {
                            month = "November"
                        }
                        "12" -> {
                            month = "December"
                        }
                    }

                    if (day.toInt() < 10) {
                        day = day.toInt().toString()
                    }

                    output = "$month $day, $year"

                }
            }
            true -> {
                if (!_endDate.value.isNullOrBlank()) {

                    year = _endDate.value!![0].toString() + _endDate.value!![1].toString() + _endDate.value!![2].toString() + _endDate.value!![3].toString()
                    month = _endDate.value!![4].toString() + _endDate.value!![5].toString()
                    day = _endDate.value!![6].toString() + _endDate.value!![7].toString()

                    when (month) {
                        "01" -> {
                            month = "January"
                        }
                        "02" -> {
                            month = "February"
                        }
                        "03" -> {
                            month = "March"
                        }
                        "04" -> {
                            month = "April"
                        }
                        "05" -> {
                            month = "May"
                        }
                        "06" -> {
                            month = "June"
                        }
                        "07" -> {
                            month = "July"
                        }
                        "08" -> {
                            month = "August"
                        }
                        "09" -> {
                            month = "September"
                        }
                        "10" -> {
                            month = "October"
                        }
                        "11" -> {
                            month = "November"
                        }
                        "12" -> {
                            month = "December"
                        }
                    }

                    if (day.toInt() < 10) {
                        day = day.toInt().toString()
                    }

                    output = "$month $day, $year"

                }
            }
        }

        return output
    }
    fun checkForNullDate(): Boolean {
        return when {
            _startDate.value.isNullOrEmpty() -> {
                false
            }
            _endDate.value.isNullOrEmpty() -> {
                false
            }
            else -> {
                true
            }
        }
    }

    



}