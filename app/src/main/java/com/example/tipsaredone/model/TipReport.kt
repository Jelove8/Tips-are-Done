package com.example.tipsaredone.model

import android.util.Log
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.absoluteValue

class TipReport(
    private val _employees: MutableList<Employee>,
    private val _bills: MutableList<Double>,
    private var _tipRate: Double = 0.00,
    private var _error: Boolean? = null                  // null = No Redistribution, false = minor error (<$3) (Redistributed), true = major error (No Redistribution)
    ) {
    fun getTipRate(): Double {
        return _tipRate
    }

    fun distributeTips(sumOfHours: Double,sumOfBills: Double): Double? {

        _tipRate = sumOfBills / sumOfHours

        for (emp in _employees) {
            val rawTips = _tipRate * emp.tippableHours!!
            val roundedTips = BigDecimal(rawTips).setScale(0, RoundingMode.HALF_EVEN)
            emp.tips = roundedTips.toDouble()
        }

        return checkForRoundingError(sumOfBills)
    }
    private fun checkForRoundingError(sumOfBills: Double): Double? {
        var roundedSum = 0.0
        _employees.forEach {
            roundedSum += it.tips
        }

        val roundingError = roundedSum - sumOfBills
        val roundingErrorAbs = roundingError.absoluteValue

        return if (roundingErrorAbs == 0.0) {
            Log.d("TipReport","Tip redistribution unnecessary.")
            _error = null
            null
        }
        else if (roundingErrorAbs > 2.0) {
            _error = true
            roundingError
        }
        else {
            redistributeTips(roundingError)
            Log.d("TipReport","Tips redistributed.")
            _error = false
            null
        }
    }
    private fun redistributeTips(roundingError: Double) {
        var firstEmployee = Employee("Template1")
        var secondEmployee = Employee("Template2")

        val employeesCopy = _employees.toMutableList()
        firstEmployee = employeesCopy.random()
        employeesCopy.remove(firstEmployee)
        secondEmployee = employeesCopy.random()

        if (roundingError < 0.0) {
            firstEmployee.tips += 1.0
            secondEmployee.tips += 1.0
        }
        else if (roundingError > 0.0) {
            firstEmployee.tips -= 1.0
            secondEmployee.tips -= 1.0
        }

        for (emp in _employees) {
            if (emp == firstEmployee) {
                emp.tips = firstEmployee.tips
            }
            else if (emp == secondEmployee && roundingError.absoluteValue == 2.0) {
                emp.tips = secondEmployee.tips
            }
        }

    }

}