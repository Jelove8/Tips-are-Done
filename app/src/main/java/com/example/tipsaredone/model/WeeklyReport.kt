package com.example.tipsaredone.model

import android.util.Log
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.absoluteValue

data class WeeklyReport(
    val startDate: String,
    val endDate: String,
    var individualReports: MutableList<IndividualReport> = mutableListOf(),
    var totalHours: Double = 0.0,
    var collectedTips: MutableList<Map<String,Int>> = mutableListOf(),
    var totalCollected: Double = 0.0,
    var tipRate: Double = 0.0,
    var majorRoundingError: Int = 0
    ) {

    companion object {
        const val WEEKLY_REPORT = "WeeklyReport"
    }

    init {
        Log.d(WEEKLY_REPORT,"New weekly report created.")
    }

    fun collectTips(data: MutableList<Double>) {
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
        totalCollected = data.sum()
        Log.d(WEEKLY_REPORT,"Tips collected.")

        distributeTips()
    }

    private fun distributeTips() {
        tipRate = ((totalCollected / totalHours) * 100).toInt() / 100.0

        for (report in individualReports) {
            val rawTips = if (report.hours != null) {
                tipRate * report.hours!!
            } else {
                0.0
            }
            val roundedTips = BigDecimal(rawTips).setScale(0, RoundingMode.HALF_DOWN)
            report.distributedTips = roundedTips.toDouble()
        }
        Log.d(WEEKLY_REPORT,"Tips distributed.")
        checkForError()

    }
    private fun checkForError() {
        val expectedTotal = totalCollected
        var actualTotal = 0.0
        individualReports.forEach {
            actualTotal += it.distributedTips!!
        }

        val roundingError = actualTotal - expectedTotal
        val roundingErrorAbs = roundingError.absoluteValue
        majorRoundingError = roundingError.toInt()

        if (roundingError != 0.0) {
            Log.d(WEEKLY_REPORT,"Error found.")
        }

        individualReports.forEach {
            it.majorRoundingError = majorRoundingError
        }

        if (roundingErrorAbs > 0 && roundingErrorAbs < 3) {
            redistributeTips()
        }
    }
    private fun redistributeTips() {
        val listOfIDs = mutableListOf<String>()
        individualReports.forEach {
            listOfIDs.add(it.id)
        }

        val firstReportID = listOfIDs.random()
        listOfIDs.remove(firstReportID)
        val secondReportID = listOfIDs.random()
        listOfIDs.remove(secondReportID)

        if (majorRoundingError < 0) {
            if (majorRoundingError.absoluteValue == 1) {
                individualReports.forEach {
                    if (it.id == firstReportID) {
                        it.distributedTips!! + 1.0
                    }
                }
            } else if (majorRoundingError.absoluteValue == 2) {
                individualReports.forEach {
                    if (it.id == firstReportID) {
                        it.distributedTips!! + 1.0
                    }
                    else if (it.id == secondReportID) {
                        it.distributedTips!! + 1.0
                    }
                }
            }
        } else {
            if (majorRoundingError == 1) {
                individualReports.forEach {
                    if (it.id == firstReportID) {
                        it.distributedTips!! - 1.0
                    }
                }
            } else if (majorRoundingError == 2) {
                individualReports.forEach {
                    if (it.id == firstReportID) {
                        it.distributedTips!! - 1.0
                    }
                    else if (it.id == secondReportID) {
                        it.distributedTips!! - 1.0
                    }
                }
            }
        }
        Log.d(WEEKLY_REPORT,"Tips redistributed.")
        checkForError()
    }
}