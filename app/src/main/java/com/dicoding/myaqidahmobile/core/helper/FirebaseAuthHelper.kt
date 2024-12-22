package com.dicoding.myaqidahmobile.core.helper

import com.dicoding.myaqidahmobile.core.firebasemodel.*
import com.google.firebase.auth.*
import com.google.firebase.firestore.*
import java.util.*

class FirebaseAuthHelper(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

) {
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
        val db = FirebaseFirestore.getInstance()

        // Create a sub-collection 'dataUser' under 'users' collection
        db.collection("users")
            .document(userId)
            .collection("dataUser")
            .document(userId)
            .set(userData)
            .addOnSuccessListener {
                onSuccess(auth.currentUser)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun saveTargetData(
        stepTarget: Int,
        stepsAchieved: Float,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val userId = auth.currentUser?.uid ?: return
        val status = if (stepsAchieved >= stepTarget) "Tercapai" else "Belum Tercapai"
        val message = if (stepsAchieved >= stepTarget) {
            "Selamat! Target langkah Anda tercapai."
        } else {
            "Sayang sekali! Target langkah Anda belum tercapai."
        }

        val targetData = mapOf(
            "targetSteps" to stepTarget,
            "stepsAchieved" to stepsAchieved,
            "status" to status,
            "message" to message,
            "date" to Date()
        )

        // Store target data in the 'dataStepsTarget' sub-collection
        db.collection("users")
            .document(userId)
            .collection("dataStepsTarget")
            .add(targetData)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }
}
