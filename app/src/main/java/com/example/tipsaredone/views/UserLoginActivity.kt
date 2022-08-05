package com.example.tipsaredone.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.tipsaredone.R
import com.example.tipsaredone.databinding.ActivityMainBinding
import com.example.tipsaredone.databinding.ActivityUserLoginBinding
import com.google.firebase.auth.FirebaseAuth

class UserLoginActivity : AppCompatActivity() {

    companion object {
        const val USER_LOGIN = "user_login"
    }

    private lateinit var binding2: ActivityUserLoginBinding

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding2 = ActivityUserLoginBinding.inflate(layoutInflater)
        setContentView(binding2.root)
        val toast = "Both fields must not be empty."

        binding2.btnSignUp.setOnClickListener {
            Toast.makeText(this,toast,Toast.LENGTH_SHORT).show()
        }

        binding2.btnUserLogin.setOnClickListener {
        }



    }



    private fun navigateToEmployeesList() {
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
    }
















































}