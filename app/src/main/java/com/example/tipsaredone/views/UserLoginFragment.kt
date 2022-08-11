package com.example.tipsaredone.views

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.example.tipsaredone.R
import com.example.tipsaredone.databinding.FragmentUserLoginBinding
import com.example.tipsaredone.viewmodels.UserLoginViewModel
import com.google.firebase.auth.FirebaseAuth

class UserLoginFragment : Fragment() {

    private var _binding: FragmentUserLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var userLoginViewModel: UserLoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentUserLoginBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userLoginVM = ViewModelProvider(this)[UserLoginViewModel::class.java]
        userLoginViewModel = userLoginVM

        updateLoginAndSignUpButtonVisibility()

        if (FirebaseAuth.getInstance().currentUser != null) {
            findNavController().navigate(R.id.action_userLoginFragment_to_EmployeeListFragment)
        }

        binding.inputLoginEmail.doAfterTextChanged {
            userLoginViewModel.setEmail(
                if (it.isNullOrEmpty()) { null }
                else { it.toString() })
            updateLoginAndSignUpButtonVisibility()
        }
        binding.inputLoginPassword.doAfterTextChanged {
            userLoginViewModel.setPassword(
                if (it.isNullOrEmpty()) { null }
                else { it.toString() })
            updateLoginAndSignUpButtonVisibility()
        }

        binding.btnUserLogin.setOnClickListener {
            if (checkForValidInputs()) {
                (context as MainActivity).signInUser(binding.inputLoginEmail.text.toString(),binding.inputLoginPassword.text.toString())
            }
        }
        binding.btnSignUp.setOnClickListener {
            if (checkForValidInputs()) {
                (context as MainActivity).signUpNewUser(binding.inputLoginEmail.text.toString(),binding.inputLoginPassword.text.toString())
            }
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    private fun checkForSignedInUser(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }
    private fun checkForValidInputs(): Boolean {
        val output = userLoginViewModel.email.value != null && userLoginViewModel.password.value != null
        if (!output) {
            val toast = "An email and password must be provided."
            (context as MainActivity).makeToastMessage(toast)
        }
        return output
    }
    private fun updateLoginAndSignUpButtonVisibility() {
        if (binding.inputLoginEmail.text.isNotEmpty() && binding.inputLoginPassword.text.isNotEmpty()) {
            val sbGreen = ResourcesCompat.getColor(resources, R.color.starbucks_green, (context as MainActivity).theme)
            binding.btnSignUp.setBackgroundColor(sbGreen)
            binding.btnUserLogin.setBackgroundColor(sbGreen)
            Log.d("UserLogin","hello?1")
        }
        else {
            val wrmNeutral = ResourcesCompat.getColor(resources, R.color.warm_neutral, (context as MainActivity).theme)
            binding.btnSignUp.setBackgroundColor(wrmNeutral)
            binding.btnUserLogin.setBackgroundColor(wrmNeutral)
            Log.d("UserLogin","hello?2")
        }
    }

    private fun initMockUser() {
        val mockPassword = "4.Santiago"
        binding.inputLoginPassword.setText(mockPassword)
        val mockEmail = "jelovalera@gmail.com"
        binding.inputLoginEmail.setText(mockEmail)
    }




















}