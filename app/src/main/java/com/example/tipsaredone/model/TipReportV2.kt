package com.example.tipsaredone.model

class TipReportV2() {

    private val employees: MutableList<Employee> = mutableListOf()
    private val bills: MutableList<Map<String,Int>> = mutableListOf()
    private val coins: MutableList<Map<String,Double>> = mutableListOf()
    private var totalEarnings: Double = 0.0
    private var startDate: String = ""
    private var endDate: String = ""

    fun setEmployees(data: MutableList<Employee>) {
        data.forEach {
            employees.add(it)
        }
    }

    fun setBills(data: MutableList<Map<String,Int>>) {
        data.forEach {
            bills.add(it)
        }
    }

    fun setCoins(data: MutableList<Map<String,Double>>) {
        data.forEach {
            coins.add(it)
        }
    }

    fun setTotalEarnings(data: Double) {
        totalEarnings = data
    }

    fun setStartDate(data: String) {
        startDate = data
    }

    fun setEndDate(data: String) {
        endDate = data
    }
}