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
        userModel = UserAccount()

        if (firebaseAuth.currentUser != null) {
            navigateToMainActivity()
        }

        /*
        if (firebaseAuth.currentUser != null) {
            userModel.getRememberUserCredentials(firebaseAuth.currentUser!!,
            callBackListener = fun(rememberCredentials: Boolean) {
                // User automatically signs in, navigates to main.
                if (rememberCredentials) {
                    setRememberCredentialsCheckboxVisibility(true)
                    val toast = resources.getString(R.string.user_already_signed_in)
                    makeToastMessage(toast)
                    navigateToMainActivity()
                }
                // User already signed in, but stays in LoginActivity.
                else {
                    setRememberCredentialsCheckboxVisibility(false)
                    val email = firebaseAuth.currentUser!!.email.toString()
                    firebaseAuth.signOut()
                    binding.inputEmail.setText(email)
                }
            })
        }
*/

        binding.switchRememberMe.setOnClickListener {
            setRememberCredentialsCheckboxVisibility(false)
        }
        binding.switchRememberMeNot.setOnClickListener {
            setRememberCredentialsCheckboxVisibility(true)
        }

        binding.btnForgotPassword.setOnClickListener {

        }

        binding.btnCreateAccount.setOnClickListener {
            if (checkForNonNullInputs()) {
                val inputEmail = binding.inputEmail.text.toString()
                val inputPassword = binding.inputPassword.text.toString()
                userModel.signUpUser(inputEmail, inputPassword,

                    callBackListener = fun(signUpSuccessful: String?) {
                        if (signUpSuccessful == null) {
                            makeToastMessage(resources.getString(R.string.account_creation_successful))
                            navigateToMainActivity()
                        }
                        else {
                            makeToastMessage(resources.getString(R.string.account_creation_unsuccessful))
                        }
                })
            }
        }
        binding.btnSignIn.setOnClickListener {
            if (checkForNonNullInputs()) {
                val inputEmail = binding.inputEmail.text.toString()
                val inputPassword = binding.inputPassword.text.toString()
                userModel.signInUser(inputEmail, inputPassword,

                callBackListener = fun(signInSuccessful: Boolean) {
                    if (signInSuccessful) {
                        val toast = resources.getString(R.string.sign_in_successful)
                        makeToastMessage(toast)
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

    private fun setRememberCredentialsCheckboxVisibility(isChecked: Boolean) {
        if (isChecked) {
            binding.switchRememberMe.visibility = View.VISIBLE
        }
        else {
            binding.switchRememberMe.visibility = View.GONE
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

