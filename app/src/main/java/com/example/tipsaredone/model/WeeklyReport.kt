package com.example.tipsaredone.model

import android.util.Log
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.absoluteValue

data class WeeklyReport(
    val reportID: String,
    val startDate: String,
    val endDate: String,

    var collectedTips: MutableList<Map<String,Int>>,
    var totalHours: Double,
    var totalTips: Double,
    var tipRate: Double,
    var error: Int
    )