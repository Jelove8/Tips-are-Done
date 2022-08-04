package com.example.tipsaredone.model

data class Employee(
    var name: String,
    var tippableHours: Double? = null,
    var tips: Double = 0.0,
    val tipReports: MutableList<IndividualTipReport> = mutableListOf()
) {
    override fun toString(): String {
        return "Category [name: ${this.name}, tippableHours: ${this.tippableHours}, distributedTips: ${this.tips}]"
    }
}