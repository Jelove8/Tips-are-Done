package com.example.tipsaredone.model

data class Employee(
    val id: String,
    var name: String,
    var currentTippableHours: Double? = null,
    var distributedTips: Double = 0.00,
    val archived: Boolean = false
)
