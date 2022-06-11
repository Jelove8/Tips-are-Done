package com.example.tipsaredone.model

import android.util.Log
import com.google.gson.GsonBuilder


class MyEmployees() {

    companion object {
        const val INTERNAL_STORAGE = "internal_storage"
    }

    fun convertEmployeeNamesToJson(employee_names: MutableList<String>): String {
        // Converting list of string into Json.
        val gson = GsonBuilder().setPrettyPrinting().create()
        val json: String = gson.toJson(employee_names)
        Log.d(INTERNAL_STORAGE, "Employee names converted to Json:\n${json}")
        return json
    }

}

data class Employee(
    var name: String,
    var tippableHours: Double? = null,
    var distributedTips: Double = 0.00
) {
    override fun toString(): String {
        return "Category [name: ${this.name}, tippableHours: ${this.tippableHours}, distributedTips: ${this.distributedTips}]"
    }
}


