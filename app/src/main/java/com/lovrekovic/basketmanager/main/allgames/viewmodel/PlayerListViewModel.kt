package com.lovrekovic.basketmanager.main.allgames.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lovrekovic.basketmanager.auth.model.models.User
import com.lovrekovic.basketmanager.main.allgames.model.AllGamesRepository
import com.lovrekovic.basketmanager.main.allgames.model.models.Game

class PlayerListViewModel(
    private val repository: AllGamesRepository
) : ViewModel() {

    private val _players = MutableLiveData<List<User>>()
    val players: LiveData<List<User>> get() = _players

    val error = MutableLiveData<String>()

    fun getGameByID(id: String) {
        try {
            repository.getGameByID(id)
                .addOnCompleteListener { gameByIDTask ->
                    if (gameByIDTask.isSuccessful) {
                        val game = gameByIDTask.result.toObjects(Game::class.java).firstOrNull()
                        game?.let { getPlayers(it) }
                    } else {
                        gameByIDTask.exception?.printStackTrace()
                        error.postValue("Something went wrong with getting games. Please try again!")
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
            error.postValue("Something went wrong with getting games. Please try again!")
        }
    }

    private fun getPlayers(game: Game){
        Log.d("PlayerListViewModel", "getPlayers: $game")
        try {
            game.playerIDs?.let { players ->
                players.forEach { id ->
                    repository.getPlayerByID(id)
                        .addOnCompleteListener { getPlayerTask ->
                            if(getPlayerTask.isSuccessful) {
                                val player = getPlayerTask.result.toObjects(User::class.java).firstOrNull()
                                if(player != null) {
                                    val playerList : MutableList<User> = _players.value as MutableList<User>? ?: mutableListOf()
                                    playerList.add(player)
                                    _players.postValue(playerList)
                                }
                            } else {
                                getPlayerTask.exception?.printStackTrace()
                                error.postValue("Something went wrong with getting games. Please try again!")
                            }
                        }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            error.postValue("Something went wrong with getting games. Please try again!")
        }
    }
}