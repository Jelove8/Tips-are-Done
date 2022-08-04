package com.example.tipsaredone.model

data class NewEmployee(
    val id: String,
    var name: String,
    val tipReports: MutableList<IndividualTipReport> = mutableListOf()
) {

    fun addTipReport(data: IndividualTipReport) {
        tipReports.add(data)
    }
    fun checkForUncollectedTips(): Double {
        var output = 0.0
        tipReports.forEach {
            if (!it.collected && it.distributedTips != null) {
                output += it.distributedTips!!
            }
        }
        return output
    }

}
