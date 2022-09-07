package com.lovrekovic.basketmanager.main.allgames.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lovrekovic.basketmanager.main.allgames.model.AllGamesRepository
import com.lovrekovic.basketmanager.main.allgames.model.models.Game

class AllGamesViewModel(
    private val allGamesRepository: AllGamesRepository
) : ViewModel() {

    private val _allGames = MutableLiveData<List<Game>>()
    val allGames: LiveData<List<Game>> get() = _allGames

    val searchText = MutableLiveData<String>()

    val error = MutableLiveData<String>()

    fun getAllGames() {
        allGamesRepository.getAllGames()
            .addOnCompleteListener { allGamesTask ->
                if (allGamesTask.isSuccessful) {
                    _allGames.postValue(allGamesTask.result.toObjects(Game::class.java))
                } else {
                    allGamesTask.exception?.printStackTrace()
                    error.postValue("Something went wrong with getting games. Please try again!")
                }
            }
    }

    fun searchGameByLocation() {
        allGamesRepository.getGamesByLocation(searchText.value.toString())
            .addOnCompleteListener { getGameByLocation ->
                if (getGameByLocation.isSuccessful) {
                    _allGames.postValue(getGameByLocation.result.toObjects(Game::class.java))
                } else {
                    getGameByLocation.exception?.printStackTrace()
                    error.postValue("Something went wrong with getting games. Please try again!")
                }
            }
    }
}