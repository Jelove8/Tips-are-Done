package com.example.tipsaredone.model

class TipCalculations(
    private val employees: MutableList<Employee>,
    private val hours: MutableList<Double>,
    private val bills: MutableList<Int>
) {

    private val tipRate: Double

    init {
        val totalHours = hours.sum()
        val totalTips = bills.sum()
        tipRate = totalTips.toDouble() / totalHours
    }

    fun calculateTips(): MutableList<Double> {
        val tips = mutableListOf<Double>()

        for (hr in hours) {

            tips.add(tipRate * hr)

        }

        return tips

    }

}