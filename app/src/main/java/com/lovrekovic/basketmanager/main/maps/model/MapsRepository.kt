package com.lovrekovic.basketmanager.main.maps.model

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase

class MapsRepository(
    private val database: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = Firebase.auth
) {

    fun getAllGames(): Task<QuerySnapshot> {
        return database.collection("games").get()
    }


    fun getGamesByLocation(name: String): Task<QuerySnapshot> {
        return database.collection("games").whereEqualTo("cityName", name).get()
    }

    fun addPlayerToGame(gameId: String): Task<Void> {
        return database.collection("games").document(gameId).update("playerIDs", FieldValue.arrayUnion(auth.currentUser?.uid.toString()))
    }

    fun addGameToPlayer(gameId: String): Task<Void> {
        return database.collection("users").document(auth.currentUser?.uid.toString()).update("gameIds", FieldValue.arrayUnion(gameId))
    }
}