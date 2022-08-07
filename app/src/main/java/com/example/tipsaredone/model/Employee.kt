package com.example.tipsaredone.model

class Employee(
    var name: String,
    val id: String,
    val tipReports: MutableList<IndividualTipReport> = mutableListOf()
) {

    fun addTipReport(data: IndividualTipReport) {
        tipReports.add(data)
    }
    fun checkForUncollectedTips(): Double {
        var output = 0.0
        tipReports.forEach {
            if (!it.collected) {
                output += it.distributedTips
            }
        }
        return output
    }

}

