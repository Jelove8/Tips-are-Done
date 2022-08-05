package com.example.tipsaredone.model

import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

interface JsonConversions {

    fun convertEmployeeToJson(employees: MutableList<Employee>): String {
        return Json.encodeToString(employees)
    }
}