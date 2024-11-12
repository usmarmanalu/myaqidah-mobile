package com.dicoding.myaqidahmobile.core.helper

import com.dicoding.myaqidahmobile.core.firebasemodel.*
import com.google.firebase.auth.*
import com.google.firebase.firestore.*

class FirebaseDataManager {

//    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun getUserReference(userId: String): DocumentReference {
        return firestore.collection("users").document(userId)
    }

    fun getCurrentUserName(userId: String, onSuccess: (String) -> Unit) {
        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val name = document.getString("name")
                    onSuccess.invoke(name.orEmpty())
                }
            }
    }

    fun getCurrentUser(userId: String, onSuccess: (String, String, String, String, String, String) -> Unit) {
        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val name = document.getString("name").orEmpty()
                    val dateBird = document.getString("dateOfBirth").orEmpty()
                    val gender = document.getString("gender").orEmpty()
                    val noHp = document.getString("noHp").orEmpty()
                    val email = document.getString("email").orEmpty()
                    val profileImageUrl = document.getString("profileImageUrl").orEmpty()
                    onSuccess.invoke(name, dateBird, gender, noHp, email, profileImageUrl)
                } else {
                    // Optional: Handle if the document is not found
                    onSuccess.invoke("", "", "", "", "", "")
                }
            }
            .addOnFailureListener {
                // Optional: Handle failure case, e.g., log error or invoke with empty values
                onSuccess.invoke("", "", "", "", "", "")
            }
    }


    fun getHospitalInformation(
        onSuccess: (String, String, String, String, String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore.collection("sejarah_aqidah")
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val document = documents.documents[0]
                    val sejarah = document.getString("sejarah").orEmpty()
                    val visi = document.getString("visi").orEmpty()
                    val misi = document.getString("misi").orEmpty()
                    val motto = document.getString("motto").orEmpty()
                    val imageUrl = document.getString("image").orEmpty()
                    onSuccess.invoke(sejarah, visi, misi, motto, imageUrl)
                } else {
                    onFailure.invoke(Exception("Document does not exist"))
                }
            }
            .addOnFailureListener { exception ->
                onFailure.invoke(exception)
            }
    }

    fun getHakKewajiban(
        onSuccess: (String, String) -> Unit,
        onFailure: (Exception) -> Unit

    ) {
        firestore.collection("hak_kewajiban")
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val document = documents.documents[0]
                    val hak = document.getString("hak").orEmpty()
                    val kewajiban = document.getString("kewajiban").orEmpty()
                    onSuccess.invoke(hak, kewajiban)
                }
            }
            .addOnFailureListener { exception ->
                onFailure.invoke(exception)
            }
    }

    fun getDataBerita(
        onSuccess: (List<Pair<String, String>>) -> Unit,
        onFailure: (Exception) -> Unit

    ) {
        firestore.collection("berita")
            .get()
            .addOnSuccessListener { documents ->
                val items = documents.map { document ->
                    val image = document.getString("image").orEmpty()
                    val url = document.getString("url").orEmpty()
                    image to url
                }
                onSuccess.invoke(items)
            }
            .addOnFailureListener { exception ->
                onFailure.invoke(exception)
            }
    }

    fun getDataGallery(
        onSuccess: (List<Pair<String, String>>) -> Unit,
        onFailure: (Exception) -> Unit

    ) {
        firestore.collection("galery_rawat_jalan")
            .get()
            .addOnSuccessListener { documents ->
                val items = documents.map { document ->
                    val imageUrl = document.getString("image").orEmpty()
                    val title = document.getString("title").orEmpty()
                    imageUrl to title
                }
                onSuccess.invoke(items)
            }
            .addOnFailureListener { exception ->
                onFailure.invoke(exception)
            }
    }

    fun getArticles(onSuccess: (List<Artikel>) -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection("artikel")
            .get()
            .addOnSuccessListener { documents ->
                val articles = mutableListOf<Artikel>()
                for (document in documents) {
                    val artikel = document.toObject(Artikel::class.java)
                    articles.add(artikel)
                }
                onSuccess(articles)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}