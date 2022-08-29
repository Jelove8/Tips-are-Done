package com.example.tipsaredone.model

class WeeklyReportGenerator(startDate: String, endDate: String) {

    companion object {
        const val WEEKLY_REPORTS = "Weekly Reports"
    }

    private val reportID: String

    private val relatedEmployeeIDs: MutableList<String> = mutableListOf()
    private val relatedIndividualReports: MutableList<IndividualReport> = mutableListOf()
    private val collectedTips: MutableList<Map<String,Int>> = mutableListOf()

    private val totalHours: Double = 0.0
    private val totalTips: Double = 0.0
    private val tipRate: Double = 0.0
    private val error: Int = 0

    init {
        reportID = "$startDate-$endDate"
    }


}