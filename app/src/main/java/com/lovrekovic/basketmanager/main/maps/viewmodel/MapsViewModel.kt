package com.lovrekovic.basketmanager.main.maps.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lovrekovic.basketmanager.main.allgames.model.models.Game
import com.lovrekovic.basketmanager.main.maps.model.MapsRepository

class MapsViewModel(
    private val mapsRepository: MapsRepository
): ViewModel() {

    private val _allGames = MutableLiveData<List<Game>>()
    val allGames: LiveData<List<Game>> get() = _allGames

    val searchText = MutableLiveData<String>()

    val error = MutableLiveData<String>()
    val isGameAdded = MutableLiveData<Boolean>()

    fun getAllGames() {
        mapsRepository.getAllGames()
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
        mapsRepository.getGamesByLocation(searchText.value.toString())
            .addOnCompleteListener { getGameByLocation ->
                if (getGameByLocation.isSuccessful) {
                    _allGames.postValue(getGameByLocation.result.toObjects(Game::class.java))
                } else {
                    getGameByLocation.exception?.printStackTrace()
                    error.postValue("Something went wrong with getting games. Please try again!")
                }
            }
    }

    fun joinGame(game: Game) {
        mapsRepository.addPlayerToGame(game.id)
            .addOnCompleteListener { addPlayerToGameTask ->
                if(addPlayerToGameTask.isSuccessful) {
                    mapsRepository.addGameToPlayer(game.id)
                        .addOnCompleteListener { addGameToPlayerTask ->
                            if(addGameToPlayerTask.isSuccessful) {
                                isGameAdded.postValue(true)
                            } else {
                                addGameToPlayerTask.exception?.printStackTrace()
                                error.postValue("Something went wrong with getting games. Please try again!")
                            }
                        }
                } else {
                    addPlayerToGameTask.exception?.printStackTrace()
                    error.postValue("Something went wrong with getting games. Please try again!")
                }
            }
    }
}