package com.example.tipsaredone.model

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DatabaseModel() {

    private var firebaseDB: FirebaseFirestore = Firebase.firestore

    private val employees = mutableListOf<Employee>()
    private val weeklyTipReports = mutableListOf<WeeklyTipReport>()
    private val individualTipReports = mutableListOf<IndividualTipReport>()


    fun initializeEmployees(firebaseAuth: FirebaseAuth): MutableList<Employee> {
        val userID = firebaseAuth.currentUser!!.uid

        /*
        firebaseDB.collection("sKitupTj1LdAFTwOccrpsrET1w33").document("Employees").get()
            .addOnSuccessListener {
                it.toObject(MutableList<Employee>)
                employees = it.toObject()
                Log.d("FirebaseDatabase", "DocumentSnapshot successfully read: $it")
            }.addOnFailureListener {
                Log.d("FirebaseDatabase", "Failed to read DocumentSnapshot.")
            }

        firebaseDB.collection().document()

         */



        employees.sortBy { it.name }
        return employees
    }

    fun addNewEmployee(newEmployee: Employee) {
        val currentUserUID = FirebaseAuth.getInstance().currentUser!!.uid
        firebaseDB.collection("Users").document(currentUserUID).collection("Employees").document(newEmployee.id).set(newEmployee)
            .addOnSuccessListener {
                Log.d("FirebaseDatabase", "New employee added.")
            }
            .addOnFailureListener {
                Log.d("FirebaseDatabase", "Employee failed to add")
            }

        employees.add(newEmployee)
        employees.sortBy { it.name }
    }

    fun deleteEmployee(selectedEmployee: Employee) {
        val currentUserUID = FirebaseAuth.getInstance().currentUser!!.uid
        firebaseDB.collection("Users").document(currentUserUID).collection("Employees").document(selectedEmployee.id).delete()
            .addOnSuccessListener {
                Log.d("FirebaseDatabase", "Employee deleted from database: ${selectedEmployee.name}")
            }
            .addOnFailureListener {
                Log.d("FirebaseDatabase", "Failed to delete Employee: ${selectedEmployee.name}")
            }

        employees.remove(selectedEmployee)
    }

    fun updateEmployee(editedEmployee: Employee) {
        val currentUserUID = FirebaseAuth.getInstance().currentUser!!.uid
        firebaseDB.collection("Users").document(currentUserUID).collection("Employees").document(editedEmployee.id).update("name",editedEmployee.name)
        firebaseDB.collection("Users").document(currentUserUID).collection("Employees").document(editedEmployee.id).update("tipReports",editedEmployee.tipReports)
    }



    fun updateJackie() {
        firebaseDB.collection("Users").document("sKitupTj1LdAFTwOccrpsrET1w33").collection("Employees").document("Jackie-522086b5-2223-4e4d-8bcb-d5391fab2d2f")
            .update("name","Jordan")
            .addOnSuccessListener {
                Log.d("FirebaseDatabase", "Employee name updated")
            }
            .addOnFailureListener {
                Log.d("FirebaseDatabase", "Failed to update name")
            }
    }



}