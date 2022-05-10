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
        ),
        Employee(
            "emp_0000005",
            "Ferdinand Fagellan"
        ),
        Employee(
            "emp_0000006",
            "Gordon Gallows"
        ),
        Employee(
            "emp_0000007",
            "Henry Hopper"
        ),
        Employee(
            "emp_0000008",
            "Irwin Indigo"
        ),
        Employee(
            "emp_0000009",
            "Jacqueline Jacques"
        ),
        Employee(
            "emp_0000010",
            "Kelly Kapur"
        ),
        Employee(
            "emp_0000011",
            "Leon Leo"
        ),
        Employee(
            "emp_0000012",
            "Marilyn Monroe"
        )
    )


    fun getMockEmployees(): MutableList<Employee> {
        return mockEmployees
    }
}