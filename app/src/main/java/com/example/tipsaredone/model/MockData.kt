package com.example.tipsaredone.model

import java.text.SimpleDateFormat
import java.util.*

class MockData() {

    private val mockEmployees = mutableListOf(
        Employee(
            "Ashley Amari"
        ),
        Employee(
            "Brandon Butter"
        ),
        Employee(
            "Calvin Clein"
        ),
        Employee(
            "David Duster"
        ),
        Employee(
            "Eduardo Ent"
        ),
        Employee(
            "Fuego French"
        ),
        Employee(
            "Gordon Gamsey"
        ),
        Employee(
            "Helena"
        ),
        Employee(
            "Ipis"
        ),
        Employee(
            "Jordan Jordanson"
        ),
        Employee(
            "Kalvin Klein"
        ),
        Employee(
            "Lorax"
        ),
        Employee(
            "M"
        ),
        Employee(
            "Nick Ramirez"
        ),
        Employee(
            "Osaro Ramirez"
        ),
        Employee(
            "Pia Piper"
        )
    )

    fun getMockEmployees(): MutableList<Employee> {
        return mockEmployees
    }
}