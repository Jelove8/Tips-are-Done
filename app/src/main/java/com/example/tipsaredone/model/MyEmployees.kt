package com.example.tipsaredone.model

import android.content.Context
import android.util.Log
import com.example.tipsaredone.views.MainActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.Serializable
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

@Serializable
class MyEmployees() {

    companion object {
        const val INTERNAL_STORAGE = "internal_storage"
    }

    fun saveEmployeesAsJson(mainActivity: MainActivity, employees: MutableList<Employee>) {




    }

    fun convertEmployeesToJson(employees: MutableList<Employee>) {

    }

    fun convertEmployeeObjectsToJson(employees: MutableList<Employee>): String {

        // Converting the Employee Data Class into Json string.
        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonEmployeeData: String = gson.toJson(employees)
        Log.d(INTERNAL_STORAGE, "Employee objects converted to Json string:\n${jsonEmployeeData}")
        return jsonEmployeeData
    }

    fun loadEmployeesAsList(mainActivity: MainActivity): MutableList<Employee> {

        // Getting Json data from internal storage
        val employeesFile = File(mainActivity.filesDir, "myEmployees")
        val employees = convertIntStorageFileToJsonString(employeesFile)

        if (employees.isEmpty()) {
            Log.d(INTERNAL_STORAGE, "MyEmployees: Json file is empty:\n${employees}")

            return mutableListOf()
        }
        else {
            Log.d(INTERNAL_STORAGE, "MyEmployees: Json file loaded:\n${employees}")

            // Converting Json into a MutableList<Employees>, then returning it
            val gson = Gson()
            val employeeListType = object : TypeToken<MutableList<Employee>>() {}.type
            val employeeAsMutableList: MutableList<Employee> = gson.fromJson(employees, employeeListType)

            Log.d(INTERNAL_STORAGE, "Json data as mutable list:\n${employeeAsMutableList}")




            return employeeAsMutableList
        }


    }

    private fun convertIntStorageFileToJsonString(file: File): String {

        // https://medium.com/@nayantala259/android-how-to-read-and-write-parse-data-from-json-file-226f821e957a

        val fileReader = FileReader(file)
        val bufferedReader = BufferedReader(fileReader)
        val stringBuilder = StringBuilder()
        var line: String = bufferedReader.readLine()
        while (line.isNotEmpty()) {
            stringBuilder.append(line).append("\n")
            line = bufferedReader.readLine()
        }
        bufferedReader.close()

        return stringBuilder.toString()

    }

}

data class Employee(
    val id: String,
    var name: String,
    var tippableHours: Double? = null,
    var distributedTips: Double = 0.00
) {
    override fun toString(): String {
        return "Category [id: ${this.id}, name: ${this.name}, tippableHours: ${this.tippableHours}, distributedTips: ${this.distributedTips}]"
    }
}
