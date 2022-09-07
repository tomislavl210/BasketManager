package com.lovrekovic.basketmanager.auth.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lovrekovic.basketmanager.auth.model.AuthRepository
import com.lovrekovic.basketmanager.auth.model.models.User

class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    val email = MutableLiveData<String>()
    val username = MutableLiveData<String>()
    val nameSurname = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    var pickedImage: Uri? = null


    val error = MutableLiveData<String>()
    val success = MutableLiveData<Boolean>()

    fun register() {
        try {
            if (email.value != null && password.value != null && nameSurname.value != null && username.value != null) {
                authRepository.registerUser(email.value!!, password.value!!)
                    .addOnCompleteListener { loginTask ->
                        if (loginTask.isSuccessful) {
                            storeUser()
                        } else {
                            loginTask.exception?.printStackTrace()
                            error.postValue("Something went wrong! Check your credentials.")
                        }
                    }

            } else {
                error.postValue("Please fill all the required fields!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            error.postValue("Something went wrong! Check your internet connection and try again")
        }
    }

    private fun storeUser() {
        try {
            val user = User(
                username = username.value.toString(),
                nameSurname = nameSurname.value.toString(),
                email = email.value.toString(),
                password = password.value.toString(),
                gameIds = emptyList()
            )
            Log.d("RegisterViewModel", "storeUser: $pickedImage")
            if (pickedImage != null) {
                authRepository.storeImageToStorage(pickedImage!!)
                    .addOnCompleteListener { storeImageTask ->
                        if (storeImageTask.isSuccessful) {
                            storeImageTask.result.storage.downloadUrl
                                .addOnCompleteListener { downloadUrlTask ->
                                    if (downloadUrlTask.isSuccessful) {
                                        user.photoUrl = downloadUrlTask.result.toString()
                                        storeUserToDB(user)
                                    } else {
                                        downloadUrlTask.exception?.printStackTrace()
                                        error.postValue("Something went wrong! Check your inputted values!")
                                    }
                                }
                        } else {
                            storeImageTask.exception?.printStackTrace()
                            error.postValue("Something went wrong! Check your inputted values!")
                        }
                    }
            } else {
                storeUserToDB(user)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            error.postValue("Something went wrong! Check your inputted values!")
        }
    }

    private fun storeUserToDB(user: User) {
        authRepository.storeUserToDB(user)
            .addOnCompleteListener { storeUserTask ->
                if (storeUserTask.isSuccessful) {
                    success.postValue(true)
                } else {
                    storeUserTask.exception?.printStackTrace()
                    error.postValue("Something went wrong! Check your inputted values!")
                }
            }
    }
}