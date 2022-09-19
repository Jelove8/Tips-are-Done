package com.example.tipsaredone.model

import android.util.Log
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.absoluteValue

class WeeklyReportGenerator(val startDate: String, val endDate: String) {

    companion object {
        const val THIS = "WeeklyReports"
    }

    init {
        Log.d(THIS,"Weekly Report Generator initialized.")
    }

    private val reportID: String = "$startDate-$endDate"

    private val employees: MutableList<Employee> = mutableListOf()
    private val collectedTips: MutableList<Map<String,Int>> = mutableListOf()

    private var totalHours: Double = 0.0
    private var totalTips: Double = 0.0
    private var tipRate: Double = 0.0
    private var error: Int = 0

    fun setWeeklyEmployees(data: MutableList<Employee>) {
        data.forEach {
            if (it.hours != null) {
                employees.add(it)
                totalHours += it.hours!!
            }
        }
        employees.sortBy { it.name }
        Log.d(THIS,"Employees and hours set.")
    }
    fun collectWeeklyTips(weeklyCollection: MutableList<Double>) {
        for ((i,item) in weeklyCollection.withIndex()) {
            when (i) {
                0 -> {
                    collectedTips.add(mapOf(Pair("Ones",item.toInt())))
                }
                1 -> {
                    collectedTips.add(mapOf(Pair("Twos",item.toInt())))
                }
                2 -> {
                    collectedTips.add(mapOf(Pair("Fives",item.toInt())))
                }
                3 -> {
                    collectedTips.add(mapOf(Pair("Tens",item.toInt())))
                }
                4 -> {
                    collectedTips.add(mapOf(Pair("Twenties",item.toInt())))
                }
                5 -> {
                    collectedTips.add(mapOf(Pair("Fifties",item.toInt())))
                }
                6 -> {
                    collectedTips.add(mapOf(Pair("Hundreds",item.toInt())))
                }
            }
        }
        totalTips = weeklyCollection.sum()
        Log.d(THIS,"Tips collected.")
    }
    fun distributeWeeklyTips() {
        tipRate = ((totalTips / totalHours) * 100).toInt() / 100.0

        for (emp in employees) {
            val rawTips = tipRate * emp.hours!!
            val roundedTips = BigDecimal(rawTips).setScale(0, RoundingMode.HALF_DOWN)
            emp.tips = roundedTips.toInt()
        }
        Log.d(THIS,"Tips distributed.")

        if (!checkForError()) {
            redistributeTips()
        }
    }
    private fun redistributeTips(){
        val listOfIDs = mutableListOf<String>()
        employees.forEach {
            listOfIDs.add(it.id)
        }

        val firstReportID = listOfIDs.random()
        listOfIDs.remove(firstReportID)
        val secondReportID = listOfIDs.random()
        listOfIDs.remove(secondReportID)

        if (error < 0) {
            if (error.absoluteValue == 1) {
                employees.forEach {
                    if (it.id == firstReportID) {
                        it.tips!! + 1.0
                    }
                }
            } else if (error.absoluteValue == 2) {
                employees.forEach {
                    if (it.id == firstReportID) {
                        it.tips!! + 1.0
                    }
                    else if (it.id == secondReportID) {
                        it.tips!! + 1.0
                    }
                }
            }
        } else {
            if (error == 1) {
                employees.forEach {
                    if (it.id == firstReportID) {
                        it.tips!! - 1.0
                    }
                }
            } else if (error == 2) {
                employees.forEach {
                    if (it.id == firstReportID) {
                        it.tips!! - 1.0
                    }
                    else if (it.id == secondReportID) {
                        it.tips!! - 1.0
                    }
                }
            }
        }
        Log.d(THIS,"Tips redistributed.")

        if (!checkForError()) {
            redistributeTips()
        }
    }
    private fun checkForError(): Boolean {
        val expectedTotal = totalTips
        var actualTotal = 0.0
        employees.forEach {
            actualTotal += it.tips!!
        }

        val roundingError = actualTotal - expectedTotal
        val roundingErrorAbs = roundingError.absoluteValue
        error = roundingError.toInt()

        if (roundingError != 0.0) {
            Log.d(THIS,"Error found.")
        }

        return !(roundingErrorAbs > 0 && roundingErrorAbs < 3)
    }

    fun getWeeklyReport(): WeeklyReport {
        return WeeklyReport(reportID, startDate, endDate, collectedTips, totalHours, totalTips, tipRate, error)
    }

}