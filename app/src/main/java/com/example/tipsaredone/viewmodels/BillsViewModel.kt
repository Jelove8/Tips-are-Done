package com.example.tipsaredone.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BillsViewModel : ViewModel() {

    companion object {
        const val INPUT_TIPS = "input_tips"
    }

    private val _billsList = MutableLiveData(mutableListOf(0.0,0.0,0.0,0.0,0.0))
    val billsList: LiveData<MutableList<Double>> = _billsList
    // Index: [0] Ones, [1] Twos, [2] Fives, [3] Tens, [4] Twenties

    private var confirmButtonBoolean = false

    fun getBillsList(): MutableList<Double> {
        return _billsList.value!!
    }
    fun clearBillsList() {
        _billsList.value = mutableListOf(0.0,0.0,0.0,0.0,0.0)
    }

    fun getSumOfBills(): Double {
        var output = 0.0
        _billsList.value!!.forEach {
            output += it
        }
        return output
    }

    fun getConfirmButtonBool(): Boolean {
        return confirmButtonBoolean
    }

    fun checkForValidAmounts(): String {
        val sumOfModulos = (_billsList.value!![0] % 1.00) + (_billsList.value!![1] % 2.00) + (_billsList.value!![2] % 5.00) + (_billsList.value!![3] % 10.00) + (_billsList.value!![4] % 20.00)
        confirmButtonBoolean = sumOfModulos == 0.0

        return when {
            // At least one EditText input must be filled.
            _billsList.value!!.sum() == 0.0 -> {
                "No bills inputted."
            }
            // An input(s) is not divisible by their corresponding bill type.
            sumOfModulos != 0.0 -> {
                "Incorrect amount detected."
            }
            else -> {
                "Error"
            }
        }

    }

}