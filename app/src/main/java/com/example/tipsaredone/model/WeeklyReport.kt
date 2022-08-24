package com.example.tipsaredone.model

import android.util.Log
import com.example.tipsaredone.activities.MainActivity
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.util.*
import kotlin.math.absoluteValue

data class WeeklyReport(
    val startDate: String,
    val endDate: String,
    var individualReports: MutableList<IndividualTipReport> = mutableListOf(),
    var totalHours: Double = 0.0,
    var collectedTips: MutableList<Map<String,Int>> = mutableListOf(),
    var totalCollected: Double = 0.0,
    var tipRate: Double = 0.0,
    var majorRoundingError: Int = 0
    ) {

    init {
        Log.d(MainActivity.WEEKLY_REPORT,"New WeeklyReport created.")
    }

    fun initializeReports(data: MutableList<IndividualTipReport>) {
        var sumHours = 0.0
        data.forEach {
            it.startDate = startDate
            it.endDate = endDate
            individualReports.add(it)
            if (it.employeeHours != null) {
                sumHours += it.employeeHours!!
            }
        }
        individualReports.sortBy { it.employeeName }


        totalHours = sumHours

        Log.d(MainActivity.WEEKLY_REPORT,"Individual reports initialized.")
    }
    fun initializeCollectedTips(data: MutableList<Double>) {
        totalCollected = data.sum()
        val collectedTipsAsMap = mutableListOf<Map<String,Int>>()
        for ((i,item) in data.withIndex()) {
            when (i) {
                0 -> {
                    collectedTipsAsMap.add(mapOf(Pair("Ones",item.toInt())))
                }
                1 -> {
                    collectedTipsAsMap.add(mapOf(Pair("Twos",item.toInt())))
                }
                2 -> {
                    collectedTipsAsMap.add(mapOf(Pair("Fives",item.toInt())))
                }
                3 -> {
                    collectedTipsAsMap.add(mapOf(Pair("Tens",item.toInt())))
                }
                4 -> {
                    collectedTipsAsMap.add(mapOf(Pair("Twenties",item.toInt())))
                }
                5 -> {
                    collectedTipsAsMap.add(mapOf(Pair("Fifties",item.toInt())))
                }
                6 -> {
                    collectedTipsAsMap.add(mapOf(Pair("Hundreds",item.toInt())))
                }
            }
        }
        collectedTips = collectedTipsAsMap

        Log.d(MainActivity.WEEKLY_REPORT,"Collected tips initialized.")
    }

    fun distributeTips() {
        tipRate = totalCollected / totalHours

        for (report in individualReports) {
            val rawTips = if (report.employeeHours != null) {
                tipRate * report.employeeHours!!
            }
            else {
                0.0
            }
            val roundedTips = BigDecimal(rawTips).setScale(0, RoundingMode.HALF_EVEN)
            report.distributedTips = roundedTips.toDouble()
        }

        val expectedTotal = totalCollected
        var actualTotal = 0.0
        individualReports.forEach {
            actualTotal += it.distributedTips!!
        }

        val roundingError = actualTotal - expectedTotal
        val roundingErrorAbs = roundingError.absoluteValue

        if (roundingErrorAbs > 2.0) {
            majorRoundingError = roundingError.toInt()
        }
        else if (roundingErrorAbs > 0) {
            redistributeTips(roundingError)
        }

        individualReports.forEach {
            it.majorRoundingError = majorRoundingError
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