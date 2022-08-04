package com.example.tipsaredone.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate

class TipCollectionViewModel : ViewModel() {

    private val _billsList = MutableLiveData(mutableListOf(0.0,0.0,0.0,0.0,0.0,0.0,0.0))
    val billsList: LiveData<MutableList<Double>> = _billsList
    // Index: [0] Ones, [1] Twos, [2] Fives, [3] Tens, [4] Twenties, [5] Fifties, [6] Hundreds


    fun clearBillsList() {
        _billsList.value = mutableListOf(0.0,0.0,0.0,0.0,0.0,0.0,0.0)
    }

    fun getSumOfBills(): Double {
        var output = 0.0
        _billsList.value!!.forEach {
            output += it
        }
        return output
    }
}