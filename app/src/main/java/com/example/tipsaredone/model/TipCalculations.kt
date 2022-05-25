package com.example.tipsaredone.model

import android.util.Log

class TipCalculations() {

    private var tipRate: Double = 0.00
    private val rawDistributedTips = mutableListOf<Double>()

    fun getTipRate(totalHours: Double, totalTips: Double): Double {

        tipRate = totalTips / totalHours
        return tipRate

    }

    fun distributeTips(employees: MutableList<Employee>) {

        for (emp in employees) {
            val hours = emp.currentTippableHours
            val rawTips = tipRate * hours!!
            rawDistributedTips.add(rawTips)
            emp.distributedTips = rawTips
        }

    }

}