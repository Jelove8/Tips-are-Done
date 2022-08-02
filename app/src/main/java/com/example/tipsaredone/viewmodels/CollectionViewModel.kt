package com.example.tipsaredone.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tipsaredone.model.Employee
import kotlinx.coroutines.flow.combineTransform
import java.time.LocalDate
import java.util.*

class CollectionViewModel : ViewModel() {


    private val _billsList = MutableLiveData(mutableListOf(0.0,0.0,0.0,0.0,0.0,0.0,0.0))
    val billsList: LiveData<MutableList<Double>> = _billsList
    // Index: [0] Ones, [1] Twos, [2] Fives, [3] Tens, [4] Twenties

    private val _startDate = MutableLiveData<LocalDate?>(null)
    val startDate: LiveData<LocalDate?> = _startDate

    private val _endDate = MutableLiveData<LocalDate?>(null)
    val endDate: LiveData<LocalDate?> = _endDate

    private var confirmButtonBoolean = false

    fun clearBillsList() {
        _billsList.value = mutableListOf(0.0,0.0,0.0,0.0,0.0,0.0,0.0)
    }

    fun setStartDate(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        _startDate.value = LocalDate.of(year,monthOfYear,dayOfMonth)
    }
    fun setEndDate(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        _endDate.value = LocalDate.of(year,monthOfYear,dayOfMonth)
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

    fun getValidityString(): String {
        val sumOfModulos = (_billsList.value!![0] % 1.00) + (_billsList.value!![1] % 2.00) + (_billsList.value!![2] % 5.00) + (_billsList.value!![3] % 10.00) + (_billsList.value!![4] % 20.00) + (_billsList.value!![5] % 50.00) + (_billsList.value!![6] % 100.00)
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
            _startDate.value == null || _endDate.value == null -> {
                "Both dates must be filled out."
            }
            else -> {
                "An error has occurred."
            }
        }

    }

    fun checkForValidInputs(): Boolean {
        val sumOfModulos = (_billsList.value!![0] % 1.00) + (_billsList.value!![1] % 2.00) + (_billsList.value!![2] % 5.00) + (_billsList.value!![3] % 10.00) + (_billsList.value!![4] % 20.00) + (_billsList.value!![5] % 50.00) + (_billsList.value!![6] % 100.00)
        return sumOfModulos == 0.0
    }

    fun checkForValidDates(): Boolean {
        return _startDate.value != null && _endDate.value != null
    }

}