package com.example.tipsaredone.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tipsaredone.R
import com.example.tipsaredone.databinding.ActivityUserLoginBinding
import com.example.tipsaredone.model.DatabaseModel
import com.google.firebase.auth.FirebaseAuth

class UserLoginActivity : AppCompatActivity() {

    companion object {
        const val LOGIN = "LOGIN_ACTIVITY"
        const val REMEMBER_USER_BOOL = "rememberUserBool"
    }

    private lateinit var binding: ActivityUserLoginBinding
    private var rememberingCurrentUser: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (FirebaseAuth.getInstance().currentUser != null) {
            Log.d(LOGIN,"Current user remembered: ${FirebaseAuth.getInstance().currentUser!!.email}")
            DatabaseModel().readRememberUserBooleanForUserLogin(this)
        }

        binding.switchRememberMe.setOnClickListener {
            rememberCurrentUser(false)
        }
        binding.switchRememberMeNot.setOnClickListener {
            rememberCurrentUser(true)
        }

        binding.btnForgotPassword.setOnClickListener {
            TODO()
        }

        binding.btnCreateAccount.setOnClickListener {
            if (checkForNonNullInputs()) {
                val inputEmail = binding.inputEmail.text.toString()
                val inputPassword = binding.inputPassword.text.toString()
                signUpUser(inputEmail,inputPassword)
            }
        }

        binding.btnSignIn.setOnClickListener {
            if (checkForNonNullInputs()) {
                val inputEmail = binding.inputEmail.text.toString()
                val inputPassword = binding.inputPassword.text.toString()
                signInUser(inputEmail,inputPassword)
            }
        }
    }

    private fun rememberCurrentUser(isRemembered: Boolean) {
        rememberingCurrentUser = isRemembered
        if (isRemembered) { binding.switchRememberMe.visibility = View.VISIBLE }
        else { binding.switchRememberMe.visibility = View.GONE }
    }
    private fun signInUser(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                Log.d(LOGIN,"Successfully signed in user: $email")
                DatabaseModel().setRememberUserBoolean(rememberingCurrentUser, it.user!!.uid)
                if (rememberingCurrentUser) {
                    Log.d(LOGIN,"    User will be remembered")
                } else {
                    Log.d(LOGIN,"    User will not be remembered")
                }
                navigateToMainActivity()
            }
            .addOnFailureListener {
                Log.d(LOGIN,"Failed to sign in user: $email")
                makeToastMessage(resources.getString(R.string.login_failed))
            }
    }
    private fun signUpUser(email: String, password: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                Log.d(LOGIN,"Successfully created new user: $email")
                DatabaseModel().setRememberUserBoolean(rememberingCurrentUser, it.user!!.uid)
                if (rememberingCurrentUser) {
                    Log.d(LOGIN,"    User will be remembered")
                } else {
                    Log.d(LOGIN,"    User will not be remembered")
                }
                navigateToMainActivity()
            }
            .addOnFailureListener {
                Log.d(LOGIN,"Failed to create new user: $email")
                makeToastMessage(resources.getString(R.string.account_creation_unsuccessful))
            }
    }

    private fun checkForNonNullInputs(): Boolean {
        return if (binding.inputEmail.text.isNotEmpty() && binding.inputPassword.text.isNotEmpty()) {
            true
        }
        else {
            val toast = resources.getString(R.string.empty_credentials)
            Toast.makeText(this,toast,Toast.LENGTH_SHORT).show()
            false
        }
    }
    fun navigateToMainActivity() {
        Log.d(LOGIN,"Navigating to MainActivity.")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun makeToastMessage(message: String, durationIsShort: Boolean = true) {
        if (durationIsShort) {
            Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(this,message,Toast.LENGTH_LONG).show()
        }

    }
}

/**
 * Notes
 *
 * I like that all UserLogin related stuff is only used in this Activity and it's invocation of the DatabaseModel.
 *      The only exception is in MainActivity: A function to navigate back to this Activity.
 */





