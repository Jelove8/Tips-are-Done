package com.example.tipsaredone.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tipsaredone.model.Employee
import com.example.tipsaredone.model.MockData

class TipsViewModel : ViewModel() {

    private val _billsList = MutableLiveData<MutableList<Double?>>(mutableListOf(null,null,null,null,null))
    // Index: [0] Ones, [1] Twos, [2] Fives, [3] Tens, [4] Twenties

    private val _totalTips = MutableLiveData(0.00)

    fun getBillsList(): MutableList<Double?> {
        return _billsList.value!!
    }
    fun updateBillAmount(index: Int, dollarAmount: Double?) {
        _billsList.value!![index] = dollarAmount
    }

    fun updateTotalTips(value: Double) {
        _totalTips.value = value
    }
    fun getTotalTips(): Double {
        return _totalTips.value!!
    }

}