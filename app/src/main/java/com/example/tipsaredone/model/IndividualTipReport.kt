package com.example.tipsaredone.model

import java.time.LocalDateTime

class IndividualTipReport(
    val employeeName: String,
    var employeeID: String,
    var employeeHours: Double,
    var distributedTips: Double,
    var startDate: LocalDateTime,
    var endDate: LocalDateTime,
    var majorRoundingError: Int?,
    var collected: Boolean
) {

    fun convertForStorage(): IndividualTipReportConvertedForStorage {
        val startDateString= startDate.year.toString() + startDate.monthValue.toString() + startDate.dayOfMonth.toString()
        val endDateString= endDate.year.toString() + endDate.monthValue.toString() + endDate.dayOfMonth.toString()
        return IndividualTipReportConvertedForStorage(
            employeeName,employeeID,employeeHours,distributedTips,
            startDateString,endDateString,
            majorRoundingError,collected
        )
    }


}
data class IndividualTipReportConvertedForStorage(
    val employeeName: String,
    var employeeID: String,
    var employeeHours: Double,
    var distributedTips: Double,
    var startDate: String,
    var endDate: String,
    var majorRoundingError: Int?,
    var collected: Boolean
)