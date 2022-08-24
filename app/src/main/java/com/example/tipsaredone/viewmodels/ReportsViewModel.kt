package com.example.tipsaredone.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tipsaredone.model.WeeklyReport

class ReportsViewModel : ViewModel() {

    private val _weeklyReports = MutableLiveData<MutableList<WeeklyReport>>(mutableListOf())
    val weeklyReports: LiveData<MutableList<WeeklyReport>> = _weeklyReports

    fun setWeeklyReports(data: MutableList<WeeklyReport>) {
        _weeklyReports.value = data
    }

}