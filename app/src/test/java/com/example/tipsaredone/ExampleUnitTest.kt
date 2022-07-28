package com.example.tipsaredone

import android.util.Log
import com.example.tipsaredone.model.Employee
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun addition_of_hours_is_correct() {
        var output = 0.00
        val employeeList = mutableListOf(
            Employee("0",null),
            Employee("1",0.2),
            Employee("2",2.4),
            Employee("3",4.8),
            Employee("4",8.16),
            Employee("5",16.32),
            Employee("6",32.64),
            Employee("7",64.128),
            Employee("8",128.256)
        )

        for (emp in employeeList) {
            if (emp.tippableHours != null) {
                output += emp.tippableHours.toString().toDouble() * 1000
            }
        }
        assertEquals(256904,output.toInt())
    }
}