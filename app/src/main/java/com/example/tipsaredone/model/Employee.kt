package com.example.tipsaredone.model

class Employee(
    var name: String,
    val id: String,
    var tipReports: MutableList<IndividualReport> = mutableListOf()
) {

    fun addTipReport(data: IndividualReport) {
        tipReports.add(data)
    }
    fun checkForUncollectedTips(): Double {
        var output = 0.0
        tipReports.forEach {
            if (!it.collected) {
                output += it.distributedTips!!
            }
        }
        return output
    }

}

