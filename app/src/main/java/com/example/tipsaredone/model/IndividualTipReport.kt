package com.example.tipsaredone.model

import kotlinx.serialization.Serializable

data class IndividualTipReport (
    var employeeName: String,
    val employeeID: String,
    var employeeHours: Double? = null,
    var distributedTips: Double? = null,
    var startDate: String? = null,
    var endDate: String? = null,
    var majorRoundingError: Int? = null,
    var collected: Boolean? = null
)



