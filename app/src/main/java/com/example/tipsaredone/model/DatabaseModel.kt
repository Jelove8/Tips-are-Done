package com.example.tipsaredone.model

import android.util.Log
import com.example.tipsaredone.activities.MainActivity
import com.example.tipsaredone.activities.UserLoginActivity
import com.example.tipsaredone.adapters.EmployeesAdapter
import com.example.tipsaredone.adapters.IndividualReportsAdapter
import com.example.tipsaredone.adapters.WeeklyReportsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.security.auth.callback.Callback

class DatabaseModel() {

    companion object {
        const val USERS = "Users"
        const val EMPLOYEES = "Employees"
        const val WEEKLY_REPORTS = "WeeklyReports"
        const val ID = "id"
        const val NAME = "name"
        const val START_DATE = "startDate"
        const val END_DATE = "endDate"
        const val INDIVIDUAL_REPORTS = "individualReports"
        const val TOTAL_HOURS = "totalHours"
        const val COLLECTED_TIPS = "collectedTips"
        const val TOTAL_COLLECTED = "totalCollected"
        const val TIP_RATE = "tipRate"
        const val MAJOR_ROUNDING_ERROR = "majorRoundingError"

        const val EMPLOYEE_NAME = "employeeName"
        const val EMPLOYEE_ID = "employeeID"
        const val EMPLOYEE_HOURS = "employeeHours"
        const val DISTRIBUTED_TIPS = "distributedTips"
        const val COLLECTED_BOOLEAN = "collected"
    }


    private val firebaseDB: FirebaseFirestore = Firebase.firestore
    private val employees = mutableListOf<Employee>()
    private val weeklyReports = mutableListOf<WeeklyReport>()
    private val individualTipReports = mutableListOf<IndividualTipReport>()

    init {
        Log.d("FirebaseDatabase","DataBaseModel initialized.")
        Log.d("FirebaseDatabase","Current User: ")
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
                    Log.d("FirebaseDatabase","Successfully read 'remember me' boolean for UserLogin: $rememberMeBoolean")
                }
            }
            .addOnFailureListener {
                Log.d("FirebaseDatabase","Failed to read 'remember me' boolean for UserLogin: $it")
            }
    }

    fun setRememberUserBoolean(boolean: Boolean, currentUserID: String) {
        firebaseDB.collection(USERS).document(currentUserID).update(UserLoginActivity.REMEMBER_USER_BOOL,boolean)
            .addOnSuccessListener {
                Log.d("FirebaseDatabase","Successfully changed 'remember me' boolean to $boolean.")
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

    fun deleteExistingEmployee(selectedEmployee: Employee) {
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

        firebaseDB.collection(USERS).document(FirebaseAuth.getInstance().currentUser!!.uid).collection(WEEKLY_REPORTS).document(documentID).set(weeklyReport)
            .addOnSuccessListener {
                weeklyReports.add(weeklyReport)
                weeklyReports.sortBy { it.startDate }
                Log.d("FirebaseDatabase", "Successfully saved new weekly report.")
            }
            .addOnFailureListener {
                Log.d("FirebaseDatabase", "Failed to saved new weekly report.")
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


        firebaseDB.collection(USERS).document(FirebaseAuth.getInstance().currentUser!!.uid).collection(WEEKLY_REPORTS).document(documentID).collection(INDIVIDUAL_REPORTS).get()
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
        firebaseDB.collection(USERS).document(currentUserUID).collection(WEEKLY_REPORTS).get()
            .addOnSuccessListener { weeklyReports ->
                for (report in weeklyReports) {
                    val reportStartDate = report.data[START_DATE].toString()
                    val reportEndDate = report.data[END_DATE].toString()
                    val reportTotalHours = report.data[TOTAL_HOURS].toString().toDouble()
                    val reportTotalCollected = report.data[TOTAL_COLLECTED].toString().toDouble()
                    val reportTipRate = report.data[TIP_RATE].toString().toDouble()
                    val reportError = report.data[MAJOR_ROUNDING_ERROR].toString().toInt()



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