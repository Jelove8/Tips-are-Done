package com.example.tipsaredone.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.LocalDateTime

class DatePickerViewModel : ViewModel() {

    private val _startDate = MutableLiveData<LocalDateTime?>(null)
    val startDate: LiveData<LocalDateTime?> = _startDate

    private val _endDate = MutableLiveData<LocalDateTime?>(null)
    val endDate: LiveData<LocalDateTime?> = _endDate

    fun setStartDate(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        _startDate.value = LocalDateTime.of(year,monthOfYear,dayOfMonth,0,0)
        Log.d("meow",_startDate.value.toString())
    }
    fun setEndDate(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        _endDate.value = LocalDateTime.of(year,monthOfYear,dayOfMonth,0,0)
        Log.d("meow",_endDate.value.toString())
    }

}