package com.example.tipsaredone.model

import java.time.LocalDateTime

class IndividualTipReport(
    val employeeName: String,
    var employeeID: String,
    var employeeHours: Double,
    var distributedTips: Double,
    var startDate: String,
    var endDate: String,
    var majorRoundingError: Int?,
    var collected: Boolean
)



