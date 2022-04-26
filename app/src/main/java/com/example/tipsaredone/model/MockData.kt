package com.example.tipsaredone.model

import java.text.SimpleDateFormat
import java.util.*

class MockData() {

    private val weeklyReport1 = TipReport(
        "03082022",
        "03142022",
        40,
        true
    )
    private val weeklyReport2 = TipReport(
        "03152022",
        "03212022",
        25,
        true
    )
    private val weeklyReport3 = TipReport(
        "03222022",
        "03282022",
        67,
        true
    )
    private val weeklyReport4 = TipReport(
        "03292022",
        "04042022",
        35,
        true
    )
    private val bensWeeklyReport2 = TipReport(
        "03152022",
        "03212022",
        41,
        false
    )

    private val reports = mutableListOf(weeklyReport1,weeklyReport2,weeklyReport3,weeklyReport4)
    private val bensReports = mutableListOf(weeklyReport1,bensWeeklyReport2,weeklyReport3,weeklyReport4)

    private val emp1 = Employee(
        "emp_00000001",
        "Ashley Ramirez Johnson",
        reports
    )
    private val emp2 = Employee(
        "emp_00000002",
        "Benjamin Button",
        bensReports
    )
    private val emp3 = Employee(
        "emp_00000003",
        "Cali Fornia",
        reports
    )
    private val emp4 = Employee(
        "emp_00000004",
        "Dash Ashley",
        reports
    )
    private val emp5 = Employee(
        "emp_00000005",
        "Eduardo Bo",
        reports
    )
    private val emp6 = Employee(
        "emp_00000006",
        "Felicia Day",
        reports
    )
    private val emp7 = Employee(
        "emp_00000007",
        "Gordon Ramsay",
        reports
    )
    private val emp8 = Employee(
        "emp_00000008",
        "Helen of Troy",
        reports
    )
    private val emp9 = Employee(
        "emp_00000009",
        "Icarus Falling",
        reports
    )
    private val emp10 = Employee(
        "emp_00000010",
        "Jack-Jack from the Incredibles",
        reports
    )

    fun getMockEmployees(): MutableList<Employee> {
        return mutableListOf(emp1,emp2,emp3,emp4,emp5,emp6,emp7,emp8,emp9,emp10)
    }
}