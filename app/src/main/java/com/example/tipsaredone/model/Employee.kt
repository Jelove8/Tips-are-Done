package com.example.tipsaredone.model

data class Employee(
    val id: String,
    var name: String,
    val reports: MutableList<TipReport> = mutableListOf(),
    var currentTippableHours: Double? = null,
    val archived: Boolean = false
)
