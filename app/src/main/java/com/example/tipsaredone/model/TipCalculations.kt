package com.example.tipsaredone.model

import android.util.Log
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.absoluteValue
import kotlin.random.Random

class TipCalculations() {

    private var tipRate: Double = 0.00

    fun getTipRate(): Double {
        return tipRate
    }

    fun distributeTips(employees: MutableList<Employee>, sumOfHours: Double,sumOfBills: Double): Double? {

        tipRate = sumOfBills / sumOfHours

        for (emp in employees) {
            val rawTips = tipRate * emp.tippableHours!!
            val roundedTips = BigDecimal(rawTips).setScale(0, RoundingMode.HALF_EVEN)
            emp.tips = roundedTips.toDouble()
        }
        return checkForRoundingError(employees,sumOfBills)
    }

    private fun checkForRoundingError(employees: MutableList<Employee>, sumOfBills: Double): Double? {
        var roundedSum = 0.0
        employees.forEach {
            roundedSum += it.tips
        }

        val roundingError = roundedSum - sumOfBills
        val roundingErrorAbs = roundingError.absoluteValue


        return if (roundingErrorAbs == 0.0) {
            Log.d("TipCalculations","Tip redistribution unnecessary.")
            null
        }
        else if (roundingErrorAbs > 2.0) {
            roundingError
        }
        else {
            redistributeTips(employees, roundingError)
            Log.d("TipCalculations","Tips redistributed.")
            null
        }
    }

    private fun redistributeTips(employees: MutableList<Employee>, roundingError: Double) {
        var firstEmployee = Employee("Template1")
        var secondEmployee = Employee("Template2")

        val employeesCopy = employees.toMutableList()
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

        for (emp in employees) {
            if (emp == firstEmployee) {
                emp.tips = firstEmployee.tips
            }
            else if (emp == secondEmployee && roundingError.absoluteValue == 2.0) {
                emp.tips = secondEmployee.tips
            }
        }

    }

}