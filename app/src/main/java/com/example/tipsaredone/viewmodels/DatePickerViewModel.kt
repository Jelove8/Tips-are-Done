package com.example.tipsaredone.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDateTime

class DatePickerViewModel : ViewModel() {

    private val _startDate = MutableLiveData<LocalDateTime?>(null)
    val startDate: LiveData<LocalDateTime?> = _startDate

    private val _endDate = MutableLiveData<LocalDateTime?>(null)
    val endDate: LiveData<LocalDateTime?> = _endDate

    private lateinit var _startDateString: String
    private lateinit var _endDateString: String

    fun setStartDate(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        _startDate.value = LocalDateTime.of(year,monthOfYear,dayOfMonth,0,0)
        _startDateString = convertDateToString(_startDate.value!!)
    }
    fun setEndDate(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        _endDate.value = LocalDateTime.of(year,monthOfYear,dayOfMonth,0,0)
        _endDateString = convertDateToString(_endDate.value!!)
    }

    fun getStartDateString(): String {
        return _startDateString
    }
    fun getEndDateString(): String {
        return _endDateString
    }

    private fun convertDateToString(date: LocalDateTime): String {
        val monthString =
            if (date.monthValue < 10) {
                "0${date.monthValue}"
            } else {
                date.monthValue
            }
        val dayString =
            if (date.dayOfMonth < 10) {
                "0${date.dayOfMonth}"
            } else {
                date.dayOfMonth
            }
        return "${date.year}$monthString$dayString"
    }

}