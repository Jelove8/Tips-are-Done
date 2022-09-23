package com.example.tipsaredone.model

import android.util.Log
import com.example.tipsaredone.model.WeeklyReportGenerator.Companion.THIS
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.absoluteValue
import kotlin.time.Duration.Companion.hours

class WeeklyReportGenerator(private val startDate: String, private val endDate: String) {

    companion object {
        const val THIS = "WeeklyReports"
    }

    private val reportID: String = "$startDate-$endDate"

    private val employees = mutableListOf<Employee>()
    private val collectedTips = mutableListOf<Double>()

    var totalHours = 0.0
    var totalTips = 0.0
    var tipRate = 0.0
    var error = 0


    fun collectWeeklyEmployees(data: MutableList<Employee>) {
        data.forEach {
            employees.add(it)
            totalHours += it.hours!!
        }
        employees.sortBy { it.name }
    }
    fun collectWeeklyTips(data: MutableList<Double>) {
        data.forEach {
            collectedTips.add(it)
            totalTips += it
        }
    }

    fun distributeWeeklyTips() {
        tipRate = ((totalTips / totalHours) * 100).toInt() / 100.0

        for (emp in employees) {
            val rawTips = tipRate * emp.hours!!
            val roundedTips = BigDecimal(rawTips).setScale(0, RoundingMode.HALF_DOWN)
            emp.tips = roundedTips.toInt()
        }

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

    fun generateWeeklyReport(): WeeklyReport {
        return WeeklyReport(startDate,endDate,reportID,totalTips,totalHours,tipRate,error)
    }

}

class WeeklyReport(
    val startDate: String,
    val endDate: String,
    val reportID: String,
    val totalTips: Double,
    var totalHours: Double,
    val tipRate: Double,
    val error: Int
)

class TipReport() {

}