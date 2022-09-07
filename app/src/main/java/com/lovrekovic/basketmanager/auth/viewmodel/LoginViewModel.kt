package com.lovrekovic.basketmanager.auth.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lovrekovic.basketmanager.auth.model.AuthRepository

class LoginViewModel(
    private val authRepository: AuthRepository
): ViewModel(){


    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()


    val error = MutableLiveData<String>()
    val success = MutableLiveData<Boolean>()

    init {
        checkAuth()
    }

    fun login(){
        try {
            authRepository.login(email.value!!, password.value!!)
                .addOnCompleteListener { loginTask ->
                    if(loginTask.isSuccessful){
                        success.postValue(true)
                    } else {
                        loginTask.exception?.printStackTrace()
                        error.postValue("Something went wrong! Check your credentials.")
                    }
                }
        } catch (e: Exception){
            e.printStackTrace()
            error.postValue("Something went wrong! Check your internet connection and try again")
        }
    }


    private fun checkAuth() {
        success.postValue(authRepository.checkAuth())
    }
}