package com.example.tipsaredone.model

import android.util.Log
import androidx.navigation.findNavController
import com.example.tipsaredone.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserAccount() {

    companion object {
        const val AUTH = "FirebaseAuth"
    }

    fun signInUser(email: String, password: String, callBackListener: ((Boolean) -> Unit?)) {
        Log.d("FirebaseAuth","Attempting to sign in User: $email")
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                Log.d(AUTH,"Successfully signed in user: $email")
                callBackListener.invoke(true)
            }
            .addOnFailureListener {
                Log.d(AUTH,"Failed to sign in user: $email")
                callBackListener.invoke(false)
            }
    }
    fun signUpUser(email: String, password: String, callBackListener: ((String?) -> Unit?)) {
        Log.d("FirebaseAuth","Attempting to create new User: $email")
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                Log.d(AUTH,"New user created: $email")
                callBackListener(null)
            }
            .addOnFailureListener {
                Log.d(AUTH,"Failed to create new user: $email")
                callBackListener(it.toString())
            }
    }


}