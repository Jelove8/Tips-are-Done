package com.example.tipsaredone.model

import android.content.Context
import com.example.tipsaredone.views.MainActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class MyEmployees() {

    private var employees: MutableList<Employee> = mutableListOf()

    fun getEmployees(): MutableList<Employee> {
        return employees
    }
    fun setEmployees(list: MutableList<Employee>) {
        employees = list
    }

    fun saveEmployees(mainActivity: MainActivity) {

        val gson = Gson()
        GsonBuilder().setPrettyPrinting().create()
        val fileName = "MyEmployees"
        val fileContents = gson.toJson(employees)
        mainActivity.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(fileContents.toByteArray())
        }

    }

    fun loadEmployees(mainActivity: MainActivity) {

        

    }

}

data class Employee(
    val id: String,
    var name: String,
    var currentTippableHours: Double? = null,
    var distributedTips: Double = 0.00
)
