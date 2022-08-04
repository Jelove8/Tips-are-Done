package com.example.tipsaredone.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tipsaredone.model.IndividualTipReport

class HoursViewModel: ViewModel() {

    private val _tipReports = MutableLiveData<MutableList<IndividualTipReport>>(mutableListOf())
    val tipReports: LiveData<MutableList<IndividualTipReport>> = _tipReports


    fun initializeTipReports(data: MutableList<IndividualTipReport>) {
        _tipReports.value = data
    }

    // Input Validity Checks


    // Other
    fun getSumOfHours(): Double {
        var output = 0.0
        _tipReports.value!!.forEach {
            if (it.employeeHours != null) {
                output += it.employeeHours!!
            }
        }
        output *= 100
        return output / 100
    }

}
