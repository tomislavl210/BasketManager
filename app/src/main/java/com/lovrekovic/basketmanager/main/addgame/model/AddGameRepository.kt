package com.lovrekovic.basketmanager.main.addgame.model

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.lovrekovic.basketmanager.main.allgames.model.models.Game

class AddGameRepository(
    private val database: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    fun addGame(game: Game): Task<Void> {
        return database.collection("games").document(game.id).set(game)
    }
}