package com.dicoding.myaqidahmobile.core.helper

import com.dicoding.myaqidahmobile.core.firebasemodel.*
import com.google.firebase.auth.*
import com.google.firebase.firestore.*

class FirebaseAuthHelper(private val auth: FirebaseAuth, private val db: FirebaseFirestore) {

    fun signUp(
        email: String,
        password: String,
        userData: UserData,
        onSuccess: (FirebaseUser?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            task.isSuccessful
            val user = auth.currentUser
            user?.let {
                saveUserData(it.uid, userData, onSuccess, onFailure)
            }
        }
    }

    fun login(
        email: String,
        password: String,
        onSuccess: (FirebaseUser?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess(auth.currentUser)
                } else {
                    onFailure(task.exception ?: Exception("Login Failed"))
                }
            }
    }

    private fun saveUserData(
        userId: String,
        userData: UserData,
        onSuccess: (FirebaseUser?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("users").document(userId).set(userData)
            .addOnSuccessListener {
                onSuccess(auth.currentUser)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}
