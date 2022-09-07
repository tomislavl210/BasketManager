package com.lovrekovic.basketmanager.main.allgames.model.models

import java.util.*
import kotlin.collections.ArrayList

data class Game(
    val id: String = UUID.randomUUID().toString(),
    val numOfPlayers: Int? = null,
    val cityName: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val date: String? = null,
    val time: String? = null,
    val playerIDs: List<String>? = null
)
