package com.example.tipsaredone.model

import java.text.SimpleDateFormat
import java.util.*

class MockData() {

    private val mockEmployees = mutableListOf(
        Employee(
            "Ashley Altmer"
        ),
        Employee(
            "Brandon Botmer"
        ),
        Employee(
            "Cali Chajiit"
        ),
        Employee(
            "Darwin Dunmer"
        )
    )


    fun getMockEmployees(): MutableList<Employee> {
        return mockEmployees
    }
}