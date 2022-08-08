package com.example.tipsaredone.model

import android.util.Log
import com.example.tipsaredone.adapters.EmployeesAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DatabaseModel() {

    companion object {
        const val USERS = "Users"
        const val EMPLOYEES = "Employees"
        const val ID = "id"
        const val NAME = "name"
    }

    private var currentUserUID: String = FirebaseAuth.getInstance().currentUser!!.uid

    private val firebaseDB: FirebaseFirestore = Firebase.firestore
    private val employees = mutableListOf<Employee>()
    private val weeklyTipReports = mutableListOf<WeeklyTipReport>()
    private val individualTipReports = mutableListOf<IndividualTipReport>()

    init {
        Log.d("FirebaseDatabase","DataBaseModel initialized.")
        Log.d("FirebaseDatabase","Current User: $currentUserUID")
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
        firebaseDB.collection(USERS).document(currentUserUID).collection(EMPLOYEES).document(newEmployee.id).set(newEmployee)
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
        firebaseDB.collection("Users").document(currentUserUID).collection("Employees").document(selectedEmployee.id).delete()
            .addOnSuccessListener {
                Log.d("FirebaseDatabase", "Employee deleted from database: ${selectedEmployee.name}")
            }
            .addOnFailureListener {
                Log.d("FirebaseDatabase", "Failed to delete Employee: ${selectedEmployee.name}")
            }

        employees.remove(selectedEmployee)
    }

    fun updateEmployee(selectedEmployee: Employee) {
        firebaseDB.collection(USERS).document(currentUserUID).collection(EMPLOYEES).document(selectedEmployee.id).set(selectedEmployee)
            .addOnSuccessListener {
                Log.d("FirebaseDatabase","Successfully updated employee:   ${selectedEmployee.name}") }
            .addOnFailureListener { Log.d("FirebaseDatabase","Failed to update employee:    ${selectedEmployee.name}    $it")}
    }

}