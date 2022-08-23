package com.example.tipsaredone.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.LocalDateTime

class DatePickerViewModel : ViewModel() {



    private val _startDate = MutableLiveData(LocalDate.now())
    val startDate: LiveData<LocalDate> = _startDate

    private val _endDate = MutableLiveData(LocalDate.now())
    val endDate: LiveData<LocalDate> = _endDate

    fun setStartDate(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        _startDate.value = LocalDate.of(year,monthOfYear,dayOfMonth)
    }
    fun setEndDate(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        _endDate.value = LocalDate.of(year,monthOfYear,dayOfMonth)
    }



}