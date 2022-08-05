package com.example.tipsaredone.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate

class TipCollectionViewModel : ViewModel() {

    private val _tipsCollected = MutableLiveData(mutableListOf(0.0,0.0,0.0,0.0,0.0,0.0,0.0))
    val tipsCollected: LiveData<MutableList<Double>> = _tipsCollected
    // Index: [0] Ones, [1] Twos, [2] Fives, [3] Tens, [4] Twenties, [5] Fifties, [6] Hundreds


    fun clearTipsCollected() {
        _tipsCollected.value = mutableListOf(0.0,0.0,0.0,0.0,0.0,0.0,0.0)
    }

    fun getTotalCollected(): Double {
        var output = 0.0
        _tipsCollected.value!!.forEach {
            output += it
        }
        return output
    }
}