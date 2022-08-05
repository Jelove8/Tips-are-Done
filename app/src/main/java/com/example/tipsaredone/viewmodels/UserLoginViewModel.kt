package com.example.tipsaredone.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserLoginViewModel : ViewModel() {

    private val _email = MutableLiveData<String?>(null)
    val email: LiveData<String?> = _email

    private val _password = MutableLiveData<String?>(null)
    val password: LiveData<String?> = _password

    fun setEmail(data: String?) {
        _email.value = data
    }
    fun setPassword(data: String?) {
        _password.value = data
    }
}