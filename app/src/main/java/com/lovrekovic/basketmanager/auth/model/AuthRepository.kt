package com.lovrekovic.basketmanager.auth.model

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.lovrekovic.basketmanager.auth.model.models.User

class AuthRepository(
    private val database: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = Firebase.auth,
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
) {

    fun login(email: String, password: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, password)
    }

    fun registerUser(email: String, password: String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email, password)
    }

    fun storeUserToDB(user: User): Task<Void> {
        user.id = auth.currentUser?.uid.toString()
        return database.collection("users").document(auth.currentUser?.uid.toString()).set(user)
    }

    fun storeImageToStorage(imageUri: Uri): UploadTask {
        return storage.reference.child("userImages/${auth.currentUser?.uid.toString()}").putFile(imageUri)
    }

    fun checkAuth(): Boolean {
        return auth.currentUser?.uid != null
    }
}