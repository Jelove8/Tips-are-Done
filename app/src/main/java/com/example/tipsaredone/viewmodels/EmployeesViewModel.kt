package com.example.tipsaredone.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tipsaredone.model.Employee
import com.example.tipsaredone.model.IndividualTipReport
import com.example.tipsaredone.model.MockData
import com.example.tipsaredone.model.NewEmployee
import java.time.LocalDate
import java.util.*

class EmployeesViewModel: ViewModel() {

    private val _employees = MutableLiveData<MutableList<NewEmployee>>(MockData().getMockEmployees())
    val employees: LiveData<MutableList<NewEmployee>> = _employees

    private val _selectedEmployee = MutableLiveData<NewEmployee?>(null)
    val selectedEmployee: LiveData<NewEmployee?> = _selectedEmployee

    private var _selectingDate: Boolean = false

    private val _startDate = MutableLiveData<LocalDate?>(null)
    val startDate: LiveData<LocalDate?> = _startDate

    private val _endDate = MutableLiveData<LocalDate?>(null)
    val endDate: LiveData<LocalDate?> = _endDate


    // Internal Storage
    /*
    fun loadDataFromInternalStorage(data: MutableList<Employee>) {
        if (initializeEmployeesFromInternalStorage) {
            _employees.value = data
            Log.d("Initial", "Employees loaded into ViewModel: $data")
            initializeEmployeesFromInternalStorage = false
        }
    }
     */

    fun generateNewTipReports(): MutableList<IndividualTipReport> {
        val output = mutableListOf<IndividualTipReport>()
        _employees.value!!.forEach {
            output.add(IndividualTipReport(it.name,it.id,null,null,_startDate.value!!,_endDate.value!!,null,false))
        }
        return output
    }
    fun selectEmployee(index: Int?) {
        _selectedEmployee.value =
            if (index == null) {
                null
            }
        else {
            _employees.value!![index]
            }
    }
    fun setSelectingDateBoolean(boolean: Boolean) {
        _selectingDate = boolean
    }
    fun getSelectingDateBoolean(): Boolean {
        return _selectingDate
    }
    fun setStartDate(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        _startDate.value = LocalDate.of(year,monthOfYear,dayOfMonth)
    }
    fun setEndDate(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        _endDate.value = LocalDate.of(year,monthOfYear,dayOfMonth)
    }
    fun checkForValidDates(): Boolean {
        return if (_startDate.value == null || _endDate.value == null) {
            false
        }
        else {
            _startDate.value!!.isBefore(_endDate.value!!)
        }
    }
    fun getDateValidityString(): String {
        return if (_startDate.value == null || _endDate.value == null) {
            "Both dates must be selected."
        }
        else if (_startDate.value!!.isAfter(_endDate.value!!)) {
            "The dates entered are invalid."
        }
        else {
            "An error has occurred."
        }
    }

    // Input Validity Checks
    fun checkForValidInputs(): Boolean {
        return _employees.value!!.size > 1
    }


    fun generateUniqueID(): String {
        var uniqueID = UUID.randomUUID().toString()

        _employees.value!!.forEach {
            if (uniqueID == it.id) {
                uniqueID = generateUniqueID()
            }
        }

        return uniqueID

    }

}
