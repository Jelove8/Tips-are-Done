package com.example.tipsaredone.model

data class Employee(
    val id: String,
    var name: String,
    val reports: MutableList<TipReport> = mutableListOf(),
    val archived: Boolean = false
)
