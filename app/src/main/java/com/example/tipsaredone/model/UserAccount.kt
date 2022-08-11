package com.example.tipsaredone.model

import android.util.Log
import androidx.navigation.findNavController
import com.example.tipsaredone.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserAccount(private var currentUser: FirebaseUser?) {
    companion object {
        const val AUTH = "FirebaseAuth"
    }

    private var rememberUserCredentials = false


    fun signInUser(email: String, password: String, callBackListener: ((Boolean) -> Unit?)) {
        Log.d("FirebaseAuth","Attempting to sign in User: $email")
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    currentUser = FirebaseAuth.getInstance().currentUser!!
                    Log.d(AUTH,"Successfully signed in User: $email")
                    callBackListener.invoke(true)
                } else {
                    callBackListener.invoke(false)
                    Log.d(AUTH,"Failed to sign in User: $email")
                }
            }
    }
    fun signUpUser(email: String, password: String): String? {
        var outputString: String? = null
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {

            }
            .addOnFailureListener {
                Log.d(AUTH,"Failed to create new user: $email")
                outputString = it.toString()
            }
        return outputString
    }

    fun getRememberUserCredentials(callBackListener: ((Boolean) -> Unit?)) {
        Firebase.firestore.collection("Users").document(currentUser!!.uid).get()
            .addOnSuccessListener {
                val boolean = it.get("remember credentials") as Boolean
                callBackListener.invoke(boolean)
            }
            .addOnFailureListener {
                callBackListener.invoke(false)
            }
    }
    fun setRememberUserCredentials(boolean: Boolean) {
        val firebaseDB = Firebase.firestore
        firebaseDB.collection(DatabaseModel.USERS).document(currentUser!!.uid).update("remember credentials",boolean)
            .addOnSuccessListener {
                if (boolean) {
                    Log.d(AUTH, "Credentials will be saved: ${currentUser!!.email}")
                }
                else {
                    Log.d(AUTH, "Credentials will not be saved: ${currentUser!!.email}")
                }
            }
            .addOnFailureListener {
                Log.d(AUTH, "Failed to update user's 'rememberUserCredentials' field.")
            }
    }

    fun getRememberUser() {

    }

}