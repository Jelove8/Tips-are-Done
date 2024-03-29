package com.example.tipsaredone.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tipsaredone.activities.UserLoginActivity
import com.example.tipsaredone.activities.MainActivity
import com.example.tipsaredone.databinding.FragmentSettingsBinding
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentSettingsBinding.inflate(layoutInflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (context as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        binding.btnSignOut.setOnClickListener {
            val userEmail = FirebaseAuth.getInstance().currentUser!!.email
            FirebaseAuth.getInstance().signOut()

            (context as MainActivity).navigateToUserLoginActivity()

            Log.d(UserLoginActivity.LOGIN,"User signed out: $userEmail ")
        }
    }
    override fun onStart() {
        super.onStart()
        (context as MainActivity).displayNavbar(true)
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}