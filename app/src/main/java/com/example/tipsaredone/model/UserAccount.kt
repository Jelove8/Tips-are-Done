package com.example.tipsaredone.model

import android.util.Log
import androidx.navigation.findNavController
import com.example.tipsaredone.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class UserAccount() {
    companion object {
        const val AUTH = "FirebaseAuth"
    }

    private lateinit var currentUser: FirebaseUser


    fun signInUser(email: String, password: String, callBackListener: ((String?) -> Unit?)) {
        Log.d("FirebaseAuth","Attempting to sign in User: $email")
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    currentUser = FirebaseAuth.getInstance().currentUser!!
                    Log.d(AUTH,"Successfully signed in User: $email")
                    callBackListener.invoke(null)
                } else {
                    callBackListener.invoke("")
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



}