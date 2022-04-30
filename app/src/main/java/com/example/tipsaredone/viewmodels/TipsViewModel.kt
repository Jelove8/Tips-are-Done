package com.example.tipsaredone.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tipsaredone.model.Employee
import com.example.tipsaredone.model.MockData

class TipsViewModel : ViewModel() {

    private val _billsList = MutableLiveData<MutableList<Int?>>(mutableListOf(null,null,null,null,null))
    // Index: [0] Ones, [1] Twos, [3] Fives, [4] Tens, [5] Twenties

    fun getBillsList(): MutableList<Int?> {
        return _billsList.value!!
    }
    fun updateBillAmount(index: Int, dollarAmount: Int?) {
        _billsList.value!![index] = dollarAmount
    }

}