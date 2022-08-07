package com.example.tipsaredone.model

import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime
import kotlin.math.absoluteValue

class WeeklyTipReport(
    var individualReports: MutableList<IndividualTipReport> = mutableListOf(),
    var startDate: String? = null,
    var endDate: String? = null,
    var bills: MutableList<Map<String,Int>> = mutableListOf(),
    var sumOfBills: Double = 0.0,
    var tipRate: Double = 0.0,
    var majorRoundingError: Int? = null,

) {
    fun initializeIndividualReports(employees: MutableList<Employee>) {
        employees.sortBy { it.name }
        employees.forEach {
            individualReports.add(
                IndividualTipReport(it.name,it.id,0.0,0.0,startDate!!,endDate!!,null,false)
            )
        }

    }

    fun setTipsCollected(data: MutableList<Double>) {
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
        individualReports.forEach {
           sum += it.employeeHours
        }
        return sum
    }

    fun distributeTips() {
        tipRate = sumOfBills / getSumHours()

        for (report in individualReports) {
            val rawTips = tipRate * report.employeeHours
            val roundedTips = BigDecimal(rawTips).setScale(0, RoundingMode.HALF_EVEN)
            report.distributedTips = roundedTips.toDouble()
        }

        val expectedTotal = sumOfBills
        var actualTotal = 0.0
        individualReports.forEach {
            actualTotal += it.distributedTips
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
        var firstEmployee = IndividualTipReport("Template1","Template1",0.0,0.0, "","",null,false)
        var secondEmployee = IndividualTipReport("Template2","Template2",0.0,0.0, "","",null,false)

        val employeesCopy = individualReports.toMutableList()
        firstEmployee = employeesCopy.random()
        employeesCopy.remove(firstEmployee)
        secondEmployee = employeesCopy.random()

        if (roundingError < 0.0) {
            firstEmployee.distributedTips = firstEmployee.distributedTips + 1.0
            secondEmployee.distributedTips = secondEmployee.distributedTips + 1.0
        }
        else if (roundingError > 0.0) {
            firstEmployee.distributedTips = firstEmployee.distributedTips - 1.0
            secondEmployee.distributedTips = secondEmployee.distributedTips - 1.0
        }

        for (emp in individualReports) {
            if (emp == firstEmployee) {
                emp.distributedTips = firstEmployee.distributedTips
            }
            else if (emp == secondEmployee && roundingError.absoluteValue == 2.0) {
                emp.distributedTips = secondEmployee.distributedTips
            }
        }

    }

}



