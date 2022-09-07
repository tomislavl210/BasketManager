package com.lovrekovic.basketmanager.auth.model.models

data class User(
    var id: String? = null,
    val username: String? = null,
    val nameSurname: String? = null,
    val email: String? = null,
    val password: String? = null,
    val gameIds: List<String>? = null,
    var photoUrl: String? = null
)
