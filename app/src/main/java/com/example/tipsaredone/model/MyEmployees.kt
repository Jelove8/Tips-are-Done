package com.example.tipsaredone.model

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

    // Called in MainActivity when Employee names are being saved
    fun convertEmployeeNamesToJson(names: MutableList<String>): String {
        // Converting the Employee Data Class into Json string.
        val gson = GsonBuilder().setPrettyPrinting().create()
        val json: String = gson.toJson(names)
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
