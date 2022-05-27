package com.example.tipsaredone.model

import java.text.SimpleDateFormat
import java.util.*

class MockData() {

    private val mockEmployees = mutableListOf(
        Employee(
            "emp_0000000",
            "Ashley Altmer"
        ),
        Employee(
            "emp_0000001",
            "Brandon Botmer"
        ),
        Employee(
            "emp_0000002",
            "Cali Chajiit"
        ),
        Employee(
            "emp_0000003",
            "Darwin Dunmer"
        )
    )


    fun getMockEmployees(): MutableList<Employee> {
        return mockEmployees
    }
}