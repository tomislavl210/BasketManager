package com.lovrekovic.basketmanager.main.profile.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lovrekovic.basketmanager.auth.model.models.User
import com.lovrekovic.basketmanager.main.profile.model.ProfileRepository

class ProfileViewModel(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user


    val error = MutableLiveData<String>()
    val changeNameSuccess = MutableLiveData<Boolean>()
    val changePhotoSuccess = MutableLiveData<Boolean>()

    fun getCurrentUser() {
        repository.getCurrentUser()
            .addOnCompleteListener { getUserTask ->
                if (getUserTask.isSuccessful) {
                    _user.postValue(getUserTask.result.toObject(User::class.java))
                } else {
                    getUserTask.exception?.printStackTrace()
                    error.postValue("Something went wrong with getting games. Please try again!")
                }
            }
    }

    fun logout() {
        repository.logout()
    }

    fun changeImage(imageUri: Uri) {
        repository.storeImageToStorage(imageUri)
            .addOnCompleteListener { storeImageTask ->
                if (storeImageTask.isSuccessful) {
                    storeImageTask.result.storage.downloadUrl
                        .addOnCompleteListener { downloadUrlTask ->
                            if (downloadUrlTask.isSuccessful) {
                                repository.updateUserPhoto(downloadUrlTask.result.toString())
                                    .addOnCompleteListener { updateUserPhoto ->
                                        if (updateUserPhoto.isSuccessful) {
                                            changePhotoSuccess.postValue(true)
                                        } else {
                                            error.postValue("Something went wrong with getting games. Please try again!")
                                        }
                                    }
                            } else {
                                error.postValue("Something went wrong with getting games. Please try again!")
                            }
                        }
                } else {
                    error.postValue("Something went wrong with getting games. Please try again!")
                }
            }
    }
}