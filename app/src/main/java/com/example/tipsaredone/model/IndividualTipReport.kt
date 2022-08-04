package com.example.tipsaredone.model

import android.util.Log
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import kotlin.math.absoluteValue

class IndividualTipReport(
    val employeeName: String,
    var employeeID: String,
    var employeeHours: Double?,
    var distributedTips: Double?,
    var startDate: LocalDate,
    var endDate: LocalDate,
    var majorRoundingError: Int?,
    var collected: Boolean
) {




}