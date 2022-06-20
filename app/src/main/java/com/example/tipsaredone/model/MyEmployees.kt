package com.example.tipsaredone.model

import android.content.Context
import android.util.Log
import com.example.tipsaredone.views.MainActivity
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader


class MyEmployees() {

    companion object {
        const val INTERNAL_STORAGE = "internal_storage"
    }

    private fun convertEmployeeNamesToJson(employee_names: MutableList<String>): String {
        // Converting list of string into Json.
        val gson = GsonBuilder().setPrettyPrinting().create()
        val json: String = gson.toJson(employee_names)
        Log.d(INTERNAL_STORAGE, "Employee names converted to Json:\n${json}")
        return json
    }

    fun saveEmployeeNamesToInternalStorage(employees: MutableList<Employee>,context: Context) {        // Called whenever the "CONTINUE" button is pressed @EmployeeListFragment.
        // Converting list of employee objects into list of employee names (string).
        val listOfNames = mutableListOf<String>()
        for (emp in employees) {
            listOfNames.add(emp.name)
        }

        // Converting listOfNames to Json
        val jsonString = MyEmployees().convertEmployeeNamesToJson(listOfNames)
        val jsonFileName = "myEmployees"

        // Saving the Json data to internal storage
        (context as MainActivity).openFileOutput(jsonFileName, Context.MODE_PRIVATE).use {
            it.write(jsonString.toByteArray())
            it.close()
            Log.d(INTERNAL_STORAGE, "Employee names saved as Json:\n${jsonString}")
        }

    }
    fun loadEmployeeNamesFromInternalStorage(context: Context): MutableList<Employee> {
        // loading data as Json text
        val fileInputStream: FileInputStream? = context.openFileInput("myEmployees")
        val inputStreamReader = InputStreamReader(fileInputStream)
        val bufferedReader = BufferedReader(inputStreamReader)
        val stringBuilder: StringBuilder = StringBuilder()
        var text: String? = null

        while (run {
                text = bufferedReader.readLine()
                text
            } != null) {
            stringBuilder.append(text)
        }

        val jsonString = stringBuilder.toString()
        Log.d(INTERNAL_STORAGE,"Names loaded as Json string.")

        // Checking if Json is empty
        if (jsonString.isNotBlank() && jsonString != "")  {

            // Converting Json to list of string
            val gson = GsonBuilder().create()
            val listOfNames = gson.fromJson<MutableList<String>>(jsonString, object :
                TypeToken<MutableList<String>>(){}.type)
            Log.d(INTERNAL_STORAGE,"Names converted to mutable list of string.")

            // Initializing a list of objects
            val listOfEmployees = mutableListOf<Employee>()
            for (name in listOfNames) {
                listOfEmployees.add(Employee(name))
                Log.d(INTERNAL_STORAGE,"Employee Loaded: $name")
            }
            return listOfEmployees
        }
        else {
            Log.d(INTERNAL_STORAGE,"Internal storage contained no names.")
            return mutableListOf()
        }
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


