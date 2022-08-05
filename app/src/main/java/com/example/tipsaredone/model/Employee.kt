package com.example.tipsaredone.model

class Employee(
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
    fun convertForStorage(): EmployeeConvertedForStorage {
        val individualTipReportsConvertedForStorage = mutableListOf<IndividualTipReportConvertedForStorage>()
        tipReports.forEach {
            individualTipReportsConvertedForStorage.add(it.convertForStorage())
        }
        return EmployeeConvertedForStorage(id,name,individualTipReportsConvertedForStorage)
    }

}
data class EmployeeConvertedForStorage(
    val id: String,
    val name: String,
    val individualTipReports: MutableList<IndividualTipReportConvertedForStorage>
)
