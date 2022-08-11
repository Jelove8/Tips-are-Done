package com.example.tipsaredone.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.tipsaredone.R
import com.example.tipsaredone.databinding.ActivityLoginBinding
import com.example.tipsaredone.model.UserAccount
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var firebaseAuth: FirebaseAuth
    private var userModel = UserAccount()

    private var rememberUserCredentials = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()


        binding.switchRememberMe.setOnClickListener {
            rememberUserCredentials = false
            binding.switchRememberMe.visibility = View.GONE
            binding.switchRememberMeNot.visibility = View.VISIBLE
        }
        binding.switchRememberMeNot.setOnClickListener {
            rememberUserCredentials = true
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
                callBackListener = fun(message: String?) {
                    if (message.isNullOrEmpty()) {
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

