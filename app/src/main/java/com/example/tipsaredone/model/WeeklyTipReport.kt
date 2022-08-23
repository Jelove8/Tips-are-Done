package com.example.tipsaredone.model

import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
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

    private fun getSumHours(): Double {
        var sum = 0.0
        individualReports.forEach {
           sum += it.employeeHours!!
        }
        return sum
    }

    fun distributeTips() {
        tipRate = sumOfBills / getSumHours()

        for (report in individualReports) {
            val rawTips = tipRate * report.employeeHours!!
            val roundedTips = BigDecimal(rawTips).setScale(0, RoundingMode.HALF_EVEN)
            report.distributedTips = roundedTips.toDouble()
        }

        val expectedTotal = sumOfBills
        var actualTotal = 0.0
        individualReports.forEach {
            actualTotal += it.distributedTips!!
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
            firstEmployee.distributedTips = firstEmployee.distributedTips!! + 1.0
            secondEmployee.distributedTips = secondEmployee.distributedTips!! + 1.0
        }
        else if (roundingError > 0.0) {
            firstEmployee.distributedTips = firstEmployee.distributedTips!! - 1.0
            secondEmployee.distributedTips = secondEmployee.distributedTips!! - 1.0
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



