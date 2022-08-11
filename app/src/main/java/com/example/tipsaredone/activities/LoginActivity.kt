package com.example.tipsaredone.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.tipsaredone.R
import com.example.tipsaredone.databinding.ActivityLoginBinding
import com.example.tipsaredone.model.UserAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var fireStore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userModel: UserAccount


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fireStore = Firebase.firestore
        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth.currentUser != null) {
            userModel = UserAccount(firebaseAuth.currentUser)
            userModel.getRememberUser()
            Log.d(UserAccount.AUTH,"User already signed in.")

            userModel.getRememberUserCredentials(
                callBackListener = fun (bool: Boolean) {
                    if (bool) {
                        Log.d(UserAccount.AUTH, "User signing in.")
                        navigateToMainActivity()
                    } else {
                        Log.d(UserAccount.AUTH, "User NOT signing in.")
                    }
                })
        }
        else {
            userModel = UserAccount(null)
            Log.d(UserAccount.AUTH,"User not signed in.")
        }

        binding.switchRememberMe.setOnClickListener {
            userModel.setRememberUserCredentials(false)
            binding.switchRememberMe.visibility = View.GONE
            binding.switchRememberMeNot.visibility = View.VISIBLE
        }
        binding.switchRememberMeNot.setOnClickListener {
            userModel.setRememberUserCredentials(true)
            binding.switchRememberMe.visibility = View.VISIBLE
            binding.switchRememberMeNot.visibility = View.GONE
        }

        binding.btnForgotPassword.setOnClickListener {

        }

        binding.btnCreateAccount.setOnClickListener {
            if (checkForNonNullInputs()) {
                val email = binding.inputEmail.text.toString()
                val password = binding.inputPassword.text.toString()

                val signUpErrorMessage = userModel.signUpUser(email, password)
                if (signUpErrorMessage == null) {
                    navigateToMainActivity()
                }
                else {
                    makeToastMessage(signUpErrorMessage)
                }
            }
        }
        binding.btnSignIn.setOnClickListener {
            if (checkForNonNullInputs()) {
                val email = binding.inputEmail.text.toString()
                val password = binding.inputPassword.text.toString()

                userModel.signInUser(email,password,
                callBackListener = fun(boolean: Boolean) {
                    if (boolean) {
                        navigateToMainActivity()
                    }
                    else {
                        val toast = resources.getString(R.string.login_failed)
                        makeToastMessage(toast)
                    }
                })
            }
        }

    }

    private fun navigateToMainActivity() {
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
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

    private fun makeToastMessage(message: String, durationIsShort: Boolean = true) {
        if (durationIsShort) {
            Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(this,message,Toast.LENGTH_LONG).show()
        }

    }
}

