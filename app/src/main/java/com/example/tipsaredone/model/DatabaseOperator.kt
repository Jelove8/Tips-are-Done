package com.example.tipsaredone.model

import android.provider.ContactsContract
import android.provider.SyncStateContract.Helpers.update
import android.util.Log
import com.example.tipsaredone.adapters.EmployeesAdapter
import com.example.tipsaredone.adapters.WeeklyReportsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DatabaseOperator {

    companion object {
        const val FIREBASE_EMPLOYEES = "FirebaseEmployees"
        const val FIREBASE_REPORTS = "FirebaseReports"

        const val USERS_LABEL = "Users"
        const val EMPLOYEES_LABEL = "Employees"
        const val INDIVIDUAL_REPORTS_LABEL = "Individual Reports"
        const val WEEKLY_REPORTS_LABEL = "Weekly Reports"

        const val HOURS = "hours"
        const val DISTRIBUTED_TIPS = "distributedTips"
        const val START_DATE = "startDate"
        const val END_DATE = "endDate"
        const val ERROR = "error"
        const val COLLECTED_TIPS = "collectedTips"
        const val TOTAL_HOURS = "totalHours"
        const val TOTAL_TIPS = "totalTips"
        const val TIP_RATE = "tipRate"
        const val COLLECTED_BOOL = "collected"
        const val REPORT_ID = "reportID"
        const val TIP_REPORTS = "tipReports"
    }

    private val userID = FirebaseAuth.getInstance().currentUser!!.uid


    fun saveWeeklyReport(weeklyReport: WeeklyReport) {
        Firebase.firestore.collection(USERS_LABEL).document(userID).collection(WEEKLY_REPORTS_LABEL).document(weeklyReport.reportID).set(weeklyReport)
            .addOnSuccessListener {
                Log.d(FIREBASE_EMPLOYEES,"Successfully saved weekly report.")
            }
            .addOnFailureListener {
                Log.d(FIREBASE_REPORTS,"Failed to save weekly report: $it")
            }
    }
    fun saveIndividualReport(individualReport: IndividualReport) {
        Firebase.firestore.collection(USERS_LABEL).document(userID).collection(INDIVIDUAL_REPORTS_LABEL).document(individualReport.reportID).set(individualReport)
            .addOnSuccessListener {
                Log.d(FIREBASE_REPORTS,"Successfully saved individual report.")
            }
            .addOnFailureListener {
                Log.d(FIREBASE_REPORTS,"Failed to save individual report: $it")
            }
    }


    fun initializeEmployeeAdapter(employeesModel: MutableList<Employee>, employeesAdapter: EmployeesAdapter) {
        Firebase.firestore.collection(USERS_LABEL).document(userID).collection(EMPLOYEES_LABEL).get()
            .addOnSuccessListener { employees ->
                for (employee in employees) {
                    val id = employee.data[DatabaseModel.ID].toString()
                    val name = employee.data[DatabaseModel.NAME].toString()
                    val employeeObject = Employee(name,id)
                    employeesModel.add(employeeObject)
                    employeesAdapter.addNewEmployee(employeeObject)
                    Log.d(FIREBASE_EMPLOYEES,"Reading Employee: $name, $id")
                }
                Log.d(FIREBASE_EMPLOYEES,"${employeesAdapter.itemCount} employees read from database.")
            }
    }
    fun initializeIndividualReports(employeeAdapter: EmployeesAdapter) {
        Firebase.firestore.collection(USERS_LABEL).document(userID).collection(INDIVIDUAL_REPORTS_LABEL).get()
            .addOnSuccessListener { reports ->
                var totalIndividualReportsRead = 0
                for (report in reports) {
                    val id = report.data[DatabaseModel.ID].toString()
                    val name = report.data[DatabaseModel.NAME].toString()
                    val reportID = report.data[REPORT_ID].toString()
                    val startDate = report.data[START_DATE].toString()
                    val endDate = report.data[END_DATE].toString()
                    val hours = report.data[HOURS].toString().toDouble()
                    val tips = report.data[DISTRIBUTED_TIPS].toString().toDouble()
                    val error = report.data[ERROR].toString().toInt()
                    val collected = report.data[COLLECTED_BOOL].toString().toBoolean()

                    for (employee in employeeAdapter.getEmployees()) {
                        if (employee.id == report.id) {
                            employee.tipReports.add(IndividualReport(id,name,reportID,startDate,endDate,hours,tips,error,collected))
                        }
                    }
                    totalIndividualReportsRead++
                }
                Log.d(FIREBASE_REPORTS,"$totalIndividualReportsRead individual reports read from database.")
            }
            .addOnFailureListener {
                Log.d(FIREBASE_REPORTS,"Failed to read individual reports: $it")
            }
    }
    fun initializeWeeklyReports(weeklyReportsAdapter: WeeklyReportsAdapter) {
        Firebase.firestore.collection(USERS_LABEL).document(userID).collection(WEEKLY_REPORTS_LABEL).get().addOnSuccessListener { reports ->
                var totalWeeklyReports = 0
                for (report in reports) {
                    val reportID = report.data[REPORT_ID].toString()
                    val startDate = report.data[START_DATE].toString()
                    val endDate = report.data[END_DATE].toString()

                    val collectedTips = report.data[COLLECTED_TIPS].toString().toMutableList()
                    val totalHours = report.data[TOTAL_HOURS].toString().toDouble()
                    val totalTips = report.data[TOTAL_TIPS].toString().toDouble()
                    val tipRate = report.data[TIP_RATE].toString().toDouble()
                    val error = report.data[ERROR].toString().toInt()

                    weeklyReportsAdapter.addNewWeeklyReport(WeeklyReport(reportID,startDate,endDate,mutableListOf(),totalHours,totalTips,tipRate,error))
                    totalWeeklyReports++
                }
                Log.d(FIREBASE_REPORTS,"$totalWeeklyReports weekly reports read from database.")
            }
            .addOnFailureListener {
                Log.d(FIREBASE_REPORTS,"Failed to read weekly reports: $it")
            }
    }

    // Employees

    fun getEmployeesFromDatabase(): MutableList<Employee> {
        val output = mutableListOf<Employee>()
        Firebase.firestore.collection(USERS_LABEL).document(userID).collection(EMPLOYEES_LABEL).get()
            .addOnSuccessListener { employees ->
                for (employee in employees) {
                    val id = employee.data[DatabaseModel.ID].toString()
                    val name = employee.data[DatabaseModel.NAME].toString()
                    output.add(Employee(name,id))
                    Log.d(FIREBASE_EMPLOYEES,"Reading Employee: $name, $id")
                }
                Log.d(FIREBASE_EMPLOYEES,"${output.size} employees read from database.")
            }
        return output
    }

    fun saveNewOrExistingEmployee(newOrExistingEmployee: Employee) {
        Firebase.firestore.collection(USERS_LABEL).document(userID).collection(DatabaseModel.EMPLOYEES).document(newOrExistingEmployee.id).set(newOrExistingEmployee)
            .addOnSuccessListener {
                Log.d(FIREBASE_EMPLOYEES,"Successfully saved employee: ${newOrExistingEmployee.name} (${newOrExistingEmployee.id})")
            }
            .addOnFailureListener {
                Log.d(FIREBASE_EMPLOYEES,"Failed to save employee: ${newOrExistingEmployee.name} (${newOrExistingEmployee.id}) \n     Error Message: $it")
            }
    }

    fun deleteEmployee(selectedEmployee: Employee) {
        Firebase.firestore.collection(USERS_LABEL).document(userID).collection(EMPLOYEES_LABEL).document(selectedEmployee.id).delete()
            .addOnSuccessListener {
                Log.d(FIREBASE_EMPLOYEES, "Employee deleted from database: ${selectedEmployee.name} (${selectedEmployee.id})")
            }
            .addOnFailureListener {
                Log.d(FIREBASE_EMPLOYEES, "Failed to delete employee: ${selectedEmployee.name} (${selectedEmployee.id})")
            }
    }

    fun collectEmployeeTips(employee: Employee, individualReport: IndividualReport) {
        Firebase.firestore.collection(USERS_LABEL).document(userID)
            .collection(EMPLOYEES_LABEL).document(employee.id).update(TIP_REPORTS,employee.tipReports)
            .addOnSuccessListener {
                updateCollectedBoolean(individualReport)
                Log.d(FIREBASE_EMPLOYEES,"Successfully collected ${employee.name}'s tips from ${individualReport.startDate} to ${individualReport.endDate}.")
            }
            .addOnFailureListener {
                Log.d(FIREBASE_EMPLOYEES,"Failed to collect ${employee.name}'s tips from ${individualReport.startDate} to ${individualReport.endDate}.\n    Error: $it")
            }
    }
    private fun updateCollectedBoolean(individualReport: IndividualReport) {
        Firebase.firestore.collection(USERS_LABEL).document(userID)
            .collection(INDIVIDUAL_REPORTS_LABEL).document(individualReport.reportID).set(individualReport)
            .addOnSuccessListener {
                Log.d(FIREBASE_EMPLOYEES, "Successfully updated 'Collected' boolean of ${individualReport.name}'s report.")
            }
            .addOnFailureListener {
                Log.d(FIREBASE_EMPLOYEES, "Failed to update 'Collected' boolean of ${individualReport.name}'s report.")
            }
    }


}
