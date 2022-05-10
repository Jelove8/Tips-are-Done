package com.example.tipsaredone.model

import java.text.SimpleDateFormat
import java.util.*

class MockData() {

    private val mockEmployees = mutableListOf(
        Employee(
            "emp_0000000",
            "Ashley Atwood"
        ),
        Employee(
            "emp_0000001",
            "Benjamin Button"
        ),
        Employee(
            "emp_0000002",
            "Cali Clay"
        ),
        Employee(
            "emp_0000003",
            "Darwin Decker"
        ),
        Employee(
            "emp_0000004",
            "Eve Esque"
        )
    )


    fun getMockEmployees(): MutableList<Employee> {
        return mockEmployees
    }
}