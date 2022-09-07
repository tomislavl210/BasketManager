package com.lovrekovic.basketmanager.main.allgames.model

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

class AllGamesRepository(
    private val database: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    fun getAllGames(): Task<QuerySnapshot> {
        return database.collection("games").get()
    }

    fun getGamesByLocation(name: String): Task<QuerySnapshot> {
        return database.collection("games").whereEqualTo("cityName", name).get()
    }

    fun getGameByID(gameID: String): Task<QuerySnapshot> {
        return database.collection("games").whereEqualTo("id", gameID).get()
    }

    fun getPlayerByID(playerID: String): Task<QuerySnapshot> {
        return database.collection("users").whereEqualTo("id", playerID).get()
    }

}