package com.example.tipsaredone.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
        const val FIREBASE_EMPLOYEES = "FirebaseEmployees"

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
    }

    private val firebaseDB: FirebaseFirestore = Firebase.firestore
    private val userID = FirebaseAuth.getInstance().currentUser!!.uid

    val employees: MutableList<Employee> = mutableListOf()
    val weeklyReports = mutableListOf<WeeklyReport>()
    val individualReports = mutableListOf<IndividualReport>()

    init {
        Log.d(FIREBASE_EMPLOYEES,"Database model initialized.")
    }

    fun initializeEmployees(employeesAdapter: EmployeesAdapter) {
        // Reading employees
        Firebase.firestore.collection(DatabaseOperator.USERS_LABEL).document(userID).collection(DatabaseOperator.EMPLOYEES_LABEL).get()
            .addOnSuccessListener { employeesList ->
                for (employee in employeesList) {
                    val id = employee.data[DatabaseModel.ID].toString()
                    val name = employee.data[DatabaseModel.NAME].toString()
                    val employeeObject = Employee(name,id)
                    employees.add(employeeObject)
                    employeesAdapter.addNewEmployee(employeeObject)
                    Log.d(DatabaseOperator.FIREBASE_EMPLOYEES,"Reading Employee: $name, $id")
                }
                Log.d(DatabaseOperator.FIREBASE_EMPLOYEES,"${employeesAdapter.itemCount} employees read from database.")
            }
        employees.forEach {
            Log.d(FIREBASE_EMPLOYEES,"hello? $it")
        }
        employees.sortBy { it.name }
    }
    fun initializeIndividualReports(employeeAdapter: EmployeesAdapter) {
        DatabaseOperator().initializeIndividualReports(employeeAdapter)
        employeeAdapter.getEmployees().forEach {
            it.tipReports.forEach { tipReport ->
                individualReports.add(tipReport)
            }
        }
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        individualReports.sortByDescending {
            LocalDate.parse(it.startDate, dateTimeFormatter)
        }
    }
    fun initializeEmployeeHours(hoursAdapter: HoursAdapter) {
        Firebase.firestore.collection(DatabaseOperator.USERS_LABEL).document(userID).collection(DatabaseOperator.EMPLOYEES_LABEL).get()
            .addOnSuccessListener { employees ->
                for (employee in employees) {
                    val id = employee.data[ID].toString()
                    val name = employee.data[NAME].toString()
                    val employeeObject = Employee(name,id)
                    hoursAdapter.addEmployee(employeeObject)
                    Log.d(DatabaseOperator.FIREBASE_EMPLOYEES,"Reading Employee: $name, $id")
                }
                Log.d(DatabaseOperator.FIREBASE_EMPLOYEES,"${hoursAdapter.itemCount} employees read from database.")
            }
    }
    fun initializeWeeklyReports(weeklyReportsAdapter: WeeklyReportsAdapter) {
        DatabaseOperator().initializeWeeklyReports(weeklyReportsAdapter)
        weeklyReportsAdapter.getWeeklyReports().forEach {
            weeklyReports.add(it)
        }
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        weeklyReports.sortByDescending {
            LocalDate.parse(it.startDate, dateTimeFormatter)
        }
    }

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

    // Employee List
    fun addNewEmployee(newEmployee: Employee): Boolean {
        Log.d(FIREBASE_EMPLOYEES, "Attempting to add new employee: ${newEmployee.name}.")
        val newEmployeeID = newEmployee.id
        var idCheck = true
        employees.forEach {
            if (it.id == newEmployeeID) {
                idCheck = false
            }
        }

        if (idCheck) {
            employees.add(newEmployee)
            employees.sortBy { it.name }
            DatabaseOperator().saveNewOrExistingEmployee(newEmployee)
        } else {
            Log.d(FIREBASE_EMPLOYEES, "New employee id to be added already exists.")
        }
        return idCheck
    }
    fun editExistingEmployee(editedEmployee: Employee): Boolean {
        Log.d(FIREBASE_EMPLOYEES, "Attempting to edit existing employee: ${editedEmployee.name}.")

        var selectedEmployeeExists = false
        employees.forEach {
            if (it.id == editedEmployee.id) {
                it.name = editedEmployee.name
                it.tipReports = editedEmployee.tipReports
                selectedEmployeeExists = true
                DatabaseOperator().saveNewOrExistingEmployee(editedEmployee)
            }
        }
        employees.sortBy { it.name }

        return if (!selectedEmployeeExists) {
            Log.d(FIREBASE_EMPLOYEES, "Employee to edit does not exist: ${editedEmployee.name} (${editedEmployee.id}")
            false
        } else {
            true
        }
    }
    fun deleteExistingEmployee(selectedEmployee: Employee) : Boolean {
        Log.d(FIREBASE_EMPLOYEES, "Attempting to delete existing employee: ${selectedEmployee.name}.")

        var selectedEmployeeExists = false
        employees.forEach {
            if (it.id == selectedEmployee.id) {
                employees.remove(it)
                selectedEmployeeExists = true
                DatabaseOperator().deleteEmployee(selectedEmployee)
            }
        }

        return if (!selectedEmployeeExists) {
            Log.d(FIREBASE_EMPLOYEES,"Employee to delete does not exist: ${selectedEmployee.name} (${selectedEmployee.id}")
            false
        }
        else {
            true
        }
    }
    fun collectTipsFromSpecificWeek(selectedEmployeeID: String, selectedReportID: String, isCollecting: Boolean) : Boolean {
        Log.d(FIREBASE_EMPLOYEES, "Attempting to collect employee tips.")

        var selectedEmployee: Employee? = null
        employees.forEach {
            if (it.id == selectedEmployeeID) {
                selectedEmployee = it
            }
        }

        var selectedIndividualReport: IndividualReport? = null
        individualReports.forEach {
            if (it.reportID == selectedReportID) {
                selectedIndividualReport = it
            }
        }

        return if (selectedEmployee != null && selectedIndividualReport != null) {
            selectedEmployee!!.tipReports.forEach {
                if (it.reportID == selectedReportID) {
                    it.collected = isCollecting
                }
            }
            selectedIndividualReport!!.collected = isCollecting
            DatabaseOperator().collectEmployeeTips(selectedEmployee!!,selectedIndividualReport!!)
            true
        }
        else {
            Log.d(FIREBASE_EMPLOYEES, "Selected employee could not be found for tip collection.")
            false
        }
    }



    // Reports
    fun addWeeklyReport(weeklyReport: WeeklyReport) {
        weeklyReports.add(weeklyReport)
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        weeklyReports.sortByDescending {
            LocalDate.parse(it.startDate, dateTimeFormatter)
        }
        DatabaseOperator().saveWeeklyReport(weeklyReport)
    }
    fun addIndividualReport(individualReport: IndividualReport) {
        individualReports.add(individualReport)
        employees.forEach {
            if (it.id == individualReport.id) {
                it.tipReports.add(individualReport)
                val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                it.tipReports.sortByDescending { report ->
                    LocalDate.parse(report.startDate, dateTimeFormatter)
                }
            }
        }
        DatabaseOperator().saveIndividualReport(individualReport)
    }


}






