package com.lovrekovic.basketmanager.main.addgame.viewmodel

import android.location.Address
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.lovrekovic.basketmanager.main.addgame.model.AddGameRepository
import com.lovrekovic.basketmanager.main.allgames.model.models.Game
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "AddGameViewModel"

class AddGameViewModel(
    private val addGameRepository: AddGameRepository
) : ViewModel() {

    val hour = MutableLiveData<Int>()
    val minute = MutableLiveData<Int>()
    val year = MutableLiveData<Int>()
    val month = MutableLiveData<Int>()
    val day = MutableLiveData<Int>()

    val error = MutableLiveData<String>()
    val success = MutableLiveData<Boolean>()

    fun addGame(address: Address, numOfPlayers: Int) {
        val date = getDateFromPicker()
        val time = getTimeFromTimePicker()
        if (date.isEmpty()) {
            return
        }
        if (time.isEmpty()) {
            return
        }
        addGameRepository.addGame(
            Game(
                numOfPlayers = numOfPlayers,
                cityName = address.getAddressLine(0),
                latitude = address.latitude,
                longitude = address.longitude,
                date = date,
                time = time,
                playerIDs = emptyList()
                )
        )
            .addOnCompleteListener { addGameTask ->
                if(addGameTask.isSuccessful) {
                    success.postValue(true)
                } else {
                    addGameTask.exception?.printStackTrace()
                    error.postValue("Something went wrong. Check your internet connection and try again!")
                }
            }
    }



    private fun getDateFromPicker(): String {
        val calendar = Calendar.getInstance()
        if (year.value != null && month.value != null && day.value != null) {
            calendar.set(year.value!!, month.value!!, day.value!!)
        } else {
            error.postValue("Please select day, month and year specifically")
            return ""
        }
        return SimpleDateFormat("dd/MM/yyyy").format(calendar.time)
    }

    private fun getTimeFromTimePicker(): String {
        val calendar = Calendar.getInstance()
        if (hour.value != null && minute.value != null) {
            calendar.set(Calendar.HOUR_OF_DAY, hour.value!!)
            calendar.set(Calendar.MINUTE, minute.value!!)
        } else {
            error.postValue("Please select hour and minute specifically")
            return ""
        }

        return SimpleDateFormat("HH:mm").format(calendar.time)
    }
}