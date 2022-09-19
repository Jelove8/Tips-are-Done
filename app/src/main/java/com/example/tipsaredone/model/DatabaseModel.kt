package com.example.tipsaredone.model

import android.util.Log
import com.example.tipsaredone.activities.UserLoginActivity
import com.example.tipsaredone.adapters.EmployeesAdapter
import com.example.tipsaredone.adapters.HoursAdapter
import com.example.tipsaredone.adapters.WeeklyReportsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DatabaseModel() {

    companion object {
        const val FIRECAT = "Firecat"

        const val USERS = "Users"
        const val EMPLOYEES = "Employees"
        const val WEEKLY_REPORTS_LABEL = "Weekly Reports"
        const val INDIVIDUAL_REPORTS_LABEL = "Individual Reports"
        const val ID = "id"
        const val NAME = "name"
        const val HOURS = "hours"
        const val START_DATE = "startDate"
        const val END_DATE = "endDate"
        const val INDIVIDUAL_REPORTS = "individualReports"
        const val TOTAL_HOURS = "totalHours"
        const val COLLECTED_TIPS = "collectedTips"
        const val TOTAL_COLLECTED = "totalCollected"
        const val TIP_RATE = "tipRate"
        const val ROUNDING_ERROR = "roundingError"

        const val EMPLOYEE_NAME = "employeeName"
        const val EMPLOYEE_ID = "employeeID"
        const val EMPLOYEE_HOURS = "employeeHours"
        const val DISTRIBUTED_TIPS = "distributedTips"
        const val COLLECTED_BOOLEAN = "collected"

        const val FIREBASE_REPORTS = "FirebaseReports"

        const val USERS_LABEL = "Users"
        const val EMPLOYEES_LABEL = "Employees"

        const val ERROR = "error"
        const val TOTAL_TIPS = "totalTips"
        const val COLLECTED_BOOL = "collected"
        const val REPORT_ID = "reportID"
        const val TIP_REPORTS = "tipReports"
    }

    private val firebaseDB: FirebaseFirestore = Firebase.firestore
    private val userID = FirebaseAuth.getInstance().currentUser!!.uid



    private var weeklyReports: MutableList<WeeklyReport> = mutableListOf()

    // User Login
    fun readRememberUserBooleanForUserLogin(userLoginActivity: UserLoginActivity) {
        val currentUserUID = FirebaseAuth.getInstance().currentUser!!.uid
        firebaseDB.collection(USERS).document(currentUserUID).get()
            .addOnSuccessListener { document ->
                if (!document.data.isNullOrEmpty()) {
                    val rememberMeBoolean = document.data!![UserLoginActivity.REMEMBER_USER_BOOL].toString().toBoolean()
                    if (rememberMeBoolean) {
                        userLoginActivity.navigateToMainActivity()
                    }
                    Log.d(UserLoginActivity.LOGIN,"Successfully read 'remember me' boolean: $rememberMeBoolean")
                }
            }
            .addOnFailureListener {
                Log.d(UserLoginActivity.LOGIN,"Failed to read 'remember me' boolean for UserLogin: $it")
            }
    }

    fun setRememberUserBoolean(boolean: Boolean, currentUserID: String) {
        firebaseDB.collection(USERS).document(currentUserID).update(UserLoginActivity.REMEMBER_USER_BOOL,boolean)
            .addOnSuccessListener {
                Log.d(UserLoginActivity.LOGIN,"Successfully changed 'remember me' boolean to $boolean.")
            }
    }

    // Initial reading from database

    fun initializeEmployees(employeesAdapter: EmployeesAdapter) {
        Firebase.firestore.collection(USERS_LABEL).document(userID).collection(EMPLOYEES_LABEL).get()
            .addOnSuccessListener { employeesList ->
                for (employee in employeesList) {
                    val id = employee.data[ID].toString()
                    val name = employee.data[NAME].toString()
                    val employeeObject = Employee(name,id)
                    employeesAdapter.addNewEmployee(employeeObject)
                    Log.d(FIRECAT,"Reading employee from database: $name, $id")
                }
                Log.d(FIRECAT,"${employeesAdapter.itemCount} employees read from database.")
            }
            .addOnFailureListener {
                Log.d(FIRECAT,"Failed to read employees from database: $it")
            }

    }


    fun initializeEmployeeHours(hoursAdapter: HoursAdapter) {
        Firebase.firestore.collection(USERS_LABEL).document(userID).collection(EMPLOYEES_LABEL).get()
            .addOnSuccessListener { employees ->
                for (employee in employees) {
                    val id = employee.data[ID].toString()
                    val name = employee.data[NAME].toString()
                    val employeeObject = Employee(name,id)
                    hoursAdapter.addEmployee(employeeObject)
                    Log.d(FIRECAT,"Creating employee hours object: $name, $id")
                }
                Log.d(FIRECAT,"${hoursAdapter.itemCount} employee hour objects created.")
                Log.d(FIRECAT,"Employee hours initialization finished.")
            }
            .addOnFailureListener {
                Log.d(FIRECAT,"Failed to initialize employee hours.")
            }
    }


    // Employee List
    fun addOrEditEmployee(employee: Employee) {
        Log.d(FIRECAT, "Attempting to add or edit an employee: ${employee.name}.")
        Firebase.firestore.collection(USERS_LABEL).document(userID).collection(EMPLOYEES_LABEL).document(employee.id).set(employee)
            .addOnSuccessListener {
                Log.d(FIRECAT,"Successfully saved employee changes: ${employee.name}.")
            }
            .addOnFailureListener {
                Log.d(FIRECAT,"Failed to save employee changes: ${employee.name}")
            }
    }
    fun addNewEmployee(newEmployee: Employee) {
        Log.d(FIRECAT, "Attempting to add new employee: ${newEmployee.name}.")
        Firebase.firestore.collection(USERS_LABEL).document(userID).collection(EMPLOYEES_LABEL).add(newEmployee)
            .addOnSuccessListener {
                Log.d(FIRECAT,"Successfully saved new employee.")
            }
            .addOnFailureListener {
                Log.d(FIRECAT,"Failed to save new employee.")
            }

    }
    fun editExistingEmployee(editedEmployee: Employee) {
        Log.d(FIRECAT, "Attempting to edit existing employee: ${editedEmployee.name}.")
        Firebase.firestore.collection(USERS_LABEL).document(userID).collection(EMPLOYEES_LABEL).document(editedEmployee.id).set(editedEmployee)
            .addOnSuccessListener {
                Log.d(FIRECAT,"Successfully edited employee: ${editedEmployee.name}")
            }
            .addOnFailureListener {
                Log.d(FIRECAT,"Failed to edit employee: ${editedEmployee.name}")
            }
    }
    fun deleteExistingEmployee(selectedEmployee: Employee) {
        Log.d(FIRECAT, "Attempting to delete existing employee: ${selectedEmployee.name}.")

        Firebase.firestore.collection(USERS_LABEL).document(userID).collection(EMPLOYEES_LABEL).document(selectedEmployee.id).delete()
            .addOnSuccessListener {
                Log.d(FIRECAT, "Successfully deleted employee: ${selectedEmployee.name} (${selectedEmployee.id})")
            }
            .addOnFailureListener {
                Log.d(FIRECAT, "Failed to delete employee: ${selectedEmployee.name} (${selectedEmployee.id})")
            }


    }

    // Reports
    fun addWeeklyReport(weeklyReport: WeeklyReport) {
        weeklyReports.add(weeklyReport)
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        weeklyReports.sortByDescending {
            LocalDate.parse(it.startDate, dateTimeFormatter)
        }
    }


}






