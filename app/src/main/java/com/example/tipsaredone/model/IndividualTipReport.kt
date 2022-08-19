package com.example.tipsaredone.model

import java.time.LocalDateTime

class IndividualTipReport(
    val employeeName: String,
    val employeeID: String,
    var employeeHours: Double? = null,
    var distributedTips: Double? = null,
    var startDate: String? = null,
    var endDate: String? = null,
    var majorRoundingError: Int? = null,
    var collected: Boolean? = null
)



