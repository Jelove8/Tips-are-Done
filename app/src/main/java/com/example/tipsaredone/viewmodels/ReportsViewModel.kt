package com.example.tipsaredone.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tipsaredone.model.Employee
import com.example.tipsaredone.model.WeeklyTipReport

class ReportsViewModel : ViewModel() {

    private val _weeklyReports = MutableLiveData<MutableList<WeeklyTipReport>>(mutableListOf())
    val weeklyReports: LiveData<MutableList<WeeklyTipReport>> = _weeklyReports

    fun setWeeklyReports(data: MutableList<WeeklyTipReport>) {
        _weeklyReports.value = data
    }

}