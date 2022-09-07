package com.lovrekovic.basketmanager.main.profile.model

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask

class ProfileRepository(
    private val database: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val storage: FirebaseStorage = FirebaseStorage.getInstance(),
    private val auth: FirebaseAuth = Firebase.auth
) {

    fun storeImageToStorage(imageUri: Uri): UploadTask {
        return storage.reference.child("userImages/${auth.currentUser?.uid.toString()}").putFile(imageUri)
    }

    fun updateUserPhoto(downloadUrl: String): Task<Void> {
        return database.collection("users").document(auth.currentUser?.uid.toString()).update("photoUrl", downloadUrl)
    }

    fun logout() {
        return auth.signOut()
    }

    fun getCurrentUser(): Task<DocumentSnapshot> {
        return database.collection("users").document(auth.currentUser?.uid.toString()).get()
    }

    fun getGameById(gameID: String): Task<DocumentSnapshot> {
        return database.collection("games").document(gameID).get()
    }
}