package com.lovrekovic.basketmanager.main.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lovrekovic.basketmanager.auth.model.models.User
import com.lovrekovic.basketmanager.main.allgames.model.models.Game
import com.lovrekovic.basketmanager.main.profile.model.ProfileRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UserGamesViewModel (
    private val repository: ProfileRepository
    ): ViewModel() {

    private val _games = MutableLiveData<List<Game>>()
    val games: LiveData<List<Game>> get() = _games

    val error = MutableLiveData<String>()

    fun getCurrentUser() {
        repository.getCurrentUser()
            .addOnCompleteListener { getUserTask ->
                if(getUserTask.isSuccessful) {
                    getUserTask.result.toObject(User::class.java)?.let { getUserGames(it) }
                } else {
                    getUserTask.exception?.printStackTrace()
                    error.postValue("Something went wrong please try again later!")
                }
            }
    }

    private fun getUserGames(user: User) {
            user.gameIds?.forEach { id ->
                repository.getGameById(id)
                    .addOnCompleteListener { getGameTask ->
                        if(getGameTask.isSuccessful) {
                            val game = getGameTask.result.toObject(Game::class.java)
                            if(game != null) {
                                val gamesList : MutableList<Game> = _games.value as MutableList<Game>? ?: mutableListOf()
                                gamesList.add(game)
                                _games.postValue(gamesList)
                            }
                        } else {
                            getGameTask.exception?.printStackTrace()
                            error.postValue("Something went wrong with getting games. Please try again!")
                        }
                    }
            }
    }

    fun parseDateFromString(date: String): LocalDate {
        return try {
            val pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm")
            LocalDate.parse(date + " 12:00", pattern)
        } catch (e: Exception) {
            e.printStackTrace()
            LocalDate.now()
        }
    }
}