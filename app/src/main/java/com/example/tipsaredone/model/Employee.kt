package com.example.tipsaredone.model

import kotlinx.serialization.Serializable

@Serializable
class Employee(
    var name: String,
    val id: String,
    var tipReports: MutableList<IndividualTipReport> = mutableListOf()
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

