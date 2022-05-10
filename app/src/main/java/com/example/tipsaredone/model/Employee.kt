package com.example.tipsaredone.model

data class Employee(
    val id: String,
    var name: String,
    var currentTippableHours: Double? = null,
    val archived: Boolean = false
)
