package com.example.tipsaredone.model

import android.util.Log
import com.example.tipsaredone.activities.UserLoginActivity
import com.example.tipsaredone.adapters.EmployeesAdapter
import com.example.tipsaredone.adapters.IndividualReportsAdapter
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
    private val employees = mutableListOf<Employee>()
    private val weeklyReports = mutableListOf<WeeklyReport>()
    private val individualReports = mutableListOf<IndividualReport>()

    init {
        Log.d("FirebaseDatabase","DataBaseModel initialized.")
        Log.d("FirebaseDatabase","Current User: ")
    }


    // Employee List
    fun addNewEmployee(newEmployee: Employee) {
        val newEmployeeID = newEmployee.id
        var idCheck = true
        employees.forEach {
            if (it.id == newEmployeeID) {
                idCheck = false
            }
        }
        if (idCheck) {
            employees.add(newEmployee)
            DatabaseOperator().saveNewOrExistingEmployee(newEmployee)
        } else {
            Log.d(FIREBASE_EMPLOYEES, "New employee id to be added already exists.")
        }
    }
    fun editExistingEmployee(editedEmployee: Employee) {
        employees.forEach {
            if (it.id == editedEmployee.id) {
                it.name = editedEmployee.name
                it.tipReports = editedEmployee.tipReports
                DatabaseOperator().saveNewOrExistingEmployee(editedEmployee)
            }
        }
    }
    fun deleteExistingEmployee(deletedEmployee: Employee) {
        employees.forEach {
            if (it.id == deletedEmployee.id) {
                employees.remove(it)
                DatabaseOperator().deleteEmployee(deletedEmployee)
            }
        }
    }

    












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

    fun readEmployeeDataFromDatabase(employeesAdapter: EmployeesAdapter) {
        val currentUserUID = FirebaseAuth.getInstance().currentUser!!.uid
        firebaseDB.collection(USERS).document(currentUserUID).collection(EMPLOYEES).get()
            .addOnSuccessListener { employeeItems ->
                for (item in employeeItems) {
                    val itemID = item.data[ID].toString()
                    val itemName = item.data[NAME].toString()
                    employeesAdapter.addNewEmployee(Employee(itemName,itemID))
                    employees.add(Employee(itemName,itemID))
                    employees.sortBy { it.name }
                    Log.d("FirebaseDatabase","Reading Employee: $itemName, $itemID")
                }
                Log.d("FirebaseDatabase","${employeeItems.size()} Employees read from database.")
            }
    }


    fun readIndividualReportsFromDatabase() {
        val currentUserUID = FirebaseAuth.getInstance().currentUser!!.uid
        firebaseDB.collection(USERS).document(currentUserUID).collection(INDIVIDUAL_REPORTS_LABEL).get()
            .addOnSuccessListener { individualReportItems ->
                for (item in individualReportItems) {
                    val itemName = item.data[NAME].toString()
                    val itemID = item.data[ID].toString()
                    val itemHours = item.data[HOURS].toString().toDouble()
                    val itemDistributedTips = item.data[DISTRIBUTED_TIPS].toString().toDouble()
                    val itemStartDate = item.data[START_DATE].toString()
                    val itemEndDate = item.data[END_DATE].toString()
                    val itemRoundingError = item.data[ROUNDING_ERROR].toString().toInt()
                    val itemCollectedBool = item.data[COLLECTED_BOOLEAN].toString().toBoolean()
                    individualReports.add(IndividualReport(itemName,itemID,itemHours,itemDistributedTips,itemStartDate,itemEndDate,itemRoundingError,itemCollectedBool))
                }
            }
    }



    fun createNewEmployee(newEmployee: Employee) {
        firebaseDB.collection(USERS).document(FirebaseAuth.getInstance().currentUser!!.uid).collection(EMPLOYEES).document(newEmployee.id).set(newEmployee)
            .addOnSuccessListener {
                employees.add(newEmployee)
                employees.sortBy { it.name }
                Log.d("FirebaseDatabase", "Successfully created new employee:   ${newEmployee.name}, ${newEmployee.id}")
            }
            .addOnFailureListener {
                Log.d("FirebaseDatabase", "Failed to create new employee.")
            }
    }

    fun deleteEmployee(selectedEmployee: Employee) {
        firebaseDB.collection("Users").document(FirebaseAuth.getInstance().currentUser!!.uid).collection("Employees").document(selectedEmployee.id).delete()
            .addOnSuccessListener {
                Log.d("FirebaseDatabase", "Employee deleted from database: ${selectedEmployee.name}")
            }
            .addOnFailureListener {
                Log.d("FirebaseDatabase", "Failed to delete Employee: ${selectedEmployee.name}")
            }

        employees.remove(selectedEmployee)
    }

    fun updateEmployee(selectedEmployee: Employee) {
        firebaseDB.collection(USERS).document(FirebaseAuth.getInstance().currentUser!!.uid).collection(EMPLOYEES).document(selectedEmployee.id).set(selectedEmployee)
            .addOnSuccessListener {
                Log.d("FirebaseDatabase","Successfully updated employee:   ${selectedEmployee.name}") }
            .addOnFailureListener { Log.d("FirebaseDatabase","Failed to update employee:    ${selectedEmployee.name}    $it")}
    }


    fun saveWeeklyReport(weeklyReport: WeeklyReport) {

        var startDateString = ""
        var endDateString = ""

        weeklyReport.startDate.forEach {
            if (it.toString() != "-") {
                startDateString += it.toString()
            }
        }
        weeklyReport.endDate.forEach {
            if (it.toString() != "-") {
                endDateString += it.toString()
            }
        }
        val documentID = "$startDateString-$endDateString"

        firebaseDB.collection(USERS).document(FirebaseAuth.getInstance().currentUser!!.uid).collection(WEEKLY_REPORTS_LABEL).document(documentID).set(weeklyReport)
            .addOnSuccessListener {
                weeklyReports.add(weeklyReport)
                weeklyReports.sortBy { it.startDate }
                Log.d(WEEKLY_REPORTS_LABEL, "Successfully saved new weekly report.")
                saveIndividualReports(weeklyReport.individualReports)
            }
            .addOnFailureListener {
                Log.d(WEEKLY_REPORTS_LABEL, "Failed to saved new weekly report.")
            }
    }
    private fun saveIndividualReports(newTipReports: MutableList<IndividualReport>) {
        var startDateString = ""
        var endDateString = ""

        newTipReports[0].startDate!!.forEach {
            if (it.toString() != "-") {
                startDateString += it.toString()
            }
        }
        newTipReports[0].endDate!!.forEach {
            if (it.toString() != "-") {
                endDateString += it.toString()
            }
        }
        val documentID = "$startDateString-$endDateString"

        firebaseDB.collection(USERS).document(FirebaseAuth.getInstance().currentUser!!.uid).collection(INDIVIDUAL_REPORTS_LABEL).document(documentID).set(newTipReports)
            .addOnSuccessListener {
                newTipReports.forEach {
                    individualReports.add(it)
                    Log.d(WEEKLY_REPORTS_LABEL, "Successfully saved new individual reports to database.")
                }
                val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                individualReports.sortByDescending {
                    LocalDate.parse(it.startDate, dateTimeFormatter)
                }
            }
            .addOnFailureListener {
                Log.d(WEEKLY_REPORTS_LABEL,"Failed to save new individual reports.")
            }
    }

    fun readWeeklyIndividualReportsFromDatabase(reportStartDate: String, reportEndDate: String, individualReportsAdapter: IndividualReportsAdapter) {
        var startDateString = ""
        var endDateString = ""
        reportStartDate.forEach {
            if (it.toString() != "-") {
                startDateString += it.toString()
            }
        }
        reportEndDate.forEach {
            if (it.toString() != "-") {
                endDateString += it.toString()
            }
        }
        val documentID = "$startDateString-$endDateString"


        firebaseDB.collection(USERS).document(FirebaseAuth.getInstance().currentUser!!.uid).collection(WEEKLY_REPORTS_LABEL).document(documentID).collection(INDIVIDUAL_REPORTS).get()
            .addOnSuccessListener { individualReports ->
                for (report in individualReports) {
                    val reportEmployeeName = report.data[EMPLOYEE_NAME].toString()
                    val reportEmployeeID = report.data[EMPLOYEE_ID].toString()
                    val reportEmployeeHoursJson = report.data[EMPLOYEE_HOURS].toString()
                    val reportEmployeeHours = if (reportEmployeeHoursJson == "null") {
                        0.0
                    } else {
                        reportEmployeeHoursJson.toDouble()
                    }
                    val reportDistributedTipsJson = report.data[DISTRIBUTED_TIPS].toString()
                    val reportDistributedTips = if (reportDistributedTipsJson == "null") {
                        0.0
                    } else {
                        reportDistributedTipsJson.toDouble()
                    }
                    val reportCollectedBoolean = report.data[COLLECTED_BOOLEAN]
                }

            }
    }

    fun readWeeklyReportsFromDatabase(weeklyReportsAdapter: WeeklyReportsAdapter) {
        val currentUserUID = FirebaseAuth.getInstance().currentUser!!.uid
        firebaseDB.collection(USERS).document(currentUserUID).collection(WEEKLY_REPORTS_LABEL).get()
            .addOnSuccessListener { weeklyReports ->
                for (report in weeklyReports) {
                    val reportStartDate = report.data[START_DATE].toString()
                    val reportEndDate = report.data[END_DATE].toString()
                    val reportTotalHours = report.data[TOTAL_HOURS].toString().toDouble()
                    val reportTotalCollected = report.data[TOTAL_COLLECTED].toString().toDouble()
                    val reportTipRate = report.data[TIP_RATE].toString().toDouble()
                    val reportError = report.data[ROUNDING_ERROR].toString().toInt()



                    weeklyReportsAdapter.addNewWeeklyReport(
                        WeeklyReport(reportStartDate,reportEndDate,
                        mutableListOf(),reportTotalHours,
                        mutableListOf(),reportTotalCollected,
                        reportTipRate,reportError
                    ))
                    Log.d("FirebaseDatabase","Reading Weekly Report: ${report.id}")
                }
                Log.d("FirebaseDatabase","${weeklyReports.size()} All weekly reports read.")
            }
            .addOnFailureListener {
                Log.d("FirebaseDatabase", it.toString())
            }
    }

}






