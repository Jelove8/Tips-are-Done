package com.example.tipsaredone.model

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.math.E

class DatabaseOperator {

    companion object {
        const val FIREBASE_EMPLOYEES = "FirebaseEmployees"

        const val USERS_LABEL = "Users"
        const val EMPLOYEES_LABEL = "Employees"
    }

    private val userID = FirebaseAuth.getInstance().currentUser!!.uid

    fun saveNewOrExistingEmployee(newOrExistingEmployee: Employee) {
        Firebase.firestore.collection(USERS_LABEL).document(userID).collection(DatabaseModel.EMPLOYEES).document(newOrExistingEmployee.id).set(newOrExistingEmployee)
            .addOnSuccessListener {
                Log.d(FIREBASE_EMPLOYEES,"Successfully saved employee: ${newOrExistingEmployee.name}")
            }
            .addOnFailureListener {
                Log.d("FirebaseDatabase","Failed to save employee: ${newOrExistingEmployee.name}")
                Log.d("FirebaseDatabase","                   $it")
            }
    }
    fun deleteEmployee(selectedEmployee: Employee) {
        Firebase.firestore.collection(USERS_LABEL).document(userID).collection(EMPLOYEES_LABEL).document(selectedEmployee.id).delete()
            .addOnSuccessListener {
                Log.d("FirebaseDatabase", "Employee deleted from database: ${selectedEmployee.name}")
            }
            .addOnFailureListener {
                Log.d("FirebaseDatabase", "Failed to delete employee: ${selectedEmployee.name}")
            }
    }
}
