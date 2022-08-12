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

    /*
    fun getRememberUserCredentials(currentUser: FirebaseUser,callBackListener: ((Boolean) -> Unit?)) {
        Firebase.firestore.collection("Users").document(currentUser.uid).get()
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
        firebaseDB.collection(DatabaseModel.USERS).document(FirebaseAuth.getInstance().currentUser!!.uid).update("remember credentials",boolean)
            .addOnSuccessListener {
                if (boolean) {
                    Log.d(AUTH, "Credentials will be saved: ${FirebaseAuth.getInstance().currentUser!!.email}")
                }
                else {
                    Log.d(AUTH, "Credentials will not be saved: ${FirebaseAuth.getInstance().currentUser!!.email}")
                }
            }
            .addOnFailureListener {
                Log.d(AUTH, "Failed to update user's 'rememberUserCredentials' field.")
            }
    }
*/
}