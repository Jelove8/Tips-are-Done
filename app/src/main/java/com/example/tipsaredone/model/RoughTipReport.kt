package com.example.tipsaredone.model

import android.util.Log
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import kotlin.math.absoluteValue

class RoughTipReport(
    var employees: MutableList<Employee> = mutableListOf(),
    var bills: MutableList<Map<String,Int>> = mutableListOf(),
    var sumOfBills: Double = 0.0,
    var startDate: LocalDate? = null,
    var endDate: LocalDate? = null,
    var majorRoundingError: Int? = null,
    var tipRate: Double = 0.0
) {
    fun updateBills(data: MutableList<Double>) {
        for ((i,item) in data.withIndex()) {
            when (i) {
                0 -> {
                    bills.add(mapOf("Ones" to item.toInt()))
                }
                1 -> {
                    bills.add(mapOf("Twos" to item.toInt()))
                }
                2 -> {
                    bills.add(mapOf("Fives" to item.toInt()))
                }
                3 -> {
                    bills.add(mapOf("Tens" to item.toInt()))
                }
                4 -> {
                    bills.add(mapOf("Twenties" to item.toInt()))
                }
                5 -> {
                    bills.add(mapOf("Fifties" to item.toInt()))
                }
                6 -> {
                    bills.add(mapOf("Hundreds" to item.toInt()))
                }
            }
        }
        sumOfBills = data.sum()
    }
    private fun getSumHours(): Double {
        var sum = 0.0
        employees.forEach {
           sum += it.tippableHours!!
        }
        return sum
    }

    fun distributeTips() {
        tipRate = sumOfBills / getSumHours()

        for (emp in employees) {
            val rawTips = tipRate * emp.tippableHours!!
            val roundedTips = BigDecimal(rawTips).setScale(0, RoundingMode.HALF_EVEN)
            emp.tips = roundedTips.toDouble()
        }

        val expectedTotal = sumOfBills
        var actualTotal = 0.0
        employees.forEach {
            actualTotal += it.tips
        }

        val roundingError = actualTotal - expectedTotal
        val roundingErrorAbs = roundingError.absoluteValue

        if (roundingErrorAbs > 2.0) {
            majorRoundingError = roundingError.toInt()
        }
        else {
            redistributeTips(roundingError)
        }
    }
    private fun redistributeTips(roundingError: Double) {
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