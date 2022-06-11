package com.example.tipsaredone.model

import java.text.SimpleDateFormat
import java.util.*

class MockData() {

    private val mockEmployees = mutableListOf(
        Employee(
            "Ashley Alms"
        ),
        Employee(
            "Brandon Butter"
        )
    )

    fun getMockEmployees(): MutableList<Employee> {
        return mockEmployees
    }
}